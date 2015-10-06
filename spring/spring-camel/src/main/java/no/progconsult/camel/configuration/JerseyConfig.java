package no.progconsult.camel.configuration;

import no.progconsult.camel.heartbeat.HearBeatController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(HearBeatController.class);
	}
}