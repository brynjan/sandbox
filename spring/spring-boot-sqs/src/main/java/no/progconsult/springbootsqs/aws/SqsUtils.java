package no.progconsult.springbootsqs.aws;

import no.progconsult.springbootsqs.common.Constants;

import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-15.
 */
public class SqsUtils {

    public static void enableMDC(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String breadcrumbId = Optional.ofNullable((String) headers.get(Constants.BREADCRUMB_ID)).orElse((String) headers.get("MessageId"));
        MDC.put(Constants.BREADCRUMB_ID, breadcrumbId);
    }
}
