package no.progconsult.aws.lambda;

import no.embriq.aws.util.sqs.AmazonGlobal;
import no.embriq.aws.util.sqs.AmazonKMSSQSClient;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-05-28.
 */
public class SqsLambda implements RequestHandler<SQSEvent, Void> {


    private String kms = "alias/embriq-flow";
    private String bucket = "embriq-flow.eu-west-1.965281606204";
    AmazonKMSSQSClient amazonKMSSQSClient;

    public SqsLambda() {
        amazonKMSSQSClient = new AmazonKMSSQSClient(AmazonGlobal.DEFAULT_REGION, kms, bucket);
    }

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        int i = 1;
        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {

            amazonKMSSQSClient.inflate(msg);

            System.out.println("message number: " + i++ + "body: " + msg.getBody());
        }
        return null;
    }
}
