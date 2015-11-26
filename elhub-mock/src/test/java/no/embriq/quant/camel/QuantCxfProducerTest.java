package no.embriq.quant.camel;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jws.WebMethod;

/**
 * @author bno
 */
public class QuantCxfProducerTest {

    /**
     * Test correct method is found on given class and request type.
     *
     * @throws Exception
     */
    @Test
    public void testDiscoverOperation() throws Exception {
        QuantCxfProducer quantCxfProducer = new QuantCxfProducer(new QuantCxfEndpoint());
        String operation = quantCxfProducer.discoverOperation(ServiceClass.class, Integer.class);
        Assert.assertEquals(operation, "operation1");
    }

    interface ServiceClass {
        @WebMethod(operationName = "operation1")
        void operation1(Integer number);

        @WebMethod(operationName = "operation2")
        void operation2(String text);
    }
}