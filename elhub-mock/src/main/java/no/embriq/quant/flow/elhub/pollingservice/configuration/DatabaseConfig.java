package no.embriq.quant.flow.elhub.pollingservice.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Configure database for camel sql component.
 *
 * @author bno
 */
@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;


    //TODO: Consider moving away from "old-school" DriverManager-based JDBC driver configuration. Needs to be synchronized with
    //TODO: setup of H2 for testing.
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("database.driver.class.name"));
        config.setJdbcUrl(env.getProperty("database.url"));
        config.setUsername(env.getProperty("database.username"));
        config.setPassword(env.getProperty("database.password"));
        config.setMaximumPoolSize(Integer.parseInt(env.getProperty("database.max.connections")));
        final HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    @Bean
    DataSourceTransactionManager dataSourceTransactionManager(final DataSource dataSource) {
        final DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    //Default behaviour, always add this bean if another propagation bean is implemented in order to keep default behaviour.
    @Bean
    SpringTransactionPolicy PROPAGATION_REQUIRED(final DataSourceTransactionManager dataSourceTransactionManager) {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(dataSourceTransactionManager);
        return springTransactionPolicy;
    }

    @Bean
    SpringTransactionPolicy PROPAGATION_NOT_SUPPORTED(final DataSourceTransactionManager dataSourceTransactionManager) {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(dataSourceTransactionManager);
        return springTransactionPolicy;
    }

    @Bean
    SqlComponent sql(final DataSource dataSource) {
        final SqlComponent sqlComponent = new SqlComponent();
        sqlComponent.setDataSource(dataSource);
        return sqlComponent;
    }
}
