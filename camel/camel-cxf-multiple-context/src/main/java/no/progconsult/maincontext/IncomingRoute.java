package no.progconsult.maincontext;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * I receive messages from SOAP and multicast to dedicated processing queues
 */
@Component
public class IncomingRoute extends SpringRouteBuilder {


    //@formatter:off
    @Override
    public void configure() throws Exception {

        from("cxf:bean:helloEndpoint")
                .routeId("hello")
                .process(new Processor() {@Override
                public void process(Exchange exchange) throws Exception {
                                System.out.println("inne i hello");
                }});



//        from("cxf:bean:hello2Endpoint")
//                .routeId("hello2")
//                .process(new Processor() {@Override
//                public void process(Exchange exchange) throws Exception {
//                                System.out.println("inne i hello2");
//                }});
//

   }
    //@formatter:on
}
