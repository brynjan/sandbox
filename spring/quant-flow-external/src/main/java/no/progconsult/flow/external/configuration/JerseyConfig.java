package no.progconsult.flow.external.configuration;

import no.progconsult.flow.external.heartbeat.HearBeatController;
import no.progconsult.flow.external.web.QuantController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(HearBeatController.class);
		register(QuantController.class);
	}
}