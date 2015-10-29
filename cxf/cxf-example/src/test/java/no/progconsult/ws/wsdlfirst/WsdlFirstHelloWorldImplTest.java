package no.progconsult.ws.wsdlfirst;

import no.progconsult.ws.HelloWorld;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.testng.annotations.Test;

import javax.xml.ws.Endpoint;

public class WsdlFirstHelloWorldImplTest {


    @Test
    public void testSayHello() throws Exception{
        String url = "http://localhost:8060/wsdlFirstHelloWorld";
        System.out.println("Starting Server");
        WsdlFirstHelloWorldImpl implementor = new WsdlFirstHelloWorldImpl();
////        String address = "http://localhost:8050/helloWorld";
        Endpoint endpoint = Endpoint.publish(url, implementor);
//
//
        Thread.sleep(1000);
//
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(HelloWorld.class);
        factory.setAddress(url);
        HelloWorld client = (HelloWorld) factory.create();
//
        String reply = client.sayHi("HI");
        System.out.println("Server said: " + reply);
//
        endpoint.stop();
    }

}