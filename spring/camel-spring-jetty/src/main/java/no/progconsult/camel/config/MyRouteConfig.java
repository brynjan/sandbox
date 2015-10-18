package no.progconsult.camel.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * A simple example router from a file system to an ActiveMQ queue and then to a file system
 *
 * @version 
 */
@Configuration
@ComponentScan("no.progconsult.camel")
public class MyRouteConfig extends CamelConfiguration{
     

    /**
     * Returns the CamelContext which support Spring
     */
//    @Override
//    protected CamelContext createCamelContext() throws Exception {
////        return new SpringCamelContext(getApplicationContext());
//        return super.createCamelContext();
//    }



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

     
    public static class SomeBean {
 
        public void someMethod(String body) {
            System.out.println("Received: " + body);
        }
 
    }
 
//    @Bean
//    @Override
//    public RouteBuilder route() {
//        return new RouteBuilder() {
//            public void configure() {
//                // you can configure the route rule with Java DSL here
//
//                // populate the message queue with some messages
//                from("file:target/data?noop=true").
//                        to("seda:test.MyQueue");
//
//                from("seda:test.MyQueue").
//                        to("file://target/test?noop=true");
//
//                // set up a listener on the file component
//                from("file://target/test?noop=true").
//                        bean(new SomeBean());
//            }
//        };
//    }
 
    public void afterPropertiesSet() throws Exception {
        // just to make SpringDM happy do nothing here
    }
 
}