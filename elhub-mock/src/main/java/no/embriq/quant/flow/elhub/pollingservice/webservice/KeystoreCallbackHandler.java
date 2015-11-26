package no.embriq.quant.flow.elhub.pollingservice.webservice;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * The Cxf wss implementation needs this stupid class to get the private key to sign the request.
 * We get the keyalias and keystorepassword from our properties.
 */
@Component
public class KeystoreCallbackHandler implements CallbackHandler {

    @Value("${elhub.sign.keystore.cert.alias}")
    private String keyAlias;

    @Value("${elhub.sign.keystore.password}")
    private String jksPassword;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
            if (pc.getIdentifier().equals(keyAlias)) {
                pc.setPassword(jksPassword);
            }
        }
    }
}