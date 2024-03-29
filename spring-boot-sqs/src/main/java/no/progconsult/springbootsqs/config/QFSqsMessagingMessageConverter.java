package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-08.
 */
public class QFSqsMessagingMessageConverter extends SqsMessagingMessageConverter {

    private transient static final Logger LOG = LoggerFactory.getLogger(QFSqsMessagingMessageConverter.class);


    private  final SqsKMSClient sqsKMSClient;

    public QFSqsMessagingMessageConverter(SqsKMSClient sqsKMSClient) {
        this.sqsKMSClient = sqsKMSClient;
    }

    @Override
    protected Object getPayloadToDeserialize(Message message) {
        String breadcrumbId = Optional.ofNullable(message.messageAttributes().get(BREADCRUMB_ID)).map(MessageAttributeValue::stringValue).orElseGet(() -> "ID-" + UUID.randomUUID());
        MDC.put(BREADCRUMB_ID, breadcrumbId);
        Message inflatedMessage = sqsKMSClient.inflate(message);
        return super.getPayloadToDeserialize(inflatedMessage);
    }

    @Override
    protected Message doConvertMessage(Message messageWithHeaders, Object payload) {
        LOG.info("Inne i converter");
        Message message = super.doConvertMessage(messageWithHeaders, payload);
//        return message;

        final String breadcrumbId = Optional.ofNullable(MDC.get(BREADCRUMB_ID)).orElseGet(() -> "ID-" + UUID.randomUUID().toString());
        Message deflated = sqsKMSClient.deflate(message, breadcrumbId);


        return deflated;
    }
}
