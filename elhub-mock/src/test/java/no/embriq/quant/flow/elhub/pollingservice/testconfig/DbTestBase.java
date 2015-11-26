package no.embriq.quant.flow.elhub.pollingservice.testconfig;

import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;

import javax.sql.DataSource;
import java.io.FileReader;

/**
 * Use this class if tests need database access.
 *
 * @author bno
 */
public abstract class DbTestBase extends AbstractCamelRouteWithSedaTest {
    @Autowired
    DataSource dataSource;

    @BeforeClass
    public void setUp() throws Exception {
        //Setup for H2.
        RunScript
                .execute(dataSource.getConnection(), new FileReader("src/main/config/sql/h2_setup.sql"));
    }
}
