package no.progconsult.springbootsqs.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs, Environment env) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setWaitTimeOut(env.getRequiredProperty("sqs.wait.timeout", Integer.class));
        return factory;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
}
