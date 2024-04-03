package no.progconsult.springbootsqs.common;

import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import no.embriq.flow.aws.sqs.v2.SqsExtendedClient;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-08.
 */

public class ExtendedSqsMessagingMessageConverter extends SqsMessagingMessageConverter {

    private final SqsExtendedClient sqsExtendedClient;

    public ExtendedSqsMessagingMessageConverter(SqsExtendedClient sqsExtendedClient) {
        this.sqsExtendedClient = sqsExtendedClient;
    }

    @Override
    protected Object getPayloadToDeserialize(Message message) {
        String breadcrumbId = Optional.ofNullable(message.messageAttributes().get(BREADCRUMB_ID)).map(MessageAttributeValue::stringValue).orElseGet(() -> "ID-" + UUID.randomUUID());
        MDC.put(BREADCRUMB_ID, breadcrumbId);

        Message inflatedMessage = sqsExtendedClient.inflate(message);

        return super.getPayloadToDeserialize(inflatedMessage);
    }

    @Override
    protected Message doConvertMessage(Message messageWithHeaders, Object payload) {

        Message message = super.doConvertMessage(messageWithHeaders, payload);

        Message deflated = sqsExtendedClient.deflate(message);

        Message withBreadcrumb = addBreadCrumb(deflated);

        return withBreadcrumb;
    }

    private static Message addBreadCrumb(Message deflated) {
        final String breadcrumbId = Optional.ofNullable(MDC.get(BREADCRUMB_ID)).orElseGet(() -> "ID-" + UUID.randomUUID());
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        for (Map.Entry<String, MessageAttributeValue> entry : deflated.messageAttributes().entrySet()) {
            messageAttributes.put(entry.getKey(), entry.getValue());
        }
        messageAttributes.put(BREADCRUMB_ID, MessageAttributeValue.builder().dataType("String").stringValue(breadcrumbId).build());
        Message withBreadcrumb = deflated.copy(builder -> builder.messageAttributes(messageAttributes));
        return withBreadcrumb;
    }
}
