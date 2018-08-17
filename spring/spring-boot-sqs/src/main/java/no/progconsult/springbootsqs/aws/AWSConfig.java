package no.progconsult.springbootsqs.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.Arrays;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-07-23.
 */
@Configuration
public class AWSConfig {

    //Note! Bean name must match with SqsClientConfiguration@ConditionalOnMissingAmazonClient(AmazonSQS.class)
    @Bean
    public AmazonSQSAsync amazonSQS(Environment env) {
//        return new AmazonKMSSQSAsyncClient(AmazonGlobal.DEFAULT_REGION, env.getRequiredProperty("sqs.kms.cmk"), env.getRequiredProperty("sqs.s3.bucket"));
        return AmazonSQSAsyncClient.asyncBuilder().withRegion(Regions.EU_WEST_1).build();
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs, MessageConverter messageConverter, Environment env) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setWaitTimeOut(env.getRequiredProperty("sqs.wait.timeout", Integer.class));
        //Plugin in MDC. Do better??
        QueueMessageHandler messageHandler = new QueueMessageHandler(Arrays.asList(messageConverter)){
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                SqsUtils.enableMDC(message);
                super.handleMessage(message);
            }
        };
        factory.setQueueMessageHandler(messageHandler);
        return factory;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    MessageConverter converter(){
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setStrictContentTypeMatch(false);
        return messageConverter;
    }
}
