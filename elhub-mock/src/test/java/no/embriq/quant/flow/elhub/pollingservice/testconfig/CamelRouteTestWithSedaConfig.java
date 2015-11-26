package no.embriq.quant.flow.elhub.pollingservice.testconfig;

import no.embriq.quant.flow.elhub.pollingservice.configuration.JMSConfig;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(basePackages = {"no.embriq.quant.flow"},
        excludeFilters = {
        /*
        We dont want to load a full spring config with a jms broker in these tests.
        We still want to load the rest of our configuration (routes).
        */
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        value = {JMSConfig.class})
        })

public class CamelRouteTestWithSedaConfig extends AbstractTestConfig {

    /**
     * This camelcontext overrides jms with seda.
     *
     * @return
     * @throws Exception
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("jms", camelContext.getComponent("seda"));
        return camelContext;
    }
}
