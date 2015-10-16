package no.progconsult.camel;

import no.progconsult.camel.config.SpringConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bno
 */
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final int port;

    private final Server server;

    public static final String CONTEXT_PATH = "/";


    public Application(int port) {
        this.port = port;
        server = new Server(port);
    }

    public static void main(String[] args) throws Exception {
        try {
            // Create a basic jetty server object that will listen on port 8080.
            // Note that if you set this to port 0 then a randomly available port
            // will be assigned that you can either look in the logs for the port,
            // or programmatically obtain it for use in test cases.
            Integer webappPort = Integer.parseInt(System.getProperty("server.port", "8080"));
            final Application application = new Application(webappPort);
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

        // The ServletHandler is a dead simple way to create a context handler
        // that is backed by an instance of a Servlet.
        // This handler then needs to be registered with the Server object.
//        ServletHandler handler = new ServletHandler();
//        server.setHandler(handler);

        // Passing in the class for the Servlet allows jetty to instantiate an
        // instance of that Servlet and mount it on a given context path.

        // IMPORTANT:
        // This is a raw Servlet, not a Servlet that has been configured
        // through a web.xml @WebServlet annotation, or anything similar.
//        handler.addServletWithMapping(HelloServlet.class, "/*");


        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(CONTEXT_PATH);
//        ServletHolder camelServlet = new ServletHolder("CamelServlet", CamelHttpTransportServlet.class);
//        context.addServlet(camelServlet, "/api/*");


        context.addEventListener(new ContextLoaderListener());
//        context.addEventListener(new RequestContextListener());

        //bootstrap the context with AnnotationConfigWebApplicationContext - needed to setup java based context
        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());

        //default configuration class location must be set
        context.setInitParameter("contextConfigLocation", SpringConfig.class.getName());


        server.setHandler(context);

        // Start things up!
        try {
            server.start();
        } catch (Exception e) {
            log.error("Error during Jetty startup. Exiting", e);
            System.exit(2);
        }

        int localPort = port;
        log.info("Server started on http://localhost:{}{}", localPort, CONTEXT_PATH);


        // The use of server.join() the will make the current thread join and
        // wait until the server is done executing.
        // See
        // http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
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


    @SuppressWarnings("serial")
    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException,
                IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from HelloServlet</h1>");
        }
    }
}
