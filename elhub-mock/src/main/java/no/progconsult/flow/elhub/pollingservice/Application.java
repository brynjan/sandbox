package no.progconsult.flow.elhub.pollingservice;

import no.progconsult.flow.elhub.pollingservice.configuration.SpringCamelConfig;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

/**
 * Application to launch the elwin-adapter camel application in a standalone servlet container.
 */

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static final String CONTEXT_PATH = "/";

    private int webappPort;

    private Server server;


    public Application(Integer webappPort) {

        this.webappPort = webappPort;
        this.server = new Server(this.webappPort);
    }


    public static void main(String[] args) {
        try {
            Integer webappPort = Integer.parseInt(System.getProperty("server.port", "9088"));
            Application application = new Application(webappPort);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    log.debug("ShutdownHook triggered. Exiting application");
                    application.stop();
                }
            });
            application.start();
            log.debug("Finished waiting for Thread.currentThread().join()");
            application.stop();
        } catch (RuntimeException e) {
            log.error("Error during startup.", e);
            System.exit(1);
        }
    }

    public void start() {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(CONTEXT_PATH);
        ServletHolder cxfServlet = new ServletHolder("cxfServlet", CXFServlet.class);
        context.addServlet(cxfServlet, "/pollingservice/*");

        ServletHolder camelServlet = new ServletHolder("CamelServlet", CamelHttpTransportServlet.class);
        context.addServlet(camelServlet, "/api/*");



        context.addEventListener(new ContextLoaderListener());
        context.addEventListener(new RequestContextListener());

        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.setInitParameter("contextConfigLocation", SpringCamelConfig.class.getName());

//        //Spring security
//        FilterRegistration.Dynamic securityFilter = context.getServletContext().addFilter(
//                AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME,
//                DelegatingFilterProxy.class);
//        securityFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            log.error("Error during Jetty startup. Exiting", e);
            System.exit(2);
        }
        int localPort = getPort();
        log.info("Server started on http://localhost:{}{}", localPort, CONTEXT_PATH);
        try {
            server.join();
        } catch (InterruptedException e) {
            log.error("Jetty server thread when join. Pretend everything is OK.", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.warn("Error when stopping Jetty server", e);
        }
    }

    public int getPort() {
        return webappPort;
    }
}
