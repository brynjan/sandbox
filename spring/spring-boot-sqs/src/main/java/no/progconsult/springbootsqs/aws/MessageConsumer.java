package no.progconsult.springbootsqs.aws;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageVisibility;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-15.
 */
@Component
public class MessageConsumer {

    private transient static final Logger LOG = getLogger(MessageConsumer.class);


    private float backoffFactor;
    private int initialVisibilityTimeoutSeconds;
    private int maxVisibilityTimeoutSeconds;
    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public MessageConsumer(QueueMessagingTemplate queueMessagingTemplate, Environment env) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        backoffFactor = env.getRequiredProperty("sqs.backoffFactor", Float.class);
        initialVisibilityTimeoutSeconds = env.getRequiredProperty("sqs.initialVisibilityTimeoutSeconds", Integer.class);
        maxVisibilityTimeoutSeconds = env.getRequiredProperty("sqs.maxVisibilityTimeoutSeconds", Integer.class);
    }


    @SqsListener(value = "${sqs.in}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveHourReport(@Headers Map<String, Object> allHeaders, String message) {
        SqsHandler.setupBreadcrumb(allHeaders);
        LOG.info("Message processed. Message: {}", message);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleException(QueueMessageVisibility visibility, @Header("ReceiptHandle") String receiptHandle,
                                @Header("ApproximateReceiveCount") String approximateReceiveCount) {
        Integer receiveCount = Optional.ofNullable(approximateReceiveCount).map(count -> Integer.parseInt(count)).orElse(1);
        int nextVisibilityTimeoutSeconds = SqsHandler
                .calculateNextVisibilityTimeoutSeconds(receiveCount, backoffFactor, maxVisibilityTimeoutSeconds, initialVisibilityTimeoutSeconds);
        visibility.extend(nextVisibilityTimeoutSeconds);
        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);
    }
}
