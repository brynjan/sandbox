package no.progconsult.camel;

import org.apache.camel.Producer;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.impl.SynchronousDelegateProducer;

/**
 * Extend of CxfEndpoint. The original endpoint has been extended due to missing functionality for finding
 * webservice operation from given request type. When discoverOperation is enabled, the endpoint/producer will
 * find the operation name through reflection.
 *
 * @author bno
 */
public class QuantCxfEndpoint extends CxfEndpoint {

    private boolean discoverOperation = false;

    @Override
    public Producer createProducer() throws Exception {
        Producer answer = new QuantCxfProducer(this);
        if (isSynchronous()) {
            return new SynchronousDelegateProducer(answer);
        } else {
            return answer;
        }
    }

    public boolean isDiscoverOperation() {
        return discoverOperation;
    }

    /**
     * When enabled, the endpoint will try to fetch webservice operation associated with given webservice request.
     *
     * @param discoverOperation
     */
    public void setDiscoverOperation(boolean discoverOperation) {
        this.discoverOperation = discoverOperation;
    }
}
