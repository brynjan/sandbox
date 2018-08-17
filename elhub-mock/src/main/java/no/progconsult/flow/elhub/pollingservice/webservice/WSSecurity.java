package no.progconsult.flow.elhub.pollingservice.webservice;

import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * Delegate for WS Security
 *
 * @author bno
 */
public class WSSecurity {

    public Properties createWsSecurityProperties(Environment environment) {
        Properties props = new Properties();
        //Keystore
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type",
                environment.getRequiredProperty("elhub.sign.keystore.type"));
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password",
                environment.getRequiredProperty("elhub.sign.keystore.password"));
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias",
                environment.getRequiredProperty("elhub.sign.keystore.cert.alias"));
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.file",
                environment.getRequiredProperty("elhub.sign.keystore.location"));
        //Truststore
        props.setProperty("org.apache.ws.security.crypto.merlin.truststore.file",
                environment.getRequiredProperty("elhub.sign.truststore.location"));
        props.setProperty("org.apache.ws.security.crypto.merlin.truststore.password",
                environment.getRequiredProperty("elhub.sign.truststore.password"));

        return props;
    }
}
