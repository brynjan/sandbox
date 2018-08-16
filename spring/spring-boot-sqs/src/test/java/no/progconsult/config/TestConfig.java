package no.progconsult.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import no.progconsult.AbstractSystemTest;
import no.progconsult.common.SqsAsyncClientMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2018-08-06.
 */
@Configuration
public class TestConfig {

    @Primary
    @Bean
    public AmazonSQSAsync amazonSQS(Environment environment) {
        SqsAsyncClientMock sqsClientMock = new SqsAsyncClientMock();
        AbstractSystemTest.setupSqsQueues(sqsClientMock, environment);
        return sqsClientMock;
    }
}
