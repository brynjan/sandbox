package no.progconsult.flow.external.validate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author bno
 */
public class FailedValidationResponse implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getIn().getBody(String.class);
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        StringBuilder sb = new StringBuilder();
        sb.append("ERROR: ");
        sb.append(e.getMessage());
        sb.append("\nBODY: ");
        sb.append(body);
        exchange.getIn().setBody(sb.toString());
    }
}
