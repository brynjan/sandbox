package no.progconsult.springbootsqs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
		@PropertySource(value = "classpath:springbootsqs.properties"),
		@PropertySource(value = "file:springbootsqs_overrides.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:config_override/springbootsqs_overrides.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:springbootsqs_cognito.properties", ignoreResourceNotFound = true)
})
@Configuration
@ComponentScan
@EnableSqs //skipping @SpringBootApplication since we only want to start the sqsListener.
public class SpringBootSqsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSqsApplication.class, args);
	}
}
