package no.progconsult.maincontext;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * This is the actual configuration we use runtime for spring and camel.
 */
@ComponentScan(basePackages = {"no.progconsult.maincontext"})
@PropertySources({
        @PropertySource(value = "classpath:cxfmultiple.properties")
})
public class SpringCamelConfig extends AbstractSpringCamelConfig {
}
