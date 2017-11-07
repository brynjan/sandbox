package no.progconsult.subcontext;

import no.embriq.ws.HelloWorld2;
import no.embriq.ws.HelloWorld2_Service;

import no.progconsult.ws.HelloWorld;
import no.progconsult.ws.HelloWorld_Service;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static no.progconsult.maincontext.CXFServletFactory.getCxfServlet;

/**
 * Endpoints for IS Customer.
 */
//@Configuration
public class SubWsEndpoints {

//    private static final String ROOT_FOLDER = "SSYSv172";
//
//    @Bean
//    CxfEndpoint helloEndpoint(Environment environment, @Value("${ws.endpoint.hello.main.uri}") final String uri) {
//        CxfEndpoint helloEndpoint = new CxfEndpoint();
//        helloEndpoint.setAddress(uri);
//        helloEndpoint.setServiceClass(HelloWorld.class);
//        helloEndpoint.setPortName(HelloWorld_Service.HelloWorldImplPort);
//        helloEndpoint.setWsdlURL("classpath:/wsdl/main/hello.wsdl");
//        setCommonProperties(environment, helloEndpoint);
//        return helloEndpoint;
//    }


    public static CxfEndpoint hello2Endpoint(Bus bus)  {
        CxfEndpoint helloEndpoint = new CxfEndpoint();
//        helloEndpoint.setBus(getCxfServlet().getBus());
        helloEndpoint.setBus(bus);

        helloEndpoint.setAddress("/adapterId/HelloSub");
        helloEndpoint.setServiceClass(HelloWorld2.class);
        helloEndpoint.setPortName(HelloWorld2_Service.HelloWorldImplPort);
        helloEndpoint.setWsdlURL("classpath:/wsdl/sub/hello2.wsdl");
//        setCommonProperties(environment, helloEndpoint);
        return helloEndpoint;
    }


//    @Bean
//    CxfEndpoint helloSubEndpoint(Environment environment, @Value("${ws.endpoint.hello.sub.uri}") final String uri) {
//        CxfEndpoint helloEndpoint = new CxfEndpoint();
//        helloEndpoint.setAddress(uri);
//        helloEndpoint.setServiceClass(HelloWorld.class);
//        helloEndpoint.setPortName(HelloWorld_Service.HelloWorldImplPort);
//        helloEndpoint.setWsdlURL("classpath:/wsdl/main/hello.wsdl");
//        setCommonProperties(environment, helloEndpoint);
//        return helloEndpoint;
//    }


    //
//    @Bean
//    CxfEndpoint requestUpdateOfMeterOnLocationEndpoint(Environment environment, @Value("${ws.endpoint.updateMeterLocation.uri}") final String uri) {
//        CxfEndpoint requestUpdateOfMeterOnLocation = new CxfEndpoint();
//        requestUpdateOfMeterOnLocation.setAddress(uri);
//        requestUpdateOfMeterOnLocation.setServiceClass(SSYSRequestUpdateOfMeterOnLocationPortType.class);
//        requestUpdateOfMeterOnLocation.setPortName(RequestUpdateOfMeterOnLocationService.RequestUpdateOfMeterOnLocationPort);
//        requestUpdateOfMeterOnLocation.setWsdlURL("classpath:" + ROOT_FOLDER + "/wsdl/RequestUpdateOfMeterOnLocation.wsdl");
//        setCommonProperties(environment, requestUpdateOfMeterOnLocation);
//        return requestUpdateOfMeterOnLocation;
//    }
//
//    @Bean
//    CxfEndpoint requestUpdateOfMeterOnLocationTransformerEndpoint(Environment environment, @Value("${ws.endpoint.updateOfMeterTransformerOnLocation.uri}") final String uri) {
//        CxfEndpoint requestUpdateOfMeterTransformerOnLocation = new CxfEndpoint();
//        requestUpdateOfMeterTransformerOnLocation.setAddress(uri);
//        requestUpdateOfMeterTransformerOnLocation.setServiceClass(SSYSRequestUpdateOfMeterTransformerOnLocationPortType.class);
//        requestUpdateOfMeterTransformerOnLocation.setPortName(RequestUpdateOfMeterTransformerOnLocationService.RequestUpdateOfMeterTransformerOnLocationPort);
//        requestUpdateOfMeterTransformerOnLocation.setWsdlURL("classpath:" + ROOT_FOLDER + "/wsdl/RequestUpdateOfMeterTransformerOnLocation.wsdl");
//        setCommonProperties(environment, requestUpdateOfMeterTransformerOnLocation);
//        return requestUpdateOfMeterTransformerOnLocation;
//    }
//
//    @Bean
//    CxfEndpoint getCollectedDataEndpoint(Environment environment, @Value("${ws.endpoint.get.collecteddata.uri}") final String uri) {
//        CxfEndpoint getCollectedData = new CxfEndpoint();
//        getCollectedData.setServiceClass(DataProcessPolling.class);
//        getCollectedData.setPortName(GetCollectedDataToGridService.DataProcessPolling);
//        getCollectedData.setAddress(uri);
//        getCollectedData.setWsdlURL("classpath:" + ROOT_FOLDER + "/wsdl/GetCollectedData.wsdl");
//        setCommonProperties(environment, getCollectedData);
//        return getCollectedData;
//    }
//
//    @Bean
//    CxfEndpoint requestCollectedDataEndpoint(Environment environment, @Value("${ws.endpoint.request.collecteddata.uri}") final String uri) {
//        CxfEndpoint getCollectedData = new CxfEndpoint();
//        getCollectedData.setServiceClass(SSYSRequestCollectedDataPortType.class);
//        getCollectedData.setPortName(RequestCollectedDataService.RequestCollectedDataPort);
//        getCollectedData.setAddress(uri);
//        getCollectedData.setWsdlURL("classpath:" + ROOT_FOLDER + "/wsdl/RequestCollectedData.wsdl");
//        setCommonProperties(environment, getCollectedData);
//        return getCollectedData;
//    }
//
//    @Bean
//    CxfEndpoint getEventsEndpoint(Environment environment, @Value("${ws.endpoint.get.events.uri}") final String uri) {
//        CxfEndpoint getEvents = new CxfEndpoint();
//        getEvents.setServiceClass(EventProcessPolling.class);
//        getEvents.setAddress(uri);
//        getEvents.setWsdlURL("classpath:" + ROOT_FOLDER + "/wsdl/GetEvents.wsdl");
//        getEvents.setPortName(GetEventsToGridService.EventProcessPolling);
//        setCommonProperties(environment, getEvents);
//        return getEvents;
//    }
//
    private void setCommonProperties(Environment environment, CxfEndpoint endpoint) {
        //endpoint.setLoggingFeatureEnabled(true);
        endpoint.setLoggingFeatureEnabled(environment.getProperty("log.ws.clients.requests", boolean.class));

        //Get the soap envelope for auditlogging
//        endpoint.getInInterceptors().add(new BreadCrumbInInterceptor());
//        endpoint.getInInterceptors().add(new AddIncomingSoapMessageToHeaderInterceptor());
        //Gzip encoding in and out
//        endpoint.getOutInterceptors().add(new GZIPOutInterceptor());
//        endpoint.getInInterceptors().add(new GZIPInInterceptor());
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("schema-validation-enabled", "true");
//        properties.put(FaultListener.class.getName(), new QFSoapFaultListener());
//        endpoint.setProperties(properties);
//        endpoint.setHeaderFilterStrategy(new WhiteListHeaderFilterStrategyBuilder().allowedInHeaders(BREADCRUMB_ID).allowedOutHeaders(BREADCRUMB_ID).build());
    }
}
