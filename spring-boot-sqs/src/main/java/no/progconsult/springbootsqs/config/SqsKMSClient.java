package no.progconsult.springbootsqs.config;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.util.IOUtils;
import no.embriq.flow.aws.sqs.util.JsonUtil;
import no.embriq.flow.aws.sqs.v2.AmazonKMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StreamUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-07.
 */


public class SqsKMSClient {

    private transient static final Logger LOG = LoggerFactory.getLogger(SqsKMSClient.class);

    private static final String AWS_SQS_LARGE_PAYLOAD = "AWS_SQS_LARGE_PAYLOAD";
    public static final String ATTRIBUTE_KEY_KMS_CMK_ID = "_KMS_CMK_ID";
    public static final String ATTRIBUTE_KEY_S3_BUCKET = "_S3_BUCKET";
    private static final String IDENTIFIER = "identifier";
    private static final String BUCKET = "bucket";
    private static final String REGION = "region";
    private static final int MAX_MESSAGE_SIZE = (1024 * 64 * 4) - (1024 * 16);

    private final AwsCredentialsProvider awsCredentialsProvider;

    private final Region region;
    private final KmsClient kmsClient;
    private final S3Client s3Client;
    private final String kmsCmkId;
    private final String s3Bucket;


    public SqsKMSClient(AwsCredentialsProvider awsCredentialsProvider, Region region, String kmsCmkId, String s3Bucket) {
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.region = region;
        this.kmsClient = KmsClient.builder().credentialsProvider(awsCredentialsProvider).region(region).build();
        s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(region).build();
        this.kmsCmkId = kmsCmkId;
        this.s3Bucket = s3Bucket;
    }


    public Message inflate(Message message) {

        if (message.messageAttributes().get(AWS_SQS_LARGE_PAYLOAD) != null) {
            Map<Object, Object> map = JsonUtil.jsonToMap(message.messageAttributes().get(AWS_SQS_LARGE_PAYLOAD).stringValue());

            final String body;
            if (map.containsKey(IDENTIFIER)) {
                body = AmazonKMS.decrypt(kmsClient, read(map.get(BUCKET).toString(), map.get(IDENTIFIER).toString()));
            } else {
                body = AmazonKMS.decrypt(kmsClient, message.body());
            }

            Map<String, MessageAttributeValue> cleanedMessageAttributes = cleanedMessageAttributes(message);
            message = message.copy(builder -> builder.body(body).messageAttributes(cleanedMessageAttributes));
        } else {
            Map<Object, Object> map = JsonUtil.jsonToMap(message.body());
            Object key = map.get("key");
            Object iv = map.get("iv");
            Object cipher = map.get("cipher");
            // Message is a "normal" sqs message and should not be inflated
            if (key == null || iv == null || cipher == null) {
                return message;
            }
            message = message.copy(builder -> builder.body(AmazonKMS.decrypt(kmsClient, key.toString(), iv.toString(), cipher.toString())));
        }
        return message;
    }

    public Message deflate(Message message, final String breadcrumbId) {


        String kmsCmkIdToUse = kmsCmkId;
        String s3BucketToUse = s3Bucket;

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();

        messageAttributes.put(BREADCRUMB_ID, MessageAttributeValue.builder().dataType("String").stringValue(breadcrumbId).build());

        for (Map.Entry<String, MessageAttributeValue> entry : message.messageAttributes().entrySet()) {

            if (entry.getKey().equals(ATTRIBUTE_KEY_KMS_CMK_ID)) {
                kmsCmkIdToUse = entry.getValue().stringValue();
            } else if (entry.getKey().equals(ATTRIBUTE_KEY_S3_BUCKET)) {
                s3BucketToUse = entry.getValue().stringValue();
            } else {
                messageAttributes.put(entry.getKey(), entry.getValue());
            }
        }



//
//        String kmsCmkIdToUse = kmsCmkId;
//        MessageAttributeValue kmsCmkMessageAttributeValue = message.messageAttributes().get(ATTRIBUTE_KEY_KMS_CMK_ID);
////        com.amazonaws.services.sqs.model.MessageAttributeValue kmsCmkIdAttribute = sendMessageRequest.getMessageAttributes().get(ATTRIBUTE_KEY_KMS_CMK_ID);
//        if (kmsCmkMessageAttributeValue != null) {
//            kmsCmkIdToUse = kmsCmkMessageAttributeValue.stringValue();
//            sendMessageRequest.getMessageAttributes().remove(ATTRIBUTE_KEY_KMS_CMK_ID);
//        }
//
//        String s3BucketToUse = s3Bucket;
//        com.amazonaws.services.sqs.model.MessageAttributeValue s3BucketAttribute = sendMessageRequest.getMessageAttributes().get(ATTRIBUTE_KEY_S3_BUCKET);
//        if (s3BucketAttribute != null) {
//            s3BucketToUse = s3BucketAttribute.getStringValue();
//            sendMessageRequest.getMessageAttributes().remove(ATTRIBUTE_KEY_S3_BUCKET);
//        }

        String payload = AmazonKMS.encrypt(kmsClient, kmsCmkIdToUse, message.body());
        Map<Object, Object> map = new HashMap<Object, Object>();

        map.put(REGION, region.id());


        if (payload.length() >= MAX_MESSAGE_SIZE) {
            String uuid = UUID.randomUUID().toString();

            map.put(BUCKET, s3BucketToUse);
            map.put(IDENTIFIER, uuid);

            MessageAttributeValue string = MessageAttributeValue.builder().dataType("String").stringValue(JsonUtil.mapToJson(map)).build();

            messageAttributes.put(AWS_SQS_LARGE_PAYLOAD, MessageAttributeValue.builder().dataType("String").stringValue(JsonUtil.mapToJson(map)).build());
//            sendMessageRequest.addMessageAttributesEntry(AWS_SQS_LARGE_PAYLOAD, new com.amazonaws.services.sqs.model.MessageAttributeValue().withDataType("String").withStringValue(JsonUtil.mapToJson(map)));
//            sendMessageRequest.setMessageBody("{}");

            write(s3BucketToUse, uuid, payload);
            message = message.copy(builder -> builder.body("{}").messageAttributes(messageAttributes));
            System.out.println();
        } else {
            messageAttributes.put(AWS_SQS_LARGE_PAYLOAD, MessageAttributeValue.builder().dataType("String").stringValue(JsonUtil.mapToJson(map)).build());
            message = message.copy(builder -> builder.body(payload).messageAttributes(messageAttributes));
        }
        return message;
    }


    private String read(final String bucket, final String key) {
        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
            return IOUtils.toString(response);
//            return StreamUtils.copyToString(response, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void write(final String bucket, final String key, final String payload) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] buffer = payload.getBytes(StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<String, String>();

        objectMetadata.setUserMetadata(map);
        objectMetadata.setContentLength(buffer.length);

        s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).metadata(map).contentLength((long) buffer.length).build(), RequestBody.fromBytes(buffer));
    }

    private Map<String, MessageAttributeValue> cleanedMessageAttributes(Message message) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();

        for (Map.Entry<String, MessageAttributeValue> entry : message.messageAttributes().entrySet()) {
            if (!entry.getKey().equals(AWS_SQS_LARGE_PAYLOAD)) {
                messageAttributes.put(entry.getKey(), entry.getValue());
            }
        }

        return messageAttributes;
    }
}
