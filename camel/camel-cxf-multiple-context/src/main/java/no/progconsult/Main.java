package no.progconsult;

import no.progconsult.maincontext.SpringCamelConfig;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import static java.util.EnumSet.allOf;
import static no.progconsult.maincontext.CXFServletFactory.getCxfServlet;
import static org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;

/**
 * Application to launch the customer-adapter camel application in a standalone servlet container.
 */

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String CONTEXT_PATH = System.getProperty("server.context.path", "/");

    private final int webappPort;
    private final Server server;

    public Main(Integer webappPort) {
        this.webappPort = webappPort;
        this.server = new Server(this.webappPort);
    }

    // Set system property -Duse.elasticmq.as.sqs.mock=true when starting locally, see AWSConfig
    public static void main(String[] args) {
        System.setProperty("jdk.xml.maxOccurLimit", "9999");
        try {
            Integer webappPort = Integer.parseInt(System.getProperty("server.port", "8089"));
            Main application = new Main(webappPort);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOG.debug("ShutdownHook triggered. Exiting application");
                application.stop();
            }));
            application.start();
            LOG.debug("Finished waiting for Thread.currentThread().join()");
            application.stop();
        } catch (RuntimeException e) {
            LOG.error("Error during startup.", e);
            System.exit(1);
        }
    }


    public void start() {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(CONTEXT_PATH);
        CXFServlet cxf = getCxfServlet();
        ServletHolder cxfServlet = new ServletHolder("cxfServlet", cxf);
        context.addServlet(cxfServlet, "/*");



        context.addEventListener(new ContextLoaderListener());
        context.addEventListener(new RequestContextListener());

        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.setInitParameter("contextConfigLocation", SpringCamelConfig.class.getName());

        //Spring security
        FilterRegistration.Dynamic securityFilter = context.getServletContext()
                                                           .addFilter(DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        securityFilter.addMappingForUrlPatterns(allOf(DispatcherType.class), false, "/*");

        server.setHandler(context);





        try {
            server.start();
//            new SubContextCreator().startSubContext();




        } catch (Exception e) {
            LOG.error("Error during Jetty startup. Exiting", e);
            System.exit(2);
        }
        int localPort = getPort();
        LOG.info("Server started on http://localhost:{}{}", localPort, CONTEXT_PATH);
        try {
            server.join();
        } catch (InterruptedException e) {
            LOG.error("Jetty server thread when join. Pretend everything is OK.", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            LOG.warn("Error when stopping Jetty server", e);
        }
    }

    public int getPort() {
        return webappPort;
    }
}
