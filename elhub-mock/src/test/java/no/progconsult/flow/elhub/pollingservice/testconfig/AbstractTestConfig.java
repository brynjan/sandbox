package no.progconsult.flow.elhub.pollingservice.testconfig;


import no.progconsult.flow.elhub.pollingservice.configuration.AbstractSpringCamelConfig;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class setup properties for test and disables default load of routes into camel.
 *
 * @author bno
 */
@PropertySources({
        @PropertySource(value = "classpath:elhubpollingservice.properties"),
        @PropertySource(value = "classpath:elhubpollingservice-test.properties"),
        @PropertySource(value = "file:elhubpollingservice_overrides.properties", ignoreResourceNotFound = true)
})
public abstract class AbstractTestConfig extends AbstractSpringCamelConfig {

    /**
     * Abort default load of Camel routes into the CamelContext.
     * Each test class must instead explicit load the the routes of interest.
     * <p>
     * The org.apache.camel.spring.javaconfig.CamelConfiguration by default
     * loads every route available in the spring context. The camel routes are intially loaded into the spring context by use of
     * spring Component/ComponentScan.
     *
     * @return
     * @see AbstractBaseCamelRouteTest#routes()
     */
    @Override
    public List<RouteBuilder> routes() {
        return new ArrayList<>();
    }
}
