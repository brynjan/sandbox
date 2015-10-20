package no.progconsult.camel;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig extends CamelConfiguration {

    @Bean
    PropertiesComponent properties() {
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:application.properties");
        return properties;
    }
}