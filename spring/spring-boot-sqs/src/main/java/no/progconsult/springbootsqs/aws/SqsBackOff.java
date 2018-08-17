package no.progconsult.springbootsqs.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2018-08-17.
 */
@Component
public class SqsBackOff {
    private float backoffFactor;
    private int initialVisibilityTimeoutSeconds;
    private int maxVisibilityTimeoutSeconds;

    @Autowired
    public SqsBackOff(Environment env) {
        backoffFactor = env.getRequiredProperty("sqs.backoffFactor", Float.class);
        initialVisibilityTimeoutSeconds = env.getRequiredProperty("sqs.initialVisibilityTimeoutSeconds", Integer.class);
        maxVisibilityTimeoutSeconds = env.getRequiredProperty("sqs.maxVisibilityTimeoutSeconds", Integer.class);
    }

    public int calculateNextVisibilityTimeoutSeconds(String approximateReceiveCount){
        int receiveCount = Optional.ofNullable(approximateReceiveCount).map(count -> Integer.parseInt(count)).orElse(1);
        // We should not try to calculate factor^veryLargeNumber, BigDecimal throws arithmetic exception if above 999999999
        receiveCount = Math.min(receiveCount, 9999);
        int retryNumber = Math.max(receiveCount - 1, 0);
        BigDecimal nextVisibilityTimeoutSeconds = new BigDecimal(backoffFactor).pow(retryNumber).multiply(new BigDecimal(initialVisibilityTimeoutSeconds));
        return nextVisibilityTimeoutSeconds.min(new BigDecimal(maxVisibilityTimeoutSeconds)).intValue();
    }
}
