package no.embriq.quant.flow.elhub.pollingservice.route.outbound;

import no.embriq.quant.flow.elhub.pollingservice.common.ExchangeDelegate;

import no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataResponse;
import org.apache.camel.Headers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract class containing logic related when building output.
 *
 * @author bno
 */
public class ResponseBuilder {

    private final ExchangeDelegate exchangeDelegate;

    public ResponseBuilder() {
        exchangeDelegate = new ExchangeDelegate();
    }

    //------------------------------ INVOKED FROM CAMEL ------------------------------

//    final String marketProcessResponsePath =
// "no/embriq/quant/flow/elhub/pollingservice/route/PollMarketProcessesResponse_normal.xml";
//
//    final String meteringValuesResponsePath =
// "no/embriq/quant/flow/elhub/pollingservice/route/PollMeteringValuesResponse_normal.xml";

    final String marketProcessResponsePath =
            "PollMarketProcessesResponse_normal.xml";

    final String meteringValuesResponsePath =
            "PollMeteringValuesResponse_normal.xml";

    LocalDateTime marketPoll;

    LocalDateTime meteringPoll;

    /**
     * Invoked from Camel.
     * Build the response object our published endpoints.
     *
     * @param headers
     * @return
     */
    public PollForDataResponse buildMarketProcesses(@Headers final Map<String, Object> headers) {
        if (marketPoll == null || Duration.between(marketPoll, LocalDateTime.now()).toMinutes() > 5) {
            marketPoll = LocalDateTime.now();
            PollForDataResponse pollForDataResponse = createResponse(marketProcessResponsePath, PollForDataResponse.class);
            pollForDataResponse.setIdentification(UUID.randomUUID().toString());
            return pollForDataResponse;
        }
        return null;
    }

    public no.elhub.emif.wsdl.polling.meteringvalues.v1.PollForDataResponse buildMeteringValues(
            @Headers final Map<String, Object> headers) {
        if (meteringPoll == null || Duration.between(meteringPoll, LocalDateTime.now()).toMinutes() > 5) {
            meteringPoll = LocalDateTime.now();
            no.elhub.emif.wsdl.polling.meteringvalues.v1.PollForDataResponse pollForDataResponse = createResponse(
                    meteringValuesResponsePath, no.elhub.emif.wsdl.polling.meteringvalues.v1.PollForDataResponse.class);
            pollForDataResponse.setIdentification(UUID.randomUUID().toString());
            return pollForDataResponse;
        }
        return null;
    }

    //--------------------------------------------------------------------------------

    protected <T> T createResponse(final String path, Class<T> clazz) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            final JAXBElement<T> actualMessage = jaxbContext
                    .createUnmarshaller()
                    .unmarshal(new StreamSource(this
                                    .getClass().getClassLoader()
                                    .getResourceAsStream(
                                            path)),
                            clazz);
            return actualMessage.getValue();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
