package no.progconsult.springbootsqs.config;

import io.awspring.cloud.messaging.listener.Acknowledgment;
import io.awspring.cloud.messaging.listener.QueueMessageVisibility;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import no.progconsult.springbootsqs.common.SilentRollbackException;
import no.embriq.quant.flow.pubsub.sdk.FlowTopicEvent;
import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MessageConsumer {
    private static final Logger LOG = getLogger(MessageConsumer.class);

    private final SpringBootSqsRoute springBootSqsRoute;
    private final SqsBackOff sqsBackOff;


    public MessageConsumer(SpringBootSqsRoute springBootSqsRoute, SqsBackOff sqsBackOff) {
        this.springBootSqsRoute = springBootSqsRoute;
        this.sqsBackOff = sqsBackOff;
    }

    @SqsListener(value = "${sqs.quant.meterreading.deliveries.subcription.in}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void receiveEventFromHes(Acknowledgment acknowledgment, FlowTopicEvent flowTopicEvent) {
        acknowledgment.acknowledge();
    }


    @MessageExceptionHandler(Exception.class)
    public void handleException(Exception exception, QueueMessageVisibility visibility,
                                @Header("ReceiptHandle") String receiptHandle,
                                @Header("ApproximateReceiveCount") String approximateReceiveCount) {
        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
        visibility.extend(nextVisibilityTimeoutSeconds);
        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);

        if (exception instanceof SilentRollbackException) {
            LOG.info("SilentRollbackException received, no further logging required. {}",
                    Optional.ofNullable(exception.getMessage()).map(message -> String.format("ErrorMessage: %s", message)).orElse(""));
        } else {
            LOG.error("Exception encountered while processing message. {}", exception.getMessage());
            LOG.info("Stacktrace", exception);
        }
    }
}

