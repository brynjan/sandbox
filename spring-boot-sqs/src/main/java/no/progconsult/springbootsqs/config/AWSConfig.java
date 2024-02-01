package no.progconsult.springbootsqs.config;

import org.springframework.context.annotation.Configuration;


@Configuration
public class AWSConfig {


//    @Bean
//    SqsAsyncClient sqsAsyncClient(){
//        return SqsAsyncClient
//                .builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider
//                        .create(AwsBasicCredentials.create(accessKey, secretKey)))
//                .build();
//        // add more Options
//    }

//    @Bean
//    AWSCredentialsProvider awsCredentialsProvider(Environment env) {
//        return new AmazonCredentialsProvider(env.getRequiredProperty("cognito.credentials.provider.awsAccountId"),
//                Regions.fromName(env.getRequiredProperty("cognito.credentials.provider.awsRegion")),
//                env.getRequiredProperty("cognito.credentials.provider.identityPoolId"),
//                env.getRequiredProperty("cognito.credentials.provider.userPoolId"),
//                env.getRequiredProperty("cognito.credentials.provider.userPoolApplicationId"),
//                env.getRequiredProperty("cognito.credentials.provider.username"),
//                env.getRequiredProperty("cognito.credentials.provider.password"));
//    }


//    @Bean
//    SqsAsyncClient sqsAsyncClient(){
//        return SqsAsyncClient
//                .builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider
//                        .create(AwsBasicCredentials.create(accessKey, secretKey)))
//                .build();
//        // add more Options
//    }


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
