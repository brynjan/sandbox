package no.progconsult.camel;

import no.progconsult.camel.config.AbstractCamelConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class TestConfig extends AbstractCamelConfig {

//    @Autowired
//    Environment environment;
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
//    @Bean
//    PropertiesParser propertiesParser() {
//        return new DefaultPropertiesParser() {
//            @Override
//            public String parseProperty(String key, String value, Properties properties) {
//                return environment.getProperty(key);
//            }
//        };
//    }
//
//    @Bean
//    PropertiesComponent properties() {
//        PropertiesComponent properties = new PropertiesComponent();
//        properties.setPropertiesParser(propertiesParser());
//        return properties;
//    }
}