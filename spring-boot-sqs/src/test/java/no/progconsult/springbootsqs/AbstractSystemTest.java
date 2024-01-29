package no.progconsult.springbootsqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import no.progconsult.springbootsqs.config.TestConfig;
import no.embriq.quant.flow.pubsub.sdk.Publisher;
import no.embriq.quant.flow.pubsub.sdk.Receiver;
import no.embriq.quant.flow.test.sqs.SqsAsyncClientMock;
import no.embriq.quant.flow.test.sqs.SqsServerMock;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.reset;
import static org.slf4j.LoggerFactory.getLogger;

@SpringBootTest(classes = {SpringBootSqsMain.class, TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Setup wiremock, dynamically http port.
@AutoConfigureWireMock(port = 0)
@TestPropertySource("classpath:springbootsqs-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractSystemTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOG = getLogger(AbstractSystemTest.class);

    protected static final String sqsMeterreadingDeliveries = "embriq-nanometering-meterreading-deliveries-subscription";
    @Autowired
    protected SqsServerMock sqsServerMock;

    @Autowired
    protected SqsAsyncClientMock sqsClientMock;

    @Autowired
    protected WireMockServer wireMockServer;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected Receiver receiverMock;

    @LocalServerPort
    protected int port;

    @Autowired
    protected SimpleMessageListenerContainer simpleMessageListenerContainer;

    @Autowired
    protected Environment environment;

    @Autowired
    protected Publisher publisherMock;

    @BeforeSuite
    public void setupSuite() {
    }

    @AfterSuite
    public void afterTest() {
    }

    @AfterClass
    public void shutdownAgent() {
        //Stopping the container in order to avoid nasty stacktrace.
        simpleMessageListenerContainer.stop();
    }

    @BeforeMethod
    public void resetMocks() {
        sqsClientMock.clearState();
        wireMockServer.resetAll();
        reset(publisherMock);
    }


    @AfterMethod
    public void clearState() {
        try {
            sqsClientMock.clearState();
            wireMockServer.resetAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupSqsQueues(SqsAsyncClientMock sqsClientMock) {
        //Queues must exists before the SimpleMessageListenerContainer is started. Receivers are not started on queues not
        //found at the time the container starts.
        sqsClientMock.createQueueIfNotExists(sqsMeterreadingDeliveries);
    }

    protected String serverUrl() {
        return String.format("http://localhost:%s", port);
    }

    protected String basePathUrl() {
        return serverUrl() + environment.getProperty("base.path");
    }


    public void waitUntilMessageDeleted(String queueName) {
        sqsClientMock.waitUntilMessageDeleted(sqsClientMock.queueUrl(queueName));
    }

    public void waitUntilMessageDeleted(String queueName, int count) {
        sqsClientMock.waitUntilMessageDeleted(sqsClientMock.queueUrl(queueName), count);
    }

    private String getQueueUrl(String queue) {
        return sqsClientMock.getQueueUrl(queue).getQueueUrl();
    }

    protected String send(String queue, String body) {
        SendMessageRequest messageRequest = new SendMessageRequest()
                .withQueueUrl(getQueueUrl(queue))
                .withMessageBody(body);

        return sqsClientMock.sendMessage(messageRequest).getMessageId();
    }

    protected String send(String queue, String body, Map<String, MessageAttributeValue> sqsMessageAttributes) {
        SendMessageRequest messageRequest = new SendMessageRequest()
                .withQueueUrl(getQueueUrl(queue))
                .withMessageBody(body)
                .withMessageAttributes(sqsMessageAttributes);

        return sqsClientMock.sendMessage(messageRequest).getMessageId();
    }

    public Message receiveSingleMessage(final String queueName) {
        return sqsClientMock.receiveSingleMessage(sqsClientMock.queueUrl(queueName));
    }

    public List<Message> receiveMessages(String queueName, int numberOfExpectedMessages) {
        return sqsClientMock.receiveMessages(sqsClientMock.queueUrl(queueName), numberOfExpectedMessages);
    }

    public static String readFile(String filename) {
        try (InputStream in = AbstractSystemTest.class.getClassLoader().getResourceAsStream(filename)) {
            if (in == null) {
                throw new RuntimeException(String.format("File=%s not found.", filename));
            }
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
