package no.progconsult.maincontext;


import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.DefaultPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.properties.PropertiesParser;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.Properties;


/**
 * Configuration that is used both for the runtime config, and test configs. Property binding between camel and spring.
 * Plus camel annotation support.
 */
@Configuration
//Need to import cxf resources for soap endpoints..
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public abstract class AbstractSpringCamelConfig extends CamelConfiguration {
    @Autowired
    private Environment environment;


    /**
     * Handle soap faults in our camel error handlers.
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        //For xsd schema validation. Default is 5000, maxOccurs in a batch is 99999.
        System.setProperty("jdk.xml.maxOccurLimit", "99999");

        final CamelContext camelContext = super.createCamelContext();
        camelContext.setAllowUseOriginalMessage(false);
        camelContext.setUseMDCLogging(true);

        ((SpringCamelContext) camelContext).setName("ISSuiteAdapter");
        camelContext.getShutdownStrategy().setTimeout(environment.getRequiredProperty("camel.context.shutdown.timeout", Integer.class));

        return camelContext;
    }

    /**
     * Spring properties
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Spring properties to camel integration
     */
    @Bean
    PropertiesParser propertiesParser() {
        return new DefaultPropertiesParser() {
            @Override
            public String parseProperty(String key, String value, Properties properties) {
                return environment.getProperty(key);
            }
        };
    }

    /**
     * Spring properties to camel integration
     */
    @Bean
    PropertiesComponent properties() {
        PropertiesComponent properties = new PropertiesComponent();
        properties.setPropertiesParser(propertiesParser());
        return properties;
    }
}
