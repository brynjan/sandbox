package no.progconsult.springbootsqs.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.core.env.ResourceIdResolver;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.QueueMessageHandler;
import no.embriq.flow.aws.sqs.AmazonKMSSQSAsyncClient;
import no.embriq.flow.aws.sqs.util.AmazonGlobal;
import no.embriq.quant.flow.common.utils.JsonMappers;
import no.progconsult.springbootsqs.common.MdcHandler;
import no.progconsult.springbootsqs.common.Utils;
import no.embriq.quant.flow.pubsub.sdk.Receiver;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.Arrays;
import java.util.Map;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;


@Configuration
public class AWSConfig {
    //Note! Bean name must match with SqsClientConfiguration@ConditionalOnMissingAmazonClient(AmazonSQS.class)
    @Bean
    public AmazonSQSAsync amazonSQS(final Environment env) {
        return new AmazonKMSSQSAsyncClient(AmazonGlobal.DEFAULT_REGION,
                env.getRequiredProperty("sqs.kms.cmk"),
                env.getRequiredProperty("sqs.s3.bucket"));
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs,
                                                                                       MessageConverter messageConverter,
                                                                                       Environment env) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setWaitTimeOut(env.getRequiredProperty("sqs.wait.timeout", Integer.class));
        factory.setVisibilityTimeout(env.getRequiredProperty("sqs.visibility.timeout", Integer.class));
        // This is 10 by default, but if it isnt set the threadpool will be sat to 2 (or 3) which will make the Executor reject
        // taske
        factory.setMaxNumberOfMessages(10);

        //Plugin in MDC
        QueueMessageHandler messageHandler = new QueueMessageHandler(Arrays.asList(messageConverter)) {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                MdcHandler.enableMDC(message);
                super.handleMessage(message);
            }
        };
        factory.setQueueMessageHandler(messageHandler);
        return factory;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync, MessageConverter converter) {
        return new QueueMessagingTemplate(amazonSQSAsync, (ResourceIdResolver) null, converter) {

            //Adding breadCrumbId as sqs header. Key breadcrumbId.
            @Override
            protected Map<String, Object> processHeadersToSend(Map<String, Object> headers) {
                headers = Utils.nullToEmptyMap(headers);
                final String breadcrumbId = MDC.get(BREADCRUMB_ID);
                if (breadcrumbId != null) {
                    headers.put(BREADCRUMB_ID, breadcrumbId);
                }
                return headers;
            }
        };
    }

    @Bean
    AmazonSNS amazonSNS() {
        return AmazonSNSClientBuilder.defaultClient();
    }

    @Bean
    AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.defaultClient();
    }

    @Bean
    Receiver receiver(AmazonS3 amazonS3) {
        return new Receiver(amazonS3);
    }

    @Bean
    MessageConverter converter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setSerializedPayloadClass(String.class);
        messageConverter.setObjectMapper(JsonMappers.getObjectMapper());
        messageConverter.setStrictContentTypeMatch(false);
        return messageConverter;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMappers.getObjectMapper();
    }
}
