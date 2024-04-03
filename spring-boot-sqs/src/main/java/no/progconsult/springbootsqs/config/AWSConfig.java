package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.QueueMessageVisibility;
import io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import no.embriq.flow.aws.auth.AmazonCredentialsProvider;
import no.embriq.flow.aws.sqs.v2.SqsExtendedClient;
import no.embriq.quant.flow.common.utils.JsonMappers;
import no.progconsult.springbootsqs.common.BreadcrumbMessageInterceptor;
import no.progconsult.springbootsqs.common.ExtendedSqsMessagingMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;
import java.util.Optional;


@Configuration
public class AWSConfig {

    private transient static final Logger LOG = LoggerFactory.getLogger(AWSConfig.class);

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


    @Bean
    SqsAsyncClient sqsAsyncClient(AwsCredentialsProvider awsCredentialsProvider, Environment env) {
        SqsAsyncClient sqsAsyncClient = SqsAsyncClient
                .builder()
                .region(Region.of(env.getRequiredProperty("cognito.credentials.provider.awsRegion")))
                .credentialsProvider(awsCredentialsProvider)
                .build();
        return sqsAsyncClient;
        // add more Options
    }

    @Bean
    SqsExtendedClient sqsExtendedClient(AwsCredentialsProvider awsCredentialsProvider, Environment env) {
        return new SqsExtendedClient(awsCredentialsProvider, Region.of("eu-west-1"), env.getRequiredProperty("sqs.kms.cmk"), env.getRequiredProperty("sqs.s3.bucket"));
    }

    @Bean
    SqsMessagingMessageConverter sqsMessagingMessageConverter(SqsExtendedClient sqsExtendedClient, MessageConverter messageConverter) {
        ExtendedSqsMessagingMessageConverter converter = new ExtendedSqsMessagingMessageConverter(sqsExtendedClient);
        converter.setPayloadMessageConverter(messageConverter);
        return converter;
    }

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient, SqsMessagingMessageConverter sqsMessagingMessageConverter, @Value("${sqs.poll.timeout}") Duration pollTimeout, no.progconsult.springbootsqs.config.ErrorHandler errorHandler) {
        return SqsMessageListenerContainerFactory
                .builder()
                .configure(options -> options
                        .maxMessagesPerPoll(10)
                        .pollTimeout(pollTimeout)
                        .messageConverter(sqsMessagingMessageConverter))
                .sqsAsyncClient(sqsAsyncClient)
                .messageInterceptor(new BreadcrumbMessageInterceptor())
                .errorHandler(errorHandler)
                .build();
    }

    @Bean
    SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient, SqsMessagingMessageConverter sqsMessagingMessageConverter) {
        SqsTemplate sqsTemplate = SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .messageConverter(sqsMessagingMessageConverter)
                .build();
        return sqsTemplate;
    }

    @Bean
    MessageConverter converter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setSerializedPayloadClass(String.class);
        messageConverter.setObjectMapper(JsonMappers.getObjectMapper());
        messageConverter.setStrictContentTypeMatch(false);
        return messageConverter;
    }
}
