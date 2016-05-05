package no.progconsult.camel;

import no.progconsult.camel.config.CamelRoute;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

//@MockEndpoints
public class CamelRouteTest extends AbstractRouteTest {
//    @EndpointInject(uri = "mock:direct:end")
//    protected MockEndpoint endEndpoint;
//
    @EndpointInject(uri = "mock:activemq:complete")
    protected MockEndpoint errorEndpoint;

    @Produce(uri = "{{startEndpoint}}")
    protected ProducerTemplate testProducer;

    @Produce(uri = "activemq:start")
    protected ProducerTemplate activeMqProducer;

    @Test
    public void testRoute() throws InterruptedException {
//        CamelContext camelContext = applicationContext.getBean(CamelContext.class);
        System.out.println("");
        testProducer.sendBody("<name>test</name>");
        activeMqProducer.sendBody("test");

    }

    @Override
    protected List<Class<? extends RoutesBuilder>> routes() {
        return Collections.singletonList(CamelRoute.class);
    }
}