package no.progconsult.flow.external.configuration;

import no.progconsult.flow.external.validate.FailedValidationResponse;
import no.progconsult.flow.external.validate.QuantValidator;
import no.progconsult.flow.external.validate.ValidationException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bno
 */
@Configuration
public class CamelConfiguration {

    @Autowired
    CamelContext camelContext;

    @Bean
    RoutesBuilder quantRouter() {

        return new RouteBuilder() {

            @Override
            public void configure() {

                errorHandler(defaultErrorHandler()
                        .maximumRedeliveries(1)
//                        .backOffMultiplier(2)
                        .retryAttemptedLogLevel(LoggingLevel.WARN));

                onException(ValidationException.class).process(new FailedValidationResponse());

                from("{{route.quant}}").process(quantValidator()).wireTap("jms:auditQueue").choice()
                                       .when(body().contains("route1")).to("jms:route1Queue")
                                       .when(body().contains("route2")).to("jms:route2Queue")
                                       .otherwise().to("jms:badRoute");


                from("jms:auditQueue").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Send to audit");
                    }
                });

                from("jms:route1Queue").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Send to route1");
                    }
                });

                from("jms:route2Queue").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Send to route2");
                    }
                });

                from("jms:badRoute").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Send to badRoute");
                    }
                });

            }
        };
    }

    @Bean
    QuantValidator quantValidator() {
        return new QuantValidator();
    }
}
