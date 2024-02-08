package no.progconsult.springbootsqs.config;

import no.embriq.flow.aws.sqs.util.JsonUtil;
import no.embriq.flow.aws.sqs.v2.AmazonKMS;
import org.springframework.util.StreamUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-07.
 */


public class SqsKMSClient {


    private static final String AWS_SQS_LARGE_PAYLOAD = "AWS_SQS_LARGE_PAYLOAD";
    private static final String IDENTIFIER = "identifier";
    private static final String BUCKET = "bucket";

    private final AwsCredentialsProvider awsCredentialsProvider;
    private final KmsClient kmsClient;
    private final S3Client s3Client;

    public SqsKMSClient(AwsCredentialsProvider awsCredentialsProvider, Region region) {
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.kmsClient = KmsClient.builder().credentialsProvider(awsCredentialsProvider).region(region).build();
        s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(region).build();
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


    private String read(final String bucket, final String key) {
        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
            return StreamUtils.copyToString(response, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
