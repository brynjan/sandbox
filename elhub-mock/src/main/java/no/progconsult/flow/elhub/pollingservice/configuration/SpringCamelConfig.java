package no.progconsult.flow.elhub.pollingservice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * This is the actual configuration we use runtime for spring and camel.
 */
@ComponentScan(basePackages = {"no.progconsult.flow"})
@PropertySources({
        @PropertySource(value = "classpath:elhubpollingservice.properties"),
        @PropertySource(value = "file:elhubpollingservice_overrides.properties", ignoreResourceNotFound = true)
})
public class SpringCamelConfig extends AbstractSpringCamelConfig {
}
