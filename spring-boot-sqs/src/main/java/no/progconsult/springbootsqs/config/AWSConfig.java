package no.progconsult.springbootsqs.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazon.sqs.javamessaging.SQSSession;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import no.embriq.flow.aws.auth.AmazonCredentialsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;


@Configuration
public class AWSConfig {


    @Bean
    AwsCredentialsProvider awsCredentialsProvider(Environment env) {
        return new AmazonCredentialsProvider(env.getRequiredProperty("cognito.credentials.provider.awsAccountId"),
                Region.of(env.getRequiredProperty("cognito.credentials.provider.awsRegion")),
                env.getRequiredProperty("cognito.credentials.provider.identityPoolId"),
                env.getRequiredProperty("cognito.credentials.provider.userPoolId"),
                env.getRequiredProperty("cognito.credentials.provider.userPoolApplicationId"),
                env.getRequiredProperty("cognito.credentials.provider.username"),
                env.getRequiredProperty("cognito.credentials.provider.password"));
    }


//    @Bean
//    SqsClient sqsClient(AwsCredentialsProvider awsCredentialsProvider, Environment env) {
//
//        SqsClient sqsClient = SqsClient.builder()
//                .region(Region.of(env.getRequiredProperty("cognito.credentials.provider.awsRegion")))
//                .credentialsProvider(awsCredentialsProvider)
//                .build();
//
//
//        return sqsClient;
//    }


//    @Bean
//    public ConnectionFactory connectionFactory(SqsClient sqsClient){
//        ProviderConfiguration providerConfiguration = new ProviderConfiguration();
//
//        SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(providerConfiguration, sqsClient);
//        return sqsConnectionFactory;
//    }

//    @Bean
//    public DestinationResolver destinationResolver() {
//        return new StaticDestinationResolver("embriq-volueagent-in-more");
//    }

//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//
//        DefaultJmsListenerContainerFactory jmsListenerContainerFactory =
//                new DefaultJmsListenerContainerFactory();
//        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
//        jmsListenerContainerFactory.setDestinationResolver(new DynamicDestinationResolver());
////        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//        jmsListenerContainerFactory.setSessionAcknowledgeMode(SQSSession.UNORDERED_ACKNOWLEDGE);
//        jmsListenerContainerFactory.setSessionTransacted(false);
//
//        return jmsListenerContainerFactory;
//    }


    @Bean
    SqsAsyncClient sqsAsyncClient(AwsCredentialsProvider awsCredentialsProvider, Environment env){
        SqsAsyncClient sqsAsyncClient = SqsAsyncClient
                .builder()
                .region(Region.of(env.getRequiredProperty("cognito.credentials.provider.awsRegion")))
                .credentialsProvider(awsCredentialsProvider)
                .build();
        return sqsAsyncClient;
        // add more Options
    }


    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory
                .builder()
//                .configure(options -> options
//                        .messagesPerPoll(5)
//                        .pollTimeout(Duration.ofSeconds(10)))
                .configure(options -> options.maxMessagesPerPoll(1))

                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }


    //Note! Bean name must match with SqsClientConfiguration@ConditionalOnMissingAmazonClient(AmazonSQS.class)
//    @Bean
//    public AmazonSQSAsync amazonSQS(final Environment env) {
//        return new AmazonKMSSQSAsyncClient(AmazonGlobal.DEFAULT_REGION,
//                env.getRequiredProperty("sqs.kms.cmk"),
//                env.getRequiredProperty("sqs.s3.bucket"));
//    }
//
//    @Bean
//    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs,
//                                                                                       MessageConverter messageConverter,
//                                                                                       Environment env) {
//        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
//        factory.setAmazonSqs(amazonSqs);
//        factory.setWaitTimeOut(env.getRequiredProperty("sqs.wait.timeout", Integer.class));
//        factory.setVisibilityTimeout(env.getRequiredProperty("sqs.visibility.timeout", Integer.class));
//        // This is 10 by default, but if it isnt set the threadpool will be sat to 2 (or 3) which will make the Executor reject
//        // taske
//        factory.setMaxNumberOfMessages(10);
//
//        //Plugin in MDC
//        QueueMessageHandler messageHandler = new QueueMessageHandler(Arrays.asList(messageConverter)) {
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                MdcHandler.enableMDC(message);
//                super.handleMessage(message);
//            }
//        };
//        factory.setQueueMessageHandler(messageHandler);
//        return factory;
//    }
//
//    @Bean
//    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync, MessageConverter converter) {
//        return new QueueMessagingTemplate(amazonSQSAsync, (ResourceIdResolver) null, converter) {
//
//            //Adding breadCrumbId as sqs header. Key breadcrumbId.
//            @Override
//            protected Map<String, Object> processHeadersToSend(Map<String, Object> headers) {
//                headers = Utils.nullToEmptyMap(headers);
//                final String breadcrumbId = MDC.get(BREADCRUMB_ID);
//                if (breadcrumbId != null) {
//                    headers.put(BREADCRUMB_ID, breadcrumbId);
//                }
//                return headers;
//            }
//        };
//    }
//
//    @Bean
//    AmazonSNS amazonSNS() {
//        return AmazonSNSClientBuilder.defaultClient();
//    }
//
//    @Bean
//    AmazonS3 amazonS3() {
//        return AmazonS3ClientBuilder.defaultClient();
//    }
//
//    @Bean
//    Receiver receiver(AmazonS3 amazonS3) {
//        return new Receiver(amazonS3);
//    }
//
//    @Bean
//    MessageConverter converter() {
//        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
//        messageConverter.setSerializedPayloadClass(String.class);
//        messageConverter.setObjectMapper(JsonMappers.getObjectMapper());
//        messageConverter.setStrictContentTypeMatch(false);
//        return messageConverter;
//    }
//
//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        return JsonMappers.getObjectMapper();
//    }
}
