package no.progconsult.springbootsqs.config;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.util.IOUtils;
import no.embriq.flow.aws.sqs.util.Global;
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


    public void inflate(Message message) {
        if (message.getMessageAttributes().get(AWS_SQS_LARGE_PAYLOAD) != null) {
            Map<Object, Object> map = JsonUtil.jsonToMap(message.getMessageAttributes().get(AWS_SQS_LARGE_PAYLOAD).getStringValue());

            if (map.containsKey(IDENTIFIER)) {
                message.setBody(AmazonKMS.decrypt(kmsClient, read(map.get(BUCKET).toString(), map.get(IDENTIFIER).toString())));
            } else {
                message.setBody(AmazonKMS.decrypt(kmsClient, message.getBody()));
            }

            cleanMessageAttributes(message);
        } else {
            Map<Object, Object> map = JsonUtil.jsonToMap(message.getBody());
            Object key = map.get("key");
            Object iv = map.get("iv");
            Object cipher = map.get("cipher");
            // Message is a "normal" sqs message and should not be inflated
            if (key == null || iv == null || cipher == null) {
                return;
            }
            message.setBody(AmazonKMS.decrypt(kmsClient, key.toString(), iv.toString(), cipher.toString()));
        }
    }


    private String read(final String bucket, final String key) {
        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
            return StreamUtils.copyToString(response, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanMessageAttributes(Message message) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();

        for (Map.Entry<String, MessageAttributeValue> entry : message.getMessageAttributes().entrySet()) {
            if (!entry.getKey().equals(AWS_SQS_LARGE_PAYLOAD)) {
                messageAttributes.put(entry.getKey(), entry.getValue());
            }
        }
        message.clearMessageAttributesEntries();
        message.setMessageAttributes(messageAttributes);
    }



}
