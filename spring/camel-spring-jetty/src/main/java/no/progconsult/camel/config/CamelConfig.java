package no.progconsult.camel.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.DefaultPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.properties.PropertiesParser;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * A simple example router from a file system to an ActiveMQ queue and then to a file system
 *
 * @version 
 */
@Configuration
@ComponentScan("no.progconsult.camel")
@PropertySource(value = "classpath:application.properties")
public class CamelConfig extends CamelConfiguration{

    @Autowired
    private Environment environment;

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        super.setupCamelContext(camelContext);
    }

    public void afterPropertiesSet() throws Exception {
        // just to make SpringDM happy do nothing here
    }
}