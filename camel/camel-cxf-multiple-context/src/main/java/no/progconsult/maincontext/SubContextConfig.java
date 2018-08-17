package no.progconsult.maincontext;

import no.progconsult.subcontext.SubContextCreator;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2017-11-03.
 */
@Configuration
public class SubContextConfig {

    @Bean
    public SubContextCreator subContext(Bus springBus){

        SubContextCreator subContextCreator = new SubContextCreator();
        subContextCreator.startSubContext(springBus);
        return subContextCreator;
    }
}
