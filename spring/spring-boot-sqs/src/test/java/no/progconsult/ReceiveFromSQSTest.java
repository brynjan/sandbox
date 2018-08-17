package no.progconsult;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-16.
 */
public class ReceiveFromSQSTest extends AbstractSystemTest {

    @Test
    public void receiveMessage() {
        String queue = environment.getProperty("sqs.in");
        String sqsMessageId = send(queue, message);
        waitUntilMessageDeleted(queue);
    }

    private String message =
            "{\n"
                    + "  \"messageId\":\"1234\",\n"
                    + "  \"correlationId\":\"corrid\",\n"
                    + "  \"createdTime\":\"2018-05-28T00:00:00\"\n"
                    + "}";
}
