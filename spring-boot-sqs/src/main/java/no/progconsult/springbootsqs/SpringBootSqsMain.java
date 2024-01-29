package no.progconsult.springbootsqs;

import io.awspring.cloud.autoconfigure.context.ContextCredentialsAutoConfiguration;
import io.awspring.cloud.autoconfigure.context.ContextResourceLoaderAutoConfiguration;
import io.awspring.cloud.autoconfigure.context.ContextStackAutoConfiguration;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import static org.slf4j.LoggerFactory.getLogger;

@PropertySources({
        @PropertySource(value = "classpath:springbootsqs.properties"),
        @PropertySource(value = "file:springbootsqs_overrides.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:config_override/springbootsqs_overrides.properties", ignoreResourceNotFound = true)
})
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = {ContextStackAutoConfiguration.class, ContextCredentialsAutoConfiguration.class, ContextResourceLoaderAutoConfiguration.class})
public class SpringBootSqsMain {

    private static final Logger LOG = getLogger(SpringBootSqsMain.class);

    public static void main(String[] args) {
        try {
            final ConfigurableApplicationContext configurableApplicationContext = SpringApplication
                    .run(SpringBootSqsMain.class, args);
            final Environment environment = configurableApplicationContext.getBean(Environment.class);
            LOG.info("Server started on http://localhost:{}{}",
                    environment.getProperty("server.port"), environment.getProperty("base.path"));
        } catch (Throwable e) {
            LOG.error("Failed to start server", e);
        }
    }
}
