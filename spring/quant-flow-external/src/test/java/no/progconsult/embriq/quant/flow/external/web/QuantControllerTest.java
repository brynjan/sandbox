package no.progconsult.embriq.quant.flow.external.web;

import no.progconsult.embriq.quant.flow.external.Application;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_XML;


@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
@DirtiesContext
public class QuantControllerTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private int port;

    @BeforeClass
    public static void setUpClass() throws Exception {
        BrokerService brokerSvc = new BrokerService();
        brokerSvc.setBrokerName("TestBroker");
        brokerSvc.addConnector("tcp://localhost:61616");
        brokerSvc.start();
    }

    @Test(groups = "jms")
    public void testQuant() throws Exception {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(APPLICATION_XML);
//        HttpEntity<String> requestEntity = new HttpEntity<>("<id>test</id>", requestHeaders);
        HttpEntity<String> requestEntity = new HttpEntity<>("<id>route1</id>", requestHeaders);
        ResponseEntity<String> response = new TestRestTemplate().exchange(getUrl("quant"), POST, requestEntity, String.class);

        System.out.println("Responscode: " + response.getStatusCode());
        assertThat(response.getStatusCode(), equalTo(NO_CONTENT));
        assertThat(response.getBody(), nullValue());


//        Thread.sleep(5000);
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        Future<Boolean> future = executor.submit(() -> {
//            TimeUnit.SECONDS.sleep(2);
//            verify(meterPointWSClient.getWS(), times(1)).importMeterPoint(Matchers.<ImportMeterPointRequestType>anyObject());
//            return true;
//        });
//        future.get(3, SECONDS);
    }

    protected String getUrl(String path) {
        return "http://localhost:" + this.port + "/" + path;
    }


//    @Test
//    public void testQuantFlow() {
//        ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
//                "http://localhost:" + this.port + "/quant", String.class);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        assertEquals(entity.getBody(), "Hello from REST!");
//    }
}
