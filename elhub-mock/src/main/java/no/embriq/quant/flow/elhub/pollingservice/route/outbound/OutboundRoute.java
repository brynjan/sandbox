package no.embriq.quant.flow.elhub.pollingservice.route.outbound;

import no.embriq.quant.flow.elhub.pollingservice.common.QuantFlowContextHeaders;

import org.apache.camel.LoggingLevel;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OutboundRoute extends SpringRouteBuilder {

    @Value("${outbound.batch.limit}")
    private String queryLimit;

    private static final String ACKNOWLEDGE_OPERATION_NAME = "AcknowledgePoll";


    @Override
    public void configure() throws Exception {

        // ------------------- MARKETPROCESSES -------------------

        from("cxf:bean:marketProcessesSoapEndpoint")
                .routeId("bean:marketProcessesSoapEndpoint")
                .choice()
                .when(exchange -> exchange
                        .getIn().getHeader(CxfConstants.OPERATION_NAME, String.class)
                        .equals(ACKNOWLEDGE_OPERATION_NAME))
                .to("direct:route.outbound.cxf.acknowledge.marketprocesses").removeHeaders("*")
                .otherwise()
                .to("direct:route.outbound.cxf.poll.marketprocesses").removeHeaders("*");


        from("direct:route.outbound.cxf.poll.marketprocesses")
                .routeId("outbound.cxf.poll.marketprocesses")
                .process(new QuantFlowContextHeaders())
                .process(new UnpackCxfXBodyProcessor())
                .bean(ResponseBuilder.class, "buildMarketProcesses");

        from("direct:route.outbound.cxf.acknowledge.marketprocesses")
                .transacted()
                .routeId("outbound.cxf.acknowledge.marketprocesses")
                .process(new QuantFlowContextHeaders())
                .process(new UnpackCxfXBodyProcessor())
                //send message both to audit log and processing queue, ensure that error in first step stops processing
                .bean(AcknowledgeMarketProcesses.class, "setBindingValues")
                .log(LoggingLevel.INFO, "Message ack");

        // ------------------- METERINGVALUES -------------------

        from("cxf:bean:meteringValuesSoapEndpoint")
                .routeId("bean:meteringValuesSoapEndpoint")
                .choice()
                .when(exchange -> exchange
                        .getIn().getHeader(CxfConstants.OPERATION_NAME, String.class)
                        .equals(ACKNOWLEDGE_OPERATION_NAME))
                .to("direct:route.outbound.cxf.acknowledge.meteringvalues").removeHeaders("*")
                .otherwise()
                .to("direct:route.outbound.cxf.poll.meteringvalues").removeHeaders("*");

        from("direct:route.outbound.cxf.poll.meteringvalues")
                .routeId("outbound.cxf.poll.meteringvalues")
                .process(new QuantFlowContextHeaders())
                .process(new UnpackCxfXBodyProcessor())
                .bean(ResponseBuilder.class, "buildMeteringValues");

        from("direct:route.outbound.cxf.acknowledge.meteringvalues")
                .routeId("outbound.cxf.acknowledge.meteringvalues")
                .process(new QuantFlowContextHeaders())
                .process(new UnpackCxfXBodyProcessor())
                .routeId("outbound.acknowledge.meteringvalues")
                .bean(AcknowledgeMeteringValues.class, "setBindingValues")
                .log(LoggingLevel.INFO, "Message acknowledged");

    }
}
