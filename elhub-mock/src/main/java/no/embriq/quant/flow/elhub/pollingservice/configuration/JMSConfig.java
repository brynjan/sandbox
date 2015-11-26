package no.embriq.quant.flow.elhub.pollingservice.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.JmsTransactionManager;

/**
 * Active MQ JMS configuration. Jms configuration is defined in elwinadapter.properties.
 */
@Configuration
public class JMSConfig {
    @Autowired
    private Environment env;

    @Bean
    public PooledConnectionFactory pooledJmsConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                env.getProperty("jms.username"),
                env.getProperty("jms.password"),
                env.getProperty("jms.url"));
        activeMQConnectionFactory
                .getRedeliveryPolicy()
                .setMaximumRedeliveries(Integer.parseInt(env.getProperty("jms.max.redeliveries.from.broker")));
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        pooledConnectionFactory.setMaxConnections(Integer.parseInt(env.getProperty("jms.max.connections")));
        return pooledConnectionFactory;
    }

    @Bean
    JmsTransactionManager jmsTransactionManager(PooledConnectionFactory pooledConnectionFactory) {
        JmsTransactionManager tx = new JmsTransactionManager();
        tx.setConnectionFactory(pooledConnectionFactory);
        return tx;
    }


    @Bean
    JmsConfiguration camelJMSConfiguration(PooledConnectionFactory pooledConnectionFactory,
                                           JmsTransactionManager jmsTransactionManager) {
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setConnectionFactory(pooledConnectionFactory);
        jmsConfiguration.setTransacted(true);
        jmsConfiguration.setRequestTimeout(10000);
        jmsConfiguration.setCacheLevelName("CACHE_CONSUMER");
        jmsConfiguration.setDisableReplyTo(true);
        jmsConfiguration.setTransactionManager(jmsTransactionManager);
        return jmsConfiguration;
    }
}

