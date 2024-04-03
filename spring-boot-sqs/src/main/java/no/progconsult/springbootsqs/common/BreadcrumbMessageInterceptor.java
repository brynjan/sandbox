package no.progconsult.springbootsqs.common;

import io.awspring.cloud.sqs.listener.interceptor.MessageInterceptor;
import org.slf4j.MDC;
import org.springframework.messaging.Message;

import java.util.Optional;
import java.util.UUID;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-07.
 */
public class BreadcrumbMessageInterceptor implements MessageInterceptor<Object> {

    @Override
    public Message<Object> intercept(Message<Object> message) {
        String breadcrumbId = Optional.ofNullable(message.getHeaders().get(BREADCRUMB_ID, String.class)).orElseGet(() -> "ID-" + UUID.randomUUID());
        MDC.put(BREADCRUMB_ID, breadcrumbId);
        return MessageInterceptor.super.intercept(message);
    }
}
