package no.progconsult.camel.config;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author bno
 */
@Component
public class CamelRoute extends RouteBuilder {

    @Value("${test}")
    private String uri;


    public void configure() {
        from("file:target/data?noop=true").to("seda:test.MyQueue");
        from("seda:test.MyQueue").to("file://target/test?noop=true");

        from("{{startEndpoint}}").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                System.out.println("direct:test");
            }
        }).wireTap("activemq:wiretap").to("activemq:complete");

        from("activemq:start").to("activemq:stop");
    }
}
