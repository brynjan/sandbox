package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.annotation.SqsListener;
import no.embriq.quant.flow.typelib.common.QFEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MessageConsumerOld {
    private static final Logger LOG = getLogger(MessageConsumerOld.class);

    private final SpringBootSqsRoute springBootSqsRoute;
    private final SqsBackOff sqsBackOff;


    public MessageConsumerOld(SpringBootSqsRoute springBootSqsRoute, SqsBackOff sqsBackOff) {
        this.springBootSqsRoute = springBootSqsRoute;
        this.sqsBackOff = sqsBackOff;
    }

    @SqsListener(value = "embriq-volueagent-in-test")
    public void receiveVolueMore(QFEvent message) {
        // Process the received message
        System.out.println("Received message. Size: " + message.getPayload().length());
        System.out.println(StringUtils.abbreviate(message.getPayload(), 100_000));
    }


//    @SqsListener(value = "${sqs.quant.meterreading.deliveries.subcription.in}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
//    public void receiveEventFromHes(Acknowledgment acknowledgment, FlowTopicEvent flowTopicEvent) {
//        acknowledgment.acknowledge();
//    }


//    @MessageExceptionHandler(Exception.class)
//    public void handleException(Exception exception, QueueMessageVisibility visibility,
//                                @Header("ReceiptHandle") String receiptHandle,
//                                @Header("ApproximateReceiveCount") String approximateReceiveCount) {
//        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
//        visibility.extend(nextVisibilityTimeoutSeconds);
//        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);
//
//        if (exception instanceof SilentRollbackException) {
//            LOG.info("SilentRollbackException received, no further logging required. {}",
//                    Optional.ofNullable(exception.getMessage()).map(message -> String.format("ErrorMessage: %s", message)).orElse(""));
//        } else {
//            LOG.error("Exception encountered while processing message. {}", exception.getMessage());
//            LOG.info("Stacktrace", exception);
//        }
//    }
}

