package no.progconsult.springbootsqs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Created by lcsontos on 8/4/17.
 */
//@Component
public class MessageConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "${sqs.queue.in}")
    public void receive(@Payload String message) {
        LOG.info("Received message {}.", message);
        throw new RuntimeException("feil feil");
    }

}
