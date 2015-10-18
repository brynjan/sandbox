package no.progconsult.camel.config;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Component;

/**
 * @author bno
 */
@Component
public class CamelRoute extends RouteBuilder {

    public void configure() {
        // you can configure the route rule with Java DSL here

        // populate the message queue with some messages
        from("file:target/data?noop=true").
                                                  to("seda:test.MyQueue");

        from("seda:test.MyQueue").
                                         to("file://target/test?noop=true");

        from("direct:start").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                System.out.println("direct:start");
            }
        });

//        // set up a listener on the file component
//        from("file://target/test?noop=true").
//                                                    bean(new SomeBean());
    }
}
