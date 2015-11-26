package no.embriq.quant.flow.elhub.pollingservice.webservice;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import javax.xml.ws.BindingProvider;
import java.util.Map;
import java.util.Properties;

/**
 * Class when testing with signed cxf client.
 *
 * @author bno
 */
public class CxfTestClient {

    private final String keystoreType;

    private final String keystorePwd;

    private final String keystoreAlias;

    private final String keystoreFile;

    private final String truststoreFile;

    private final String truststorePwd;

    private CxfTestClient(ClientBuilder clientBuilder) {
        this.keystoreType = clientBuilder.keystoreType;
        this.keystorePwd = clientBuilder.keystorePwd;
        this.keystoreAlias = clientBuilder.keystoreAlias;
        this.keystoreFile = clientBuilder.keystoreFile;
        this.truststoreFile = clientBuilder.truststoreFile;
        this.truststorePwd = clientBuilder.truststorePwd;
    }


    //---------------------------------------------------------------------------------------------------------

    public <T> void initialize(String url, T port) {
        Map<String, Object> requestCtx = ((BindingProvider) port).getRequestContext();
        requestCtx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());

        //SECURITY
        requestCtx.put("ws-security.callback-handler",
                new MockEndpointKeystoreCallbackHandler(keystoreAlias, keystorePwd));
        requestCtx.put("ws-security.signature.properties", createWsSecurityProperties());
    }

    private Properties createWsSecurityProperties() {
        Properties props = new Properties();
        //Keystore
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type",
                keystoreType);
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password",
                keystorePwd);
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias",
                keystoreAlias);
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.file",
                keystoreFile);
        //Truststore
        props.setProperty("org.apache.ws.security.crypto.merlin.truststore.file",
                truststoreFile);
        props.setProperty("org.apache.ws.security.crypto.merlin.truststore.password",
                truststorePwd);
        return props;
    }

    /**
     * Builder class for construction of CxfTestClient.
     * Call build to create a instance.
     */
    public static class ClientBuilder {
        /**
         * Default certificate values.
         */
        private String keystoreType = "jks";

        private String keystorePwd = "changeit";

        private String keystoreAlias = "clientalias";

        private String keystoreFile = "elhub-sign.jks";

        private String truststoreFile = "elhub-trust.jks";

        private String truststorePwd = "changeit";

        protected ClientBuilder keystoreType(String keystoreType) {
            this.keystoreType = keystoreType;
            return this;
        }

        protected ClientBuilder keystorePwd(String keystorePwd) {
            this.keystorePwd = keystorePwd;
            return this;
        }

        protected ClientBuilder keystoreAlias(String keystoreAlias) {
            this.keystoreAlias = keystoreAlias;
            return this;
        }

        protected ClientBuilder keystoreFile(String keystoreFile) {
            this.keystoreFile = keystoreFile;
            return this;
        }

        protected ClientBuilder truststoreFile(String truststoreFile) {
            this.truststoreFile = truststoreFile;
            return this;
        }

        protected ClientBuilder truststorePwd(String truststorePwd) {
            this.truststorePwd = truststorePwd;
            return this;
        }

        /**
         * Create a instance of the CxfTestClient
         *
         * @return
         */
        public CxfTestClient build() {
            return new CxfTestClient(this);
        }
    }
}
