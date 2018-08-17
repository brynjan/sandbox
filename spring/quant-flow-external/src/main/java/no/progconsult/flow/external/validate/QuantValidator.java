package no.progconsult.flow.external.validate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author bno
 */
public class QuantValidator implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        if(!exchange.getIn().getBody().toString().contains("route")){
            throw new ValidationException("Failed to validate");
        }
    }
}
