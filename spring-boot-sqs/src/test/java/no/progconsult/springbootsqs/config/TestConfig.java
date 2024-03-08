package no.progconsult.springbootsqs.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import org.slf4j.Logger;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.endpoints.SqsEndpointProvider;

import java.net.URI;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@TestConfiguration
public class TestConfig {

    private transient static final Logger LOG = getLogger(TestConfig.class);



    @Bean
    public SqsServerMock sqsServerMock() {
        SqsServerMock sqsServerMock = new SqsServerMock();
        return sqsServerMock;
    }

    @Primary
    @Bean
    SqsAsyncClient sqsAsyncClient(SqsServerMock sqsServerMock) {

        try {

            int port = sqsServerMock.getPort();
            String endpoint = "http://localhost:" + port;
            String region = "elasticmq";
            String accessKey = "x";
            String secretKey = "x";

            AwsBasicCredentials awsSessionCredentials = AwsBasicCredentials.create(accessKey, secretKey);

            StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(awsSessionCredentials);

            URI myUri = new URI(endpoint);
            SqsAsyncClient sqsAsyncClient = SqsAsyncClient.builder().credentialsProvider(staticCredentialsProvider).region(Region.of(region)).endpointOverride(myUri).build();
            return sqsAsyncClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
