package no.progconsult.camel.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A simple example router from a file system to an ActiveMQ queue and then to a file system
 *
 * @version 
 */
@Configuration
public class CamelConfig extends CamelConfiguration{
     

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
//        // setup the ActiveMQ component
////        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
////        connectionFactory.setBrokerURL("vm://localhost.spring.javaconfig?marshal=false&broker.persistent=false&broker.useJmx=false");
//
//        // and register it into the CamelContext
////        JmsComponent answer = new JmsComponent();
////        answer.setConnectionFactory(connectionFactory);
////        camelContext.addComponent("jms", answer);
        super.setupCamelContext(camelContext);
    }

    public void afterPropertiesSet() throws Exception {
        // just to make SpringDM happy do nothing here
    }

    @Bean
    PropertiesComponent properties() {
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:application.properties");
//        properties.setPropertiesParser(propertiesParser());
        return properties;
    }


}