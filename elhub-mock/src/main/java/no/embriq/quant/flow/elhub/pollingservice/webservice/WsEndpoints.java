package no.embriq.quant.flow.elhub.pollingservice.webservice;


import no.embriq.quant.camel.QuantCxfEndpoint;

import no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling;
import no.elhub.emif.wsdl.polling.meteringvalues.v1.MeteringValuesPolling;
import no.elhub.emif.wsdl.query.v1.Query;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.cxf.Bus;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Endpoint exposed to clients of ElhubPollingService
 */
@Configuration
public class WsEndpoints {

    @Bean
    CxfEndpoint meteringValuesSoapEndpoint(Environment environment,
                                           KeystoreCallbackHandler keystoreCallbackHandler) {
        //For xsd schema validation. Default is 5000, maxOccurs in a batch is 9999.
        System.setProperty("jdk.xml.maxOccurLimit", "9999");

        CxfEndpoint meteringValuesEndpoint = new CxfEndpoint();
        meteringValuesEndpoint.setAddress("/PollMeteringValues");
        meteringValuesEndpoint.setServiceClass(MeteringValuesPolling.class);
        meteringValuesEndpoint.setWsdlURL("classpath:v1/wsdl/PollMeteringValues - WS-Security.wsdl");
        setCommonProperties(meteringValuesEndpoint, environment, keystoreCallbackHandler);
        return meteringValuesEndpoint;
    }

    @Bean
    CxfEndpoint marketProcessesSoapEndpoint(Environment environment,
                                            KeystoreCallbackHandler keystoreCallbackHandler) {
        CxfEndpoint marketProcessesEndpoint = new CxfEndpoint();
        marketProcessesEndpoint.setAddress("/PollMarketProcesses");
        marketProcessesEndpoint.setServiceClass(MarketProcessesPolling.class);
        marketProcessesEndpoint.setWsdlURL("classpath:v1/wsdl/PollMarketProcesses - WS-Security.wsdl");
        setCommonProperties(marketProcessesEndpoint, environment, keystoreCallbackHandler);
        return marketProcessesEndpoint;
    }

    private void setCommonProperties(CxfEndpoint endpoint, Environment environment,
                                     KeystoreCallbackHandler keystoreCallbackHandler) {
        endpoint.setLoggingFeatureEnabled(true);
        //Get the soap envelope for auditlogging
        endpoint.getInInterceptors().add(new AddIncomingSoapMessageToHeaderInterceptor());
        //Gzip encoding in and out
        endpoint.getOutInterceptors().add(new GZIPOutInterceptor());
        endpoint.getInInterceptors().add(new GZIPInInterceptor());
        //Get the orgnumber from the wss-security public key
        // endpoint.getInInterceptors().add(new GetOrgNumberFromCertInterceptor());


        Map<String, Object> properties = new HashMap<>();
        //Ws-security
        properties.put("ws-security.signature.properties", new WSSecurity().createWsSecurityProperties(environment));
        properties.put("ws-security.callback-handler", keystoreCallbackHandler);
        //Xsd validation on soap endpoints
        //TODO: validation create out of memory. verify elhub xsds
        //properties.put("schema-validation-enabled", "true");
        endpoint.setProperties(properties);
    }
}
