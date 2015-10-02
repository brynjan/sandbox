package no.progconsult.camel;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author bno
 */

@SpringApplicationConfiguration(classes = SpringCamelApplication.class)
@WebIntegrationTest("server.port:0")
@DirtiesContext
public class HearBeatControllerTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private int port;

    @Test(groups = {"unittest"})
    public void testConnectOracle() {
        System.out.println("testConnectOracle()");
    }

    @Test(groups = {"integrationtest"})
    public void testHeartbeat() {
        ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
                "http://localhost:" + this.port + "/heartbeat", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(entity.getBody(), "Hello respons fra heartbeat");
    }
}
