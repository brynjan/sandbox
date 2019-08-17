package no.progconsult.aws.sqs;

import no.embriq.aws.util.sqs.AmazonGlobal;
import no.embriq.aws.util.sqs.AmazonKMS;
import no.embriq.aws.util.sqs.Global;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.Base64;
import com.amazonaws.util.IOUtils;
import org.testng.annotations.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-08-15.
 */
public class S3Client {

    @Test(enabled = false)
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

    @Test(enabled = false)
    public void readS3_MFA() {

        AWSEnvironment environment = AWSEnvironment.SYSTEST;

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());


        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).build();

        s3Client.listBuckets().stream().forEach(bucket -> System.out.println("systest: " + bucket.getName()));
        System.out.println();
    }


    @Test
    public void encryptAndWriteToS3_MFA() {

        AWSEnvironment environment = AWSEnvironment.LAB;

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());

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


        System.out.println("key: " + key);
    }


    @Test
    public void readAndEncryptFromS3_MFA() throws Exception {


        String key = "64cbbadf-9197-4759-9b74-cbdb6bac2502";

        AWSEnvironment environment = AWSEnvironment.LAB;

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).build();

        String cmk = environment.getCmk();
        String bucket = environment.getBucket();

        AmazonS3 s3 = AmazonS3Client.builder().withCredentials(awsCredentialsProvider).withRegion(Regions.EU_WEST_1).build();

        AWSKMS kms = AWSKMSClient.builder().withCredentials(awsCredentialsProvider).withRegion(Regions.EU_WEST_1.getName()).build();


        String decrypt = AmazonKMS.decrypt(kms, read(s3, bucket, key));

        System.out.println();


//        String encryptedPayload = new String(IOUtils.toByteArray(s3.getObject(bucket, key).getObjectContent()), Global.DEFAULT_CHARSET);
//
//        String decryptPayload = AmazonKMS.decrypt(kms, encryptedPayload);
//
//        System.out.println("payload: " + decryptPayload);


//        String encryptedPayload = encrypt(kms, cmk, payload);


//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        byte[] buffer = encryptedPayload.getBytes(Global.DEFAULT_CHARSET);
//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("sqs-queue", queue);
//        objectMetadata.setUserMetadata(map);
//        objectMetadata.setContentLength(buffer.length);

//        PutObjectResult putObjectResult = s3.putObject(bucket, key, new ByteArrayInputStream(buffer), objectMetadata);


        System.out.println("key: " + key);
    }


    @Test
    public void testkms() {
        AWSEnvironment environment = AWSEnvironment.LAB;

        String cmk = environment.getCmk();
        String bucket = environment.getBucket();
        String key = "7f3b0d2f-0c00-4133-b457-62e7a0cb4b77";

        MFACredentialsProvider awsCredentialsProvider = new MFACredentialsProvider(environment.getProfile());


        AmazonS3 s3 = AmazonS3Client.builder().withCredentials(awsCredentialsProvider).withRegion(Regions.EU_WEST_1).build();

        AWSKMS kms = AWSKMSClient.builder().withCredentials(awsCredentialsProvider).withRegion(Regions.EU_WEST_1.getName()).build();


        String encrypted = AmazonKMS.encrypt(kms, cmk, "Dette er en test");


        String decrypt = decrypt(kms, encrypted);

        System.out.println(

        );
    }

    public static String decrypt(AWSKMS kms, String payload) {
        Map<Object, Object> map = JsonUtil.jsonToMap(payload);
        return decrypt(kms, map);
    }

    public static String decrypt(final AWSKMS kms, final Map<Object, Object> map) {
        return decrypt(kms, map.get("key").toString(), map.get("iv").toString(), map.get("cipher").toString());
    }

    public static String decrypt(final AWSKMS kms, final String key, final String iv, final String payload) {
        return new String(decrypt(kms, Base64.decode(key), Base64.decode(iv), Base64.decode(payload)), Global.DEFAULT_CHARSET);
    }

    public static byte[] decrypt(final AWSKMS kms, final byte[] key, final byte[] iv, final byte[] payload) {
        DecryptResult decryptResult = fetchKey(kms, key);

        try {
            Cipher cipher = Cipher.getInstance(AmazonGlobal.DEFAULT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptResult.getPlaintext().array(), "AES"), new IvParameterSpec(iv));
            return Gzip.decompress(cipher.doFinal(payload));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized static DecryptResult fetchKey(final AWSKMS kms, final byte[] key) {
        return kms.decrypt(new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(key)));
    }

    private String read(AmazonS3 s3, final String bucket, final String key) {
        try {
            return new String(IOUtils.toByteArray(s3.getObject(bucket, key).getObjectContent()), Global.DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
