package no.progconsult.flow.elhub.pollingservice.webservice;


import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * We need to log the entire incoming soap envelope to the auditlog.
 */
public class AddOutgoingSoapMessageToHeaderInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
    public AddOutgoingSoapMessageToHeaderInterceptor() {
        super(Phase.POST_STREAM);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        SOAPMessage soapMessage = message.getContent(SOAPMessage.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            soapMessage.writeTo(baos);
            message.getExchange().put("entireSoapMessage", baos.toString());
        } catch (SOAPException | IOException e) {
            throw new Fault(e);
        }
        finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}