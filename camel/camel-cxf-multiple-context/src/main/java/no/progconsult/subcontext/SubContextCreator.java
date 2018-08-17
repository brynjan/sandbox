package no.progconsult.subcontext;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ExplicitCamelContextNameStrategy;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.apache.cxf.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static no.progconsult.maincontext.CXFServletFactory.getCxfServlet;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2017-11-02.
 */
public class SubContextCreator {

    private transient static final Logger LOG = LoggerFactory.getLogger(SubContextCreator.class);


    public void startSubContext(Bus bus) {


        LOG.info("starting subcontext");
        try {

            ModelCamelContext camelContext = createSubContext();


            final JndiRegistry registry = (JndiRegistry) ((PropertyPlaceholderDelegateRegistry) camelContext.getRegistry()).getRegistry();

//            registry.bind("cxfBus", bus);
//            registry.bind();
//            Bus bus = getCxfServlet().getBus();
            registry.bind("hello2Endpoint", SubWsEndpoints.hello2Endpoint(bus));
            camelContext.addRoutes(new IncomingSubRoute());



            camelContext.start();
            LOG.info("Subcontext started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ModelCamelContext createSubContext() {
        final ModelCamelContext modelCamelContext = new DefaultCamelContext();
        modelCamelContext.setUseMDCLogging(true);
        // Set line logging size to 10000 chars.
        Map<String, String> camelProperties = new HashMap<>();
        camelProperties.put("CamelLogDebugBodyMaxChars", "10000");


        modelCamelContext.setGlobalOptions(camelProperties);
        modelCamelContext.setHandleFault(true);
        modelCamelContext.setNameStrategy(new ExplicitCamelContextNameStrategy("subcontext"));
        //modelCamelContext.getShutdownStrategy().setTimeout(env.getRequiredProperty("camel.context.shutdown.timeout", Integer.class));
        return modelCamelContext;
    }
}
