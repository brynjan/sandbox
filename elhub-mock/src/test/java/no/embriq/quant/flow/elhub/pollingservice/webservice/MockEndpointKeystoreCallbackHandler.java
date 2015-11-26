package no.embriq.quant.flow.elhub.pollingservice.webservice;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The elhub mock endpoint needs this class to sign the response to our testclient.
 */
public class MockEndpointKeystoreCallbackHandler implements CallbackHandler {

    private Map<String, String> passwords = new HashMap<>();

    public MockEndpointKeystoreCallbackHandler() {
        passwords.put("mockserveralias", "changeit");
        passwords.put("clientalias", "changeit");
    }

    public MockEndpointKeystoreCallbackHandler(String keyAlias, String jksPassword) {
        passwords.put(keyAlias, jksPassword);
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }
    }
}