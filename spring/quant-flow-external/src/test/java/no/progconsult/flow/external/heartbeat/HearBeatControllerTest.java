package no.progconsult.flow.external.heartbeat;


import no.progconsult.flow.external.Application;
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

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
@DirtiesContext
public class HearBeatControllerTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private int port;

    @Test(groups = {"disabled"})
    public void testConnectOracle() {
        System.out.println("testConnectOracle()");
    }

    @Test
    public void testHeartbeat() {
        ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
                "http://localhost:" + this.port + "/heartbeat", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(entity.getBody(), "Hello respons fra heartbeat");
    }
}
