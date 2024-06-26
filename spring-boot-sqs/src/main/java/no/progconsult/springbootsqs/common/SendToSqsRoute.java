package no.progconsult.springbootsqs.common;

import io.awspring.cloud.sqs.operations.SqsOperations;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import no.embriq.quant.flow.typelib.common.QFEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:brynjar.norum@embriq.no">Brynjar Norum</a> 2024-02-14.
 */
@Component
public class SendToSqsRoute {


    private static final Logger LOG = LoggerFactory.getLogger(SendToSqsRoute.class);

    private final SqsTemplate sqsTemplate;

    public SendToSqsRoute(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void send(){

//        Map<String, Object> agentSqsHeader = new HashMap<>();
//        agentSqsHeader.put("_KMS_CMK_ID", "alias/embriq-volue-test");
//        agentSqsHeader.put("_S3_BUCKET", "embriq-volue-test.eu-west-1.416899602824");

        String story = "Det var en lang historie";
        StringBuffer buf = new StringBuffer(story);
        for(int i = 0; i<4_000_000;i++){
            buf.append(story);
        }


        QFEvent qfEvent = new QFEvent()
                .withMessageId(UUID.randomUUID().toString())
                .withDateCreated(Instant.now())
                .withMessageType("Ukjent")
//                .withPayload("test");
                .withPayload(buf.toString());

        LOG.info("Lengde: {}", buf.toString().length());

//        sqsOperations.send(sqsSendOptions -> sqsSendOptions.queue("embriq-volueagent-volueadapter").headers(agentSqsHeader).payload(qfEvent));
        sqsTemplate.send(sqsSendOptions -> sqsSendOptions.queue("embriq-volueagent-volueadapter").payload(qfEvent));
        LOG.info("Message sent");
    }
}
