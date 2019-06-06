package no.progconsult.aws.lambda;

import no.embriq.aws.util.sqs.AmazonGlobal;
import no.embriq.aws.util.sqs.AmazonKMSSQSClient;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import org.slf4j.Logger;

import java.math.BigDecimal;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-05-28.
 */
public class SqsLambda implements RequestHandler<SQSEvent, Void> {

    private transient static final Logger LOG = getLogger(SqsLambda.class);

    private String kms = "alias/embriq-flow";
    private String bucket = "embriq-flow.eu-west-1.965281606204";
    private AmazonKMSSQSClient amazonKMSSQSClient;
    private String queueUrl;
    private float backoffFactor = 2;
    private int initialVisibilityTimeoutSeconds = 30;
    private int maxVisibilityTimeoutSeconds = 120;


    public SqsLambda() {
        amazonKMSSQSClient = new AmazonKMSSQSClient(AmazonGlobal.DEFAULT_REGION, kms, bucket);
        queueUrl = amazonKMSSQSClient.getQueueUrl("lamba-test-bno").getQueueUrl();
        try {
            System.out.println("starting sleep");
            Thread.sleep(4000);
            System.out.println("ending sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        int i = 1;
        LOG.info("number of messages: {}", sqsEvent.getRecords().size());

        System.out.println("context: " + context.getClass().getName());
        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {

            System.out.println("body: " + msg.getBody());
//            i++;
//            amazonKMSSQSClient.inflate(msg);
//
//            LOG.info("process message body: " + msg.getBody());
//
//            if (i > 2) {
//                LOG.info("failed message body: " + msg.getBody());
//                extendMessageVisibilityTimeout(msg);
//                throw new RuntimeException("feilet");
//            } else {
//                amazonKMSSQSClient.deleteMessage(queueUrl, msg.getReceiptHandle());
//                LOG.info("Deleted message: {}", msg.getBody());
//            }
        }
        return null;
    }

    public void extendMessageVisibilityTimeout(SQSEvent.SQSMessage message) {
        try {
            int approximateReceiveCount = getApproximateReceiveCount(message);
            int nextVisibilityTimeoutSeconds = getNextVisibilityTimeoutSeconds(approximateReceiveCount);
            String receiptHandle = message.getReceiptHandle();

            LOG.info("ChangeMessageVisibility nextVisibilityTimeout: {} seconds, receiptHandle: {}", nextVisibilityTimeoutSeconds, receiptHandle);
            amazonKMSSQSClient.changeMessageVisibility(new ChangeMessageVisibilityRequest()
                    .withQueueUrl(queueUrl)
                    .withReceiptHandle(receiptHandle)
                    .withVisibilityTimeout(nextVisibilityTimeoutSeconds));
        } catch (AmazonSQSException x) {
            if ("InvalidParameterValue".equals(x.getErrorCode())) {
                LOG.info("Message visibility timeout is already expired, unable to extend it.");
            } else {
                LOG.error("Failed to change message visibility: {}", x);
            }
        } catch (Exception x) {
            LOG.error("Failed to change message visibility: {}", x);
        }
    }

    public int getApproximateReceiveCount(SQSEvent.SQSMessage message) {
        String count = message.getAttributes().get("ApproximateReceiveCount");
        return count == null ? 1 : Integer.parseInt(count);
    }

    private int getNextVisibilityTimeoutSeconds(int approximateReceiveCount) {

        // We should not try to calculate factor^veryLargeNumber, BigDecimal throws arithmetic exception if above 999999999
        approximateReceiveCount = Math.min(approximateReceiveCount, 9999);
        int retryNumber = Math.max(approximateReceiveCount - 1, 0);

        BigDecimal nextVisibilityTimeoutSeconds = new BigDecimal(backoffFactor).pow(retryNumber).multiply(new BigDecimal(initialVisibilityTimeoutSeconds));

        return nextVisibilityTimeoutSeconds.min(new BigDecimal(maxVisibilityTimeoutSeconds)).intValue();
    }
}
