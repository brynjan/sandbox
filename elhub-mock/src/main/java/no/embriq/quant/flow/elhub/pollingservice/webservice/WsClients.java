package no.embriq.quant.flow.elhub.pollingservice.webservice;

import no.embriq.quant.camel.QuantCxfEndpoint;

import no.elhub.emif.wsdl.marketprocesses.v1.MarketProcesses;
import no.elhub.emif.wsdl.meteringvalues.v1.MeteringValues;
import no.elhub.emif.wsdl.polling.marketprocesses.v1.MarketProcessesPolling;
import no.elhub.emif.wsdl.polling.meteringvalues.v1.MeteringValuesPolling;
import no.elhub.emif.wsdl.query.v1.Query;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Webservice clients that signs the request using the provided keystore.
 * Should be used when we are sending unsigned messages to elhub.
 */
@Configuration
public class WsClients {


    @Bean
    CxfEndpoint pollMeteringValuesClient(Environment environment,
                                         KeystoreCallbackHandler keystoreCallbackHandler) throws Exception {
        //For xsd schema validation. Default is 5000, maxOccurs in a batch is 9999.
        System.setProperty("jdk.xml.maxOccurLimit", "9999");

        QuantCxfEndpoint meteringValuesEndpoint = new QuantCxfEndpoint();
        meteringValuesEndpoint.setAddress(environment.getProperty("elhub.pollmeteringvalues.ws.url"));
        meteringValuesEndpoint.setServiceClass(MeteringValuesPolling.class);
        meteringValuesEndpoint.setWsdlURL("classpath:v1/wsdl/PollMeteringValues - WS-Security.wsdl");
        setCommonProperties(meteringValuesEndpoint, environment, keystoreCallbackHandler);
        return meteringValuesEndpoint;
    }

    @Bean
    CxfEndpoint pollMarketProcessesClient(Environment environment,
                                          KeystoreCallbackHandler keystoreCallbackHandler) throws Exception {
        QuantCxfEndpoint marketProcessesEndpoint = new QuantCxfEndpoint();
        marketProcessesEndpoint.setAddress(environment.getProperty("elhub.pollmarketprocesses.ws.url"));
        marketProcessesEndpoint.setServiceClass(MarketProcessesPolling.class);
        marketProcessesEndpoint.setWsdlURL("classpath:v1/wsdl/PollMarketProcesses - WS-Security.wsdl");
        setCommonProperties(marketProcessesEndpoint, environment, keystoreCallbackHandler);
        return marketProcessesEndpoint;
    }

    private void setCommonProperties(QuantCxfEndpoint endpoint,
                                     Environment environment,
                                     KeystoreCallbackHandler keystoreCallbackHandler) throws Exception {
        //Gzip encoding in and out
        endpoint.getInInterceptors().add(new GZIPInInterceptor());
        endpoint.getOutInterceptors().add(new GZIPOutInterceptor());
        //Get the soap envelope that we send to elhub so it can be sent to the auditlog.
        endpoint.getOutInterceptors().add(new AddOutgoingSoapMessageToHeaderInterceptor());
        //Log request and response
        endpoint.setLoggingFeatureEnabled(true);

        //Auto get the soap operation
        endpoint.setDiscoverOperation(true);
        Map<String, Object> properties = new HashMap<>();
        //Xsd validation
        //TODO: commented out since makes test hang..
//        properties.put("schema-validation-enabled", "true");
        //Ws-security x509 signing
        properties.put("ws-security.signature.properties", new WSSecurity().createWsSecurityProperties(environment));
        properties.put("ws-security.callback-handler", keystoreCallbackHandler);
        endpoint.setProperties(properties);
    }
}
