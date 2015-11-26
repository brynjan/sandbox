package no.embriq.quant.flow.elhub.pollingservice.webservice;


import no.embriq.quant.flow.elhub.pollingservice.testconfig.AbstractCamelRouteWithSedaTest;

import no.elhub.emif.wsdl.marketprocesses.v1.RequestEndOfSupplyRequest;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataRequest;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Endpoint;
import java.util.Collections;
import java.util.List;

/**
 * This test sends a request with the cert-signing cxf client. The request is processed by a mock-elhub-soap-server.
 * The signed-response should be accepted by the client.
 */
public class WsSecuritySigningClientsTest extends AbstractCamelRouteWithSedaTest {
    @Autowired
    private Environment environment;

    @Produce(uri = "direct:new.message")
    protected ProducerTemplate directProducer;

    private Endpoint elhubWsMock;


    @Test
    public void requestIsSignedAndSignedResponseFromElhubMockIsAccepted() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PollForDataRequest.class);
        JAXBElement<PollForDataRequest> actualMessage = jaxbContext
                .createUnmarshaller()
                .unmarshal(new StreamSource(this
                                .getClass().getClassLoader()
                                .getResourceAsStream("RequestEndOfSupply.xml")),
                        PollForDataRequest.class);
        directProducer.sendBody(actualMessage.getValue());
    }

    @Override
    protected List<Class<? extends RoutesBuilder>> routes() {
        return Collections.singletonList(SigningWsClientTestRoute.class);
    }

    @BeforeClass
    private void launchServer() throws Exception {
        elhubWsMock = Endpoint
                .publish(environment.getProperty("elhub.pollmarketprocesses.ws.url"), new MarketProcessesPollingImpl());
    }

    @AfterClass
    public void shutdown() throws Exception {
        elhubWsMock.stop();
    }
}