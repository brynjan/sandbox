package no.progconsult.springbootsqs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class SqsBackOff {
    private final float backoffFactor;
    private final int initialVisibilityTimeoutSeconds;
    private final int maxVisibilityTimeoutSeconds;

    @Autowired
    public SqsBackOff(Environment env) {
        backoffFactor = env.getRequiredProperty("sqs.backoffFactor", Float.class);
        initialVisibilityTimeoutSeconds = env.getRequiredProperty("sqs.initialVisibilityTimeoutSeconds", Integer.class);
        maxVisibilityTimeoutSeconds = env.getRequiredProperty("sqs.maxVisibilityTimeoutSeconds", Integer.class);
    }

    public int calculateNextVisibilityTimeoutSeconds(String approximateReceiveCount) {
        int receiveCount = Optional.ofNullable(approximateReceiveCount).map(Integer::parseInt).orElse(1);
        // We should not try to calculate factor^veryLargeNumber, BigDecimal throws arithmetic exception if above 999999999
        receiveCount = Math.min(receiveCount, 9999);
        int retryNumber = Math.max(receiveCount - 1, 0);
        BigDecimal nextVisibilityTimeoutSeconds = new BigDecimal(backoffFactor)
                .pow(retryNumber)
                .multiply(new BigDecimal(initialVisibilityTimeoutSeconds));
        return nextVisibilityTimeoutSeconds.min(new BigDecimal(maxVisibilityTimeoutSeconds)).intValue();
    }

    public boolean calculateIfExpired(String approximateReceiveCount, int ttlSeconds) {
        int retryNumber = Math.max(Optional.ofNullable(approximateReceiveCount).map(Integer::parseInt).orElse(1) - 1, 0);
        int totalTimeoutSeconds = IntStream.range(1, retryNumber)
                .map(i -> calculateNextVisibilityTimeoutSeconds(String.valueOf(i)))
                .sum();
        return totalTimeoutSeconds >= ttlSeconds;
    }

}
