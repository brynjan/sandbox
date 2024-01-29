package no.progconsult.springbootsqs.common;

import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Optional;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;

public class MdcHandler {

    public static void enableMDC(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String breadcrumbId = Optional.ofNullable((String) headers.get(BREADCRUMB_ID)).orElse((String) headers.get("MessageId"));
        MDC.put(BREADCRUMB_ID, breadcrumbId);
    }
}
