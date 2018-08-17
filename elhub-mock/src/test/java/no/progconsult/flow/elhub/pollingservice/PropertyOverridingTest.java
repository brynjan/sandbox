package no.progconsult.flow.elhub.pollingservice;

import no.progconsult.flow.elhub.pollingservice.testconfig.AbstractCamelRouteWithSedaTest;

import org.apache.camel.RoutesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing our propertySetup.
 */

public class PropertyOverridingTest extends AbstractCamelRouteWithSedaTest {
    private static final File overridePropertyTestFile = new File("elhubpollingservice_overrides.properties");

    @Autowired
    private Environment environment;

    /**
     * Creating a temporary overrides.properties file before we load the properties with spring.
     */
    @Override
    @BeforeClass
    protected void springTestContextPrepareTestInstance() throws Exception {
        FileWriter writer = new FileWriter(overridePropertyTestFile);
        writer.write("test.property.overrided = Is overrided");
        writer.flush();
        super.springTestContextPrepareTestInstance();
    }

    @Test
    public void propertiesFromOverrideFileIsOverrided() {
        assertThat(environment.getProperty("test.property.overrided")).isEqualTo("Is overrided");
    }

    @Override
    @AfterClass
    protected void springTestContextAfterTestClass() throws Exception {
        overridePropertyTestFile.delete();
        super.springTestContextAfterTestClass();
    }


    @Override
    protected List<Class<? extends RoutesBuilder>> routes() {
        return Collections.emptyList();
    }
}
