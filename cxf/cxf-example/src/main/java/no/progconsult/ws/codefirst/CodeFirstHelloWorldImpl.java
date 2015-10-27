package no.progconsult.ws.codefirst;

import javax.jws.WebService;
 
@WebService(endpointInterface = "no.progconsult.ws.codefirst.CodeFirstHelloWorld",
            serviceName = "CodeFirstHelloWorld")
public class CodeFirstHelloWorldImpl implements CodeFirstHelloWorld {

 
    public String sayHi(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;
    }
 
}