package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.listener.QueueMessageVisibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-04-03.
 */

@Component
public class ErrorHandler implements io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler {

    private final SqsBackOff sqsBackOff;

    public ErrorHandler(SqsBackOff sqsBackOff) {
        this.sqsBackOff = sqsBackOff;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);
    @Override
    public void handle(Message message, Throwable t) {
        QueueMessageVisibility queueMessageVisibility = (QueueMessageVisibility) message.getHeaders().get("Sqs_VisibilityTimeout");
        String receiptHandle = message.getHeaders().get("Sqs_ReceiptHandle").toString();

        String approximateReceiveCount = Optional.ofNullable(message.getHeaders().get("Sqs_Msa_ApproximateReceiveCount")).map(Object::toString).orElse(null);

        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
        queueMessageVisibility.changeToAsync(nextVisibilityTimeoutSeconds);

        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);
        LOG.error("Exception encountered while processing message. {}", t.getMessage());
        LOG.info("Stacktrace", t);

    }
}
