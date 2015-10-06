package no.progconsult.embriq.quant.flow.external.web;

import no.progconsult.embriq.quant.flow.external.validate.ValidationException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;

/**
 * @author bno
 */
@Component
@Path("/quant")
public class QuantController {

    @Autowired
    private ProducerTemplate producer;

    @Value("${route.quant}")
    private String fromQueue;

//    @Value("${route.audit.log}")
//    private String auditLog;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response add(String xml) {
        // TODO [grkorz]: refactor
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("user", "enoro0");
        headers.put("timestamp", new Date());
//        producer.sendBodyAndHeaders(auditLog, xml, headers);
        try {
            producer.requestBody(fromQueue, xml, String.class);
            return Response.ok().build();
        } catch (CamelExecutionException ex) {
            if (ex.getCause() instanceof ValidationException) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Validation failed")
                               .build();
            }
        }
        return Response.serverError().build();
    }
}
