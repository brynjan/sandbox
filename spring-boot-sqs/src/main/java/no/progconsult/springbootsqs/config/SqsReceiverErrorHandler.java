package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.listener.QueueMessageVisibility;
import no.progconsult.springbootsqs.common.SilentRollbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-04-03.
 */

@Component
public class SqsReceiverErrorHandler implements io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler {

    private final SqsBackOff sqsBackOff;

    public SqsReceiverErrorHandler(SqsBackOff sqsBackOff) {
        this.sqsBackOff = sqsBackOff;
    }

    private static final Logger LOG = LoggerFactory.getLogger(SqsReceiverErrorHandler.class);

    @Override
    public void handle(Message message, Throwable exception) {
        QueueMessageVisibility queueMessageVisibility = (QueueMessageVisibility) message.getHeaders().get("Sqs_VisibilityTimeout");
        String receiptHandle = message.getHeaders().get("Sqs_ReceiptHandle").toString();

        String approximateReceiveCount = Optional.ofNullable(message.getHeaders().get("Sqs_Msa_ApproximateReceiveCount")).map(Object::toString).orElse(null);

        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
        queueMessageVisibility.changeToAsync(nextVisibilityTimeoutSeconds);
        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);

        if (Optional.ofNullable(exception.getCause()).map(Throwable::getCause).map(throwable -> throwable.getCause() instanceof SilentRollbackException).orElse(false)) {
            String errorMessage = Optional.ofNullable(exception.getCause().getCause().getCause().getMessage()).orElse("");
            LOG.error("SilentRollbackException received, no further logging required. {}", errorMessage);
        } else {
            LOG.error("Exception encountered while processing message. {}", exception.getMessage());
            LOG.info("Stacktrace", exception);
        }
    }
}
