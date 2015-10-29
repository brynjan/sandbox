package no.progconsult.camel;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author bno
 */
@Configuration
public class SedaTestConfig extends TestConfig{

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", camelContext.getComponent("seda"));
        return camelContext;
    }
}
