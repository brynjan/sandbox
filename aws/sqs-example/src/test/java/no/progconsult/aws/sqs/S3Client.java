package no.progconsult.aws.sqs;

import no.embriq.aws.util.sqs.AmazonGlobal;
import no.embriq.aws.util.sqs.AmazonKMSSQSClient;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-08-15.
 */
public class S3Client {

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
    public void embriq() {


        AWSEnvironment environment = AWSEnvironment.SYSTEST;

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());



        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).build();

        s3Client.listBuckets().stream().forEach(bucket -> System.out.println("systest: " + bucket.getName()));
        System.out.println();
    }

}
