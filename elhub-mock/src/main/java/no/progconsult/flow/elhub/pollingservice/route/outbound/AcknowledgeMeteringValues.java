package no.progconsult.flow.elhub.pollingservice.route.outbound;

import no.elhub.emif.acknowledgement.v1.Acknowledgement;
import no.elhub.emif.wsdl.polling.meteringvalues.v1.AcknowledgePollRequest;
import no.progconsult.flow.elhub.pollingservice.common.DateUtils;
import org.apache.camel.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by sunilvuppala
 */
public class AcknowledgeMeteringValues {

    public List<?> setBindingValues(@Headers final Map<String, Object> headers,@Body Object body) {

        AcknowledgePollRequest acknowledgePollRequest = (AcknowledgePollRequest) body;

        Acknowledgement acknowledgement = acknowledgePollRequest.getAcknowledgement();
        final String IDENTIFICATION = acknowledgement.getHeader().getIdentification().toString();
        final Timestamp ACKNOWLEDGE = Timestamp.valueOf(DateUtils.utc());

        return Arrays.asList(ACKNOWLEDGE, IDENTIFICATION);


    }
}