package no.progconsult.springbootsqs;

import io.awspring.cloud.sqs.operations.SqsOperations;
import no.embriq.quant.flow.typelib.common.QFEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-03-08.
 */
public class SystemTest extends AbstractSystemTest{


    @Autowired
    SqsOperations sqsOperations;

    @Test
    public void testName() throws Exception{

        QFEvent qfEvent = new QFEvent()
                .withMessageType("TEST")
                .withPayload("test test");
        sqsOperations.send(sqsSendOptions -> sqsSendOptions.queue("embriq-volueagent-in-test").payload(qfEvent));

        Thread.sleep(10000);
    }
}
