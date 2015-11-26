package no.embriq.quant.camel;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.apache.camel.component.cxf.common.message.CxfConstants.OPERATION_NAME;

/**
 * Extend of CxfProducer. Adds function for auto discover operation based on given request type.
 *
 * @author bno
 */
public class QuantCxfProducer extends CxfProducer {

    private static transient final Logger LOG = LoggerFactory.getLogger(QuantCxfProducer.class);

    private QuantCxfEndpoint cxfEndpoint;

    private Map<Class<?>, String> operations = new HashMap<>();

    public QuantCxfProducer(QuantCxfEndpoint cxfEndpoint) throws Exception {
        super(cxfEndpoint);
        this.cxfEndpoint = cxfEndpoint;
    }

    @Override
    public void process(Exchange camelExchange) throws Exception {
        if (cxfEndpoint.isDiscoverOperation()) {
            discoverOperation(camelExchange);
        }
        super.process(camelExchange);
    }

    @Override
    public boolean process(Exchange camelExchange, AsyncCallback callback) {
        if (cxfEndpoint.isDiscoverOperation()) {
            discoverOperation(camelExchange);
        }
        return super.process(camelExchange, callback);
    }

    /**
     * Fetch webservice operation name associated with given Camel Exchange. The operation is set on the the Camel Exchange
     * header, by key operationName.
     *
     * @param camelExchange
     */
    private void discoverOperation(Exchange camelExchange) {
        String operation = discoverOperation(cxfEndpoint.getServiceClass(), camelExchange.getIn().getBody().getClass());
        if (operation != null) {
            camelExchange.getIn().setHeader(OPERATION_NAME, operation);
        }
    }

    /**
     * Fetch webservice operation name from given request and service class. The service class can contain several the webservice
     * operation, the correct operation is found by matching request type.
     *
     * @param serviceClazz
     * @param requestClazz
     * @return String the webservice operation.
     */
    String discoverOperation(Class<?> serviceClazz, Class<?> requestClazz) {
        for (Method method : serviceClazz.getDeclaredMethods()) {
            Type[] types = method.getParameterTypes();
            for (Type type : types) {
                if (type.getTypeName().equals(requestClazz.getTypeName())) {
                    return method.getDeclaredAnnotation(WebMethod.class).operationName();
                }
            }
        }
        return null;
    }
}
