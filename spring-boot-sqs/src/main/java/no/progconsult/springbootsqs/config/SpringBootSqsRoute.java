package no.progconsult.springbootsqs.config;

import no.embriq.quant.flow.typelib.common.QFEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringBootSqsRoute {

    private transient static final Logger LOG = LoggerFactory.getLogger(SpringBootSqsRoute.class);

    public void processMessage(final QFEvent qfEvent){
        LOG.info("Inside route");
        throw new RuntimeException("test");
    }

}
