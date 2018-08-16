package no.progconsult;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import no.progconsult.common.SqsAsyncClientMock;
import no.progconsult.common.SqsServerMock;
import no.progconsult.config.TestConfig;
import no.progconsult.springbootsqs.SpringBootSqsApplication;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-06.
 */
@SpringBootTest(classes = {SpringBootSqsApplication.class, TestConfig.class})
@TestPropertySource("classpath:springbootsqs-test.properties")
public abstract class AbstractSystemTest extends AbstractTestNGSpringContextTests {

    private transient static final Logger LOG = getLogger(AbstractSystemTest.class);

    protected SqsServerMock sqsServerMock;

    @Autowired
    protected SqsAsyncClientMock sqsClientMock;

    @Autowired
    protected SimpleMessageListenerContainer simpleMessageListenerContainer;

    @Autowired
    protected Environment environment;

    @BeforeSuite
    public void setupSuite() {
        sqsServerMock = new SqsServerMock();
    }

    @AfterSuite
    public void afterTest() {
        try {
            sqsServerMock.stopSqsServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void shutdownAgent() {
        //Stopping the container in order to avoid nasty stacktrace.
        simpleMessageListenerContainer.stop();
    }

    public static void setupSqsQueues(SqsAsyncClientMock sqsClientMock, Environment environment) {
        //Queues must exists before the SimpleMessageListenerContainer is started. Receivers are not started on queues not found at the time the container starts.
        sqsClientMock.createQueueIfNotExists(environment.getProperty("sqs.in"));
    }

    public void waitUntilMessageDeleted(String queueName) {
        sqsClientMock.waitUntilMessageDeleted(sqsClientMock.queueUrl(queueName));
    }

    private String getQueueUrl(String queue) {
        return sqsClientMock.getQueueUrl(queue).getQueueUrl();
    }

    protected String send(String queue, String body) {
        SendMessageRequest messageRequest = new SendMessageRequest()
                .withQueueUrl(getQueueUrl(queue))
                .withMessageBody(body);
//                .withMessageAttributes(sqsMessageAttributes);

        String messageId = sqsClientMock.sendMessage(messageRequest).getMessageId();
        return messageId;
    }
}
