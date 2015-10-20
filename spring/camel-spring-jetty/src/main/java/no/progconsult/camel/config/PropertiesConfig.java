package no.progconsult.camel.config;

import org.apache.camel.component.properties.DefaultPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.properties.PropertiesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @author bno
 */
@Configuration
@PropertySource(value = "classpath:application.properties")
public class PropertiesConfig {

    @Autowired
    Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    PropertiesParser propertiesParser() {
        return new DefaultPropertiesParser() {
            @Override
            public String parseProperty(String key, String value, Properties properties) {
                return environment.getProperty(key);
            }
        };
    }

    @Bean
    PropertiesComponent properties() {
        PropertiesComponent properties = new PropertiesComponent();
        properties.setPropertiesParser(propertiesParser());
        return properties;
    }
}
