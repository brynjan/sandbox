package no.progconsult.springbootsqs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-14.
 */


@RestController
@RequestMapping(path = "${base.path}/sqs")
public class TriggSqsController {

    private static final Logger LOG = LoggerFactory.getLogger(TriggSqsController.class);

    private final SendToSqsRoute sendToSqsRoute;

    public TriggSqsController(SendToSqsRoute sendToSqsRoute) {
        this.sendToSqsRoute = sendToSqsRoute;
    }

    @PostMapping("/send")
    public String index() {
        sendToSqsRoute.send();
        return "test";
    }
}
