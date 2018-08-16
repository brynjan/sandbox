package no.progconsult.springbootsqs.aws;

import no.progconsult.springbootsqs.common.Constants;

import org.slf4j.MDC;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-15.
 */
public class SqsHandler {
    public static int calculateNextVisibilityTimeoutSeconds(int approximateReceiveCount, float backoffFactor, int maxVisibilityTimeoutSeconds, int initialVisibilityTimeoutSeconds) {
        // We should not try to calculate factor^veryLargeNumber, BigDecimal throws arithmetic exception if above 999999999
        approximateReceiveCount = Math.min(approximateReceiveCount, 9999);
        int retryNumber = Math.max(approximateReceiveCount - 1, 0);

        BigDecimal nextVisibilityTimeoutSeconds = new BigDecimal(backoffFactor).pow(retryNumber).multiply(new BigDecimal(initialVisibilityTimeoutSeconds));

        return nextVisibilityTimeoutSeconds.min(new BigDecimal(maxVisibilityTimeoutSeconds)).intValue();
    }

    public static String setupBreadcrumb(Map<String, Object> allHeaders) {
        String breadcrumbId = Optional.ofNullable((String) allHeaders.get(Constants.BREADCRUMB_ID)).orElse((String) allHeaders.get("MessageId"));
        MDC.put(Constants.BREADCRUMB_ID, breadcrumbId);
        return breadcrumbId;
    }
}
