package no.progconsult.flow.elhub.pollingservice.webservice;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple route that takes a pojo cxf soap message and sends it with the cxf x509-signing elhub client.
 */
@Component
public class SigningWsClientTestRoute extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:new.message")
                .transacted()
                .to("cxf:bean:pollMarketProcessesClient");

    }
}
