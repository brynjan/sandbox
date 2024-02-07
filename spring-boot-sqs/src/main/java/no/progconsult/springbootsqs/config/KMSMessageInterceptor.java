package no.progconsult.springbootsqs.config;

import io.awspring.cloud.sqs.listener.interceptor.MessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-07.
 */
public class KMSMessageInterceptor implements MessageInterceptor<Object> {

    private transient static final Logger LOG = LoggerFactory.getLogger(KMSMessageInterceptor.class);

    @Override
    public Message<Object> intercept(Message<Object> message) {
        LOG.info("Inside interceptor");



        return MessageInterceptor.super.intercept(message);
    }
}
