package no.progconsult.flow.external;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;


/**
 * Simple camel application using spring boot
 *
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        run(Application.class, args);
    }
}
