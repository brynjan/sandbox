package no.progconsult.springbootsqs.aws;

import no.progconsult.springbootsqs.model.Report;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.QueueMessageVisibility;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-15.
 */
@Component
public class MessageConsumer {

    private transient static final Logger LOG = getLogger(MessageConsumer.class);


    private final SqsBackOff sqsBackOff;

    @Autowired
    public MessageConsumer(SqsBackOff sqsBackOff, Environment env) {
        this.sqsBackOff = sqsBackOff;
    }


    @SqsListener(value = "${sqs.in}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveHourReport(Report report) {
        LOG.info("Message processed. MessageId: {}", report.messageId);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleException(QueueMessageVisibility visibility, @Header("ReceiptHandle") String receiptHandle,
                                @Header("ApproximateReceiveCount") String approximateReceiveCount) {
        int nextVisibilityTimeoutSeconds = sqsBackOff.calculateNextVisibilityTimeoutSeconds(approximateReceiveCount);
        visibility.extend(nextVisibilityTimeoutSeconds);
        LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);
    }
}
