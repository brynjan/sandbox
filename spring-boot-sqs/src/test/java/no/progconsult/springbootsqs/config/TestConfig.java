package no.progconsult.springbootsqs.config;

import org.slf4j.Logger;
import org.springframework.boot.test.context.TestConfiguration;

import static org.slf4j.LoggerFactory.getLogger;

@TestConfiguration
public class TestConfig {

    private transient static final Logger LOG = getLogger(TestConfig.class);


//    @Bean(destroyMethod = "stopSqsServer")
//    public SqsServerMock sqsServerMock() {
//        int i = 0;
//        Optional<SqsServerMock> sqsServerMock;
//        //Iterate port if unable to start sqs. This could happen if previouse test spring context isn't completely shut down.
//        do {
//            sqsServerMock = startSqsServerMock(55670 + i);
//        } while (!sqsServerMock.isPresent() && i++ < 10);
//
//        return sqsServerMock.orElseThrow(() -> new RuntimeException("Failed to start sqs"));
//    }
//
//    private static Optional<SqsServerMock> startSqsServerMock(int port) {
//        try {
//            LOG.info("Trying to start sqs on port {}", port);
//            SqsServerMock sqsServerMock = new SqsServerMock(port);
//            LOG.info("SQS started on port {}", port);
//            return Optional.of(sqsServerMock);
//        } catch (Exception bindException) {
//            LOG.info("Failed to start sqs on port {}", port);
//            return Optional.empty();
//        }
//    }

//    @Primary
//    @Bean
//    public AmazonSQSAsync amazonSQS(SqsServerMock sqsServerMock) {
//        SqsAsyncClientMock sqsClientMock = new SqsAsyncClientMock(sqsServerMock.getPort());
//        AbstractSystemTest.setupSqsQueues(sqsClientMock);
//        return sqsClientMock;
//    }

}
