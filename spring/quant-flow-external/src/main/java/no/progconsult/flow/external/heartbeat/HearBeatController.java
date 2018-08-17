package no.progconsult.flow.external.heartbeat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author bno
 */
@Component
@Path("/heartbeat")
public class HearBeatController {

    @Value("${heartbeat.respons}")
    private String msg;

    @GET
    public String heartbeat() {
        return "Hello " + msg;
    }
}
