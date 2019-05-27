package no.progconsult.aws.sqs;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-05-24.
 */
public class SqsClient {

    @Test
    public void testName() {

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                                 .withRegion(Regions.EU_WEST_1)
                                                 .build();


        s3Client.listBuckets().stream().forEach(bucket -> System.out.println("bucket1: " + bucket.getName()));
        System.out.println();



        final ProfileCredentialsProvider credentialsProvider
                = new ProfileCredentialsProvider("bno");
        s3Client = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).build();

        s3Client.listBuckets().stream().forEach(bucket -> System.out.println("bucket2: " + bucket.getName()));
        System.out.println();

    }

    @Test
    public void send() throws Exception{

        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
                                              .withRegion(Regions.EU_WEST_1)
                                              .withCredentials(new ProfileCredentialsProvider("bno"))
                                              .build();


        System.out.println("Creating a new SQS queue called MyQueue.\n");
        final CreateQueueRequest createQueueRequest =
                new CreateQueueRequest("test_bno");
        final String myQueueUrl = amazonSQS.createQueue(createQueueRequest).getQueueUrl();

        System.out.println("Listing all queues in your account.\n");
        for (final String queueUrl : amazonSQS.listQueues().getQueueUrls()) {
            System.out.println("  QueueUrl: " + queueUrl);
        }
        System.out.println();
    }
}
