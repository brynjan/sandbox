package no.embriq.quant.flow.elhub.pollingservice.webservice;


import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.IOException;
import java.io.InputStream;

/**
 * We need to log the entire incoming soap envelope to the auditlog.
 */
public class AddIncomingSoapMessageToHeaderInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
    public AddIncomingSoapMessageToHeaderInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        InputStream is = null;
        CachedOutputStream os = null;
        try {
            // now get the request xml
            is = message.getContent(InputStream.class);
            os = new CachedOutputStream();
            IOUtils.copy(is, os);
            message.setContent(InputStream.class, os.getInputStream());
            message.getExchange().put("entireSoapMessage", IOUtils.toString(os.getInputStream()));
        } catch (Exception ex) {
            throw new Fault(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}