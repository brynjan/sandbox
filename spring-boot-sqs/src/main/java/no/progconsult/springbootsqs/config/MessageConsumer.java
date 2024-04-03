package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import io.awspring.cloud.sqs.listener.QueueMessageVisibility;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler;
import no.embriq.quant.flow.typelib.common.QFEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.messaging.Message;
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

    @SqsListener(value = "embriq-volueagent-in-test", acknowledgementMode = SqsListenerAcknowledgementMode.MANUAL)
    public void receive(Acknowledgement acknowledgement, QFEvent message) {
        // Process the received message
        System.out.println("Received message. Size: " + message.getPayload().length());
        System.out.println(StringUtils.abbreviate(message.getPayload(), 100_000));
        springBootSqsRoute.processMessage(message);
//        throw new RuntimeException("feilet");
        acknowledgement.acknowledge();
    }

//    @MessageExceptionHandler(Exception.class)
//    public void exceptionHandler(Exception e) {
//        LOG.info("Failed to deserialize that pollo", e);
//    }
//
//
////    @MessageExceptionHandler(Exception.class)
////    public void handleException(Exception exception, QueueMessageVisibility visibility,
////                                @Header("ReceiptHandle") String receiptHandle,
////                                @Header("ApproximateReceiveCount") String approximateReceiveCount) {
////
////        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
////        visibility.changeToAsync(nextVisibilityTimeoutSeconds);
////
////        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds,
////                receiptHandle);
////        LOG.error("Exception encountered while processing message. {}", exception.getMessage());
////        LOG.info("Stacktrace", exception);
////    }

}

