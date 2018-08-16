package no.progconsult;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-16.
 */
public class ReceiveFromSQSTest extends AbstractSystemTest {

    @Test
    public void receiveMessage() {
        String queue = environment.getProperty("sqs.in");
        String messageId = send(queue, "test");
        waitUntilMessageDeleted(queue);
    }
}
