package no.progconsult.flow.elhub.pollingservice.webservice;

import no.elhub.emif.wsdl.polling.marketprocesses.v1.AcknowledgePollRequest;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.ElhubSOAPFault;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataRequest;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataResponse;
import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;

import javax.jws.WebService;
import java.util.logging.Logger;

/**
 * This class is used to test the WSS x509 cert signing for elhub requests.
 */

@WebService(
        serviceName = "MarketProcessesPollingService",
        portName = "MarketProcessesPolling",
        targetNamespace = "urn:no:elhub:emif:wsdl:polling:marketprocesses:v1",
        wsdlLocation = "classpath:v1/wsdl/PollMarketProcesses - WS-Security.wsdl",
        endpointInterface = "no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling")

@EndpointProperties({
        @EndpointProperty(key = "ws-security.signature.properties", value = "mockendpoint_wss.properties"),
        @EndpointProperty(key = "ws-security.callback-handler", value = "no.progconsult.flow.elhub.pollingservice.webservice"
                + ".MockEndpointKeystoreCallbackHandler")
})
public class MarketProcessesPollingImpl implements MarketProcessesPolling {

    private static final Logger LOG = Logger.getLogger(MarketProcessesPollingImpl.class.getName());

    private PollForDataResponse pollResponse;

    private ElhubSOAPFault fault;

    /* (non-Javadoc)
     * @see no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling#pollForData(no.elhub.emif.wsdl.polling
     * .marketprocesses.v1.PollForDataRequest  pollForDataRequest )*
     */
    @Override
    public no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataResponse pollForData(
            PollForDataRequest pollForDataRequest) throws
            ElhubSOAPFault {
        LOG.info("Executing operation pollForData");
        System.out.println(pollForDataRequest);
        if (fault != null) {
            ElhubSOAPFault fault = this.fault;
            this.fault = null;
            throw fault;
        }
        return pollResponse;
    }

    /* (non-Javadoc)
     * @see no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling#acknowledgePoll(no.elhub.emif.wsdl.polling
     * .marketprocesses.v1.AcknowledgePollRequest  acknowledgePollRequest )*
     */
    @Override
    public void acknowledgePoll(AcknowledgePollRequest acknowledgePollRequest) throws ElhubSOAPFault {
        LOG.info("Executing operation acknowledgePoll");
        System.out.println(acknowledgePollRequest);
    }

    /**
     * Inject reponse from Elhub Webservice Mock.
     *
     * @param pollResponse
     */
    public void setPollResponse(PollForDataResponse pollResponse) {
        this.pollResponse = pollResponse;
    }

    public void setFault(ElhubSOAPFault fault) {
        this.fault = fault;
    }
}