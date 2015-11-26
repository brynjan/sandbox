package no.embriq.quant.flow.elhub.pollingservice.testconfig;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.testng.AbstractCamelTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Common logic for adding a set number of routes to a camel context. Is used both for seda and jms.
 */
public abstract class AbstractBaseCamelRouteTest extends AbstractCamelTestNGSpringContextTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    protected ModelCamelContext camelContext;

    @BeforeMethod
    @Override
    protected void springTestContextBeforeTestMethod(Method testMethod) throws Exception {
        super.springTestContextBeforeTestMethod(testMethod);
        addRoutes();
    }


    private void addRoutes() {
        for (Class<? extends RoutesBuilder> routeClass : routes()) {
            try {
                Collection<? extends RoutesBuilder> springRoutes = applicationContext.getBeansOfType(routeClass).values();
                springRoutes.forEach((builder) -> {
                    try {
                        camelContext.addRoutes(builder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Routes to be tested must be added here.
     *
     * @return
     */
    protected abstract List<Class<? extends RoutesBuilder>> routes();
}
