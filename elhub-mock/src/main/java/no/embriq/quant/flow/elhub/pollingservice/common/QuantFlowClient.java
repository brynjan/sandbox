package no.embriq.quant.flow.elhub.pollingservice.common;

import org.apache.camel.Message;

/**
 * @author marska
 * I represent a client invoking a Quant Flow service.
 * Implementors are responsible for reporting the outcome back in a format apapted to the particular technology
 */
public interface QuantFlowClient {
    String HEADER = "QF_FLOW_CLIENT";
    void reportSuccess(String message, Message exchange);
    void abortAndReportInvalidMessage(String message, Message exchange);
    static QuantFlowClient getClient(Message msg) {
        return (QuantFlowClient) msg.getHeader(HEADER);
    }


    void abortAndReportUnprocessableMessage(String message, Message applyTo);
}
