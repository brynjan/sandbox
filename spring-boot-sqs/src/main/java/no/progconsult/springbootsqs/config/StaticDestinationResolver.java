package no.progconsult.springbootsqs.config;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.springframework.jms.support.destination.DestinationResolver;

/**
 * Created by lcsontos on 8/4/17.
 */
public class StaticDestinationResolver implements DestinationResolver {

    private final String queueName;

    public StaticDestinationResolver(String queueName) {
        this.queueName = queueName;
    }


    @Override
    public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
        return session.createQueue(queueName);
    }
}
