package no.progconsult.camel;

import no.progconsult.camel.config.CamelRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.testng.AbstractCamelTestNGSpringContextTests;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author bno
 */
@ContextConfiguration(
        classes = {TestConfig.class},
        loader = CamelSpringDelegatingTestContextLoader.class, inheritLocations = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractRouteTest extends AbstractCamelTestNGSpringContextTests {

    @BeforeMethod
    @Override
    protected void springTestContextBeforeTestMethod(Method testMethod) throws Exception {
        super.springTestContextBeforeTestMethod(testMethod);
        CamelContext camelContext = applicationContext.getBean(CamelContext.class);
        routes().forEach((builder) -> {
            try {
                camelContext.addRoutes(builder.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected abstract List<Class<? extends RoutesBuilder>> routes();
}
