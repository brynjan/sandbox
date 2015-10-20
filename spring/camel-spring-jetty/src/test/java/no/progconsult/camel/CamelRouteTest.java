package no.progconsult.camel;

import no.progconsult.camel.config.CamelRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

//@MockEndpoints
public class CamelRouteTest extends AbstractRouteTest {
//    @EndpointInject(uri = "mock:direct:end")
//    protected MockEndpoint endEndpoint;
//
//    @EndpointInject(uri = "mock:direct:error")
//    protected MockEndpoint errorEndpoint;

    @Produce(uri = "{{startEndpoint}}")
    protected ProducerTemplate testProducer;


    @Test
    public void testRoute() throws InterruptedException {
        CamelContext camelContext = applicationContext.getBean(CamelContext.class);
        System.out.println("");
        testProducer.sendBody("<name>test</name>");
    }

    @Override
    protected List<Class<? extends RoutesBuilder>> routes() {
        return Collections.singletonList(CamelRoute.class);
    }
}