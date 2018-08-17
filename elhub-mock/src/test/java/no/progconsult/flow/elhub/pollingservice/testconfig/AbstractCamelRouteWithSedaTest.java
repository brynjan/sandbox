package no.progconsult.flow.elhub.pollingservice.testconfig;

import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;

/**
 * Test classes that extends this class will run their tests with a seda memory broker.
 * We overrride the jms component definition with seda.
 */
@ContextConfiguration(classes = {CamelRouteTestWithSedaConfig.class}, loader = CamelSpringDelegatingTestContextLoader.class,
        inheritLocations = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractCamelRouteWithSedaTest extends AbstractBaseCamelRouteTest {
}
