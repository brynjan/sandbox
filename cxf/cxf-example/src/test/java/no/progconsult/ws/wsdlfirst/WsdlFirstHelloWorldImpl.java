package no.progconsult.ws.wsdlfirst;

import no.progconsult.ws.HelloWorld;
import no.progconsult.ws.codefirst.CodeFirstHelloWorld;

import javax.jws.WebService;

@WebService(endpointInterface = "no.progconsult.ws.HelloWorld",
            serviceName = "CodeFirstHelloWorld")
public class WsdlFirstHelloWorldImpl implements HelloWorld {

 
    public String sayHi(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;
    }
 
}