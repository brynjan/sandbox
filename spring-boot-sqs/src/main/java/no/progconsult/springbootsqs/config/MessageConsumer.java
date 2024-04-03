package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.QueueMessageVisibility;
import no.embriq.quant.flow.typelib.common.QFEvent;
import org.apache.commons.lang3.StringUtils;
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

    @SqsListener(value = "embriq-volueagent-in-test")
    public void receive(QFEvent message) {
        // Process the received message
        System.out.println("Received message. Size: " + message.getPayload().length());
        System.out.println(StringUtils.abbreviate(message.getPayload(), 100_000));
    }

    @MessageExceptionHandler(Exception.class)
    public void handleException(Exception exception, QueueMessageVisibility visibility,
                                @Header("ReceiptHandle") String receiptHandle,
                                @Header("ApproximateReceiveCount") String approximateReceiveCount) {

        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
        visibility.changeToAsync(nextVisibilityTimeoutSeconds);

        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds,
                receiptHandle);
        LOG.error("Exception encountered while processing message. {}", exception.getMessage());
        LOG.info("Stacktrace", exception);
    }

}

