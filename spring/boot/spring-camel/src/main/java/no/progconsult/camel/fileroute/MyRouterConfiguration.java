package no.progconsult.camel.fileroute;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRouterConfiguration {

  @Autowired
  CamelContext camelContext;

  @Bean
  RoutesBuilder myRouter() {
    return new RouteBuilder() {
 
      @Override
      public void configure() throws Exception {
        from("file:data/inbox?noop=true").to("file:data/outbox");
//        from("jms:invoices").to("file:/invoices");
      }
 
    };
  }
 
}