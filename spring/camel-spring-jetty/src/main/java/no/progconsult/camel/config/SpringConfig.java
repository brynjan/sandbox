package no.progconsult.camel.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author bno
 */

@Configuration
@ComponentScan("no.progconsult.camel")
@PropertySource(value = "classpath:application.properties")
public class SpringConfig extends AbstractCamelConfig{

}
