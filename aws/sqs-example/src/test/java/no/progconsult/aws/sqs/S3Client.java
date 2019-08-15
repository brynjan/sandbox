package no.progconsult.aws.sqs;

import no.embriq.aws.util.sqs.AmazonGlobal;
import no.embriq.aws.util.sqs.AmazonKMS;
import no.embriq.aws.util.sqs.AmazonKMSSQSClient;
import no.embriq.aws.util.sqs.Global;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.Base64;
import org.testng.annotations.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static no.embriq.aws.util.sqs.AmazonKMS.generateDataKey;

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
    public void readS3_MFA() {

        AWSEnvironment environment = AWSEnvironment.SYSTEST;

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());


        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).build();

        s3Client.listBuckets().stream().forEach(bucket -> System.out.println("systest: " + bucket.getName()));
        System.out.println();
    }


    @Test
    public void encryptAndWriteToS3_MFA() {

        AWSEnvironment environment = AWSEnvironment.SYSTEST;

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).build();


        String cmk = environment.getCmk();
        String bucket = environment.getBucket();

        String payload = "Dette er filen som skal lagres i S3";


        AWSKMS kms = AWSKMSClient.builder().withCredentials(awsCredentialsProvider).withRegion(Regions.EU_WEST_1.getName()).build();

        String encryptedPayload = encrypt(kms, cmk, payload);

        String key = UUID.randomUUID().toString();


        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] buffer = encryptedPayload.getBytes(Global.DEFAULT_CHARSET);
//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("sqs-queue", queue);
//        objectMetadata.setUserMetadata(map);
        objectMetadata.setContentLength(buffer.length);

        AmazonS3 s3 = AmazonS3Client.builder().withCredentials(awsCredentialsProvider).withRegion(Regions.EU_WEST_1).build();
        PutObjectResult putObjectResult = s3.putObject(bucket, key, new ByteArrayInputStream(buffer), objectMetadata);



        System.out.println();


    }

    public static String encrypt(final AWSKMS kms, final String kmsCmkId, final String payload) {
        return encrypt(generateDataKey(kms, kmsCmkId), payload);
    }


    public static String encrypt(final GenerateDataKeyResult dataKeyResult, final String payload) {
        return encrypt(dataKeyResult.getKeyId(), dataKeyResult.getPlaintext().array(), dataKeyResult.getCiphertextBlob().array(), payload.getBytes(Global.DEFAULT_CHARSET));
    }

    public static GenerateDataKeyResult generateDataKey(AWSKMS kms, String kmsCmkId) {
        return kms.generateDataKey((new GenerateDataKeyRequest()).withKeyId(kmsCmkId).withKeySpec(DataKeySpec.AES_256));
    }


    public static String encrypt(final String key, final byte[] plaintext, final byte[] ciphertext, final byte[] payload) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("algorithm", AmazonGlobal.DEFAULT_ALGORITHM);
        map.put("encoding", AmazonGlobal.DEFAULT_CONTENT_ENCODING);
        map.put("length", payload.length);
        map.put("cmk", key);

        try {
            byte[] buffer = Gzip.compress(payload);
            Cipher cipher = Cipher.getInstance(AmazonGlobal.DEFAULT_ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(plaintext, "AES"));
            map.put("cipher", Base64.encodeAsString(cipher.doFinal(buffer)));
            map.put("iv", Base64.encodeAsString(cipher.getIV()));
            map.put("key", Base64.encodeAsString(ciphertext));

            return JsonUtil.mapToJson(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
