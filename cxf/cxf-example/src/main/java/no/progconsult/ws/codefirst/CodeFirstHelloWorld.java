package no.progconsult.ws.codefirst;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface CodeFirstHelloWorld {

    String sayHi(@WebParam(name="text") String text);

}