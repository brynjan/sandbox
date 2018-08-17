package no.progconsult.flow.elhub.pollingservice.webservice;

import no.elhub.emif.common.aggregatedbusinessinformationentities.v1.ElhubEnergyPartyType;
import no.elhub.emif.common.aggregatedbusinessinformationentities.v1.ElhubHeaderType;
import no.elhub.emif.common.businessdatatype.v1.ElhubEnergyPartyIdentifierType;
import no.elhub.emif.pollfordata.v1.PollForData;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPollingService;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataRequest;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.PollForDataResponse;
import no.elhub.emif.wsdl.polling.meteringvalues.v1.MeteringValuesPolling;
import no.elhub.emif.wsdl.polling.meteringvalues.v1.MeteringValuesPollingService;
import org.testng.annotations.Test;

/**
 * CXF client using WS-SECURITY
 */
public class EndpointTest {

    CxfTestClient cxfTestClient;

    public EndpointTest() {
        this.cxfTestClient = new CxfTestClient.ClientBuilder().build();
    }

    @Test(groups = "cxf-client")
    public void testPollMarketProcesses() throws Exception {

        String url = "http://localhost:9088/pollingservice/PollMarketProcesses";
        MarketProcessesPolling port = new MarketProcessesPollingService().getMarketProcessesPolling();
        cxfTestClient.initialize(url, port);

        PollForDataRequest pollForDataRequest = new PollForDataRequest();
        PollForData pollForData = new PollForData();
        pollForDataRequest.setPollForData(pollForData);
        ElhubHeaderType elhubHeaderType = new ElhubHeaderType();
        pollForData.setHeader(elhubHeaderType);
        elhubHeaderType.setIdentification("12345");
        ElhubEnergyPartyType elhubEnergyPartyType = new ElhubEnergyPartyType();
        elhubHeaderType.setJuridicalSenderEnergyParty(elhubEnergyPartyType);
        ElhubEnergyPartyIdentifierType elhubEnergyPartyIdentifierType = new ElhubEnergyPartyIdentifierType();
        elhubEnergyPartyType.setIdentification(elhubEnergyPartyIdentifierType);
        elhubEnergyPartyIdentifierType.setValue("123456");
        PollForDataResponse pollForDataResponse = port.pollForData(pollForDataRequest);

        System.out.println();
    }

    @Test(groups = "cxf-client")
    public void testPollMeteringValues() throws Exception {

        String url = "http://localhost:9088/pollingservice/PollMeteringValues";
        MeteringValuesPolling port = new MeteringValuesPollingService().getMeteringValuesPolling();
        cxfTestClient.initialize(url, port);

        no.elhub.emif.wsdl.polling.meteringvalues.v1.PollForDataRequest pollForDataRequest = new no.elhub.emif.wsdl.polling
                .meteringvalues.v1.PollForDataRequest();
        PollForData pollForData = new PollForData();
        pollForDataRequest.setPollForData(pollForData);
        ElhubHeaderType elhubHeaderType = new ElhubHeaderType();
        pollForData.setHeader(elhubHeaderType);
        elhubHeaderType.setIdentification("12345");
        ElhubEnergyPartyType elhubEnergyPartyType = new ElhubEnergyPartyType();
        elhubHeaderType.setJuridicalSenderEnergyParty(elhubEnergyPartyType);
        ElhubEnergyPartyIdentifierType elhubEnergyPartyIdentifierType = new ElhubEnergyPartyIdentifierType();
        elhubEnergyPartyType.setIdentification(elhubEnergyPartyIdentifierType);
        elhubEnergyPartyIdentifierType.setValue("123456");
        no.elhub.emif.wsdl.polling.meteringvalues.v1.PollForDataResponse pollForDataResponse = port
                .pollForData(pollForDataRequest);

        System.out.println();
    }
}
