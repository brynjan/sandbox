package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.model.Message;

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
        Message inflatedMessage = sqsKMSClient.inflate(message);
        return super.getPayloadToDeserialize(inflatedMessage);
    }

    @Override
    protected Message doConvertMessage(Message messageWithHeaders, Object payload) {
        LOG.info("Inne i converter");
        Message message = super.doConvertMessage(messageWithHeaders, payload);
//        return message;
        Message deflated = sqsKMSClient.deflate(message);
        return deflated;
    }
}
