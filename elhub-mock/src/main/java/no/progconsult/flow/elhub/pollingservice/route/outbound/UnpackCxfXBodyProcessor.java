package no.progconsult.flow.elhub.pollingservice.route.outbound;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;

/**
 * This class unpacks the jaxb request from the cxf MessageContentsList that is in the exchange body.
 */
public class UnpackCxfXBodyProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange.getIn().getBody() instanceof MessageContentsList) {
            Object unpackedBody = exchange.getIn().getBody(MessageContentsList.class).get(0);
            exchange.getIn().setBody(unpackedBody);
        }
    }
}
