package no.progconsult.embriq.quant.flow.external.configuration;

import no.progconsult.embriq.quant.flow.external.heartbeat.HearBeatController;
import no.progconsult.embriq.quant.flow.external.web.QuantController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(HearBeatController.class);
		register(QuantController.class);
	}
}