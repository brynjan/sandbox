package no.progconsult.ws.codefirst;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.testng.annotations.Test;

import javax.xml.ws.Endpoint;

/**
 * @author bno
 */
public class CodeFirstHelloWorldImplTest {


    @Test
    public void testSayHello() throws Exception{
        String url = "http://localhost:8050/codeFirstHelloWorld";
        System.out.println("Starting Server");
        CodeFirstHelloWorldImpl implementor = new CodeFirstHelloWorldImpl();
//        String address = "http://localhost:8050/helloWorld";
        Endpoint endpoint = Endpoint.publish(url, implementor);


        Thread.sleep(10000);

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(CodeFirstHelloWorld.class);
        factory.setAddress(url);
        CodeFirstHelloWorld client = (CodeFirstHelloWorld) factory.create();

        String reply = client.sayHi("HI");
        System.out.println("Server said: " + reply);

        endpoint.stop();
    }

}