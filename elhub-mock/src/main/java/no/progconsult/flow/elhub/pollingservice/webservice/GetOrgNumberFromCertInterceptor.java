package no.progconsult.flow.elhub.pollingservice.webservice;

import no.elhub.emif.wsdl.common.v1.CodeGroupType;
import no.elhub.emif.wsdl.common.v1.ElhubSOAPFaultType;
import no.elhub.emif.wsdl.marketprocesses.v1.ElhubSOAPFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JInInterceptor;
import org.apache.wss4j.dom.WSSecurityEngineResult;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.wss4j.dom.handler.WSHandlerResult;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.security.cert.X509Certificate;
import java.util.List;

import static java.time.ZonedDateTime.now;
import static no.progconsult.flow.elhub.pollingservice.common.DateUtils.zonedDateTimeToXmlGregorian;

/**
 * This class gets the certificate from the ws-security header in an incoming soap request.
 * The norwegian orgno is set as an header for use by camel from the public key metadata.
 */
public class GetOrgNumberFromCertInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    //Object identifier for a serial number.
    private static final String SERIAL_NUMBER_OID = "2.5.4.5";

    public static final String SIGN_ORG_NR_HEADER = "signatureOrgNumber";

    public GetOrgNumberFromCertInterceptor() {
        super(Phase.PRE_PROTOCOL);
        addAfter(PolicyBasedWSS4JInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
        List<WSHandlerResult> results = CastUtils.cast((List<?>) soapMessage
                .get(WSHandlerConstants.RECV_RESULTS));
        for (WSHandlerResult handlerResult : results) {
            for (WSSecurityEngineResult engineResult : handlerResult.getResults()) {
                if (engineResult.containsKey(WSSecurityEngineResult.TAG_X509_CERTIFICATE)) {
                    X509Certificate x509Certificate = (X509Certificate) engineResult
                            .get(WSSecurityEngineResult.TAG_X509_CERTIFICATE);
                    soapMessage.getExchange().put(SIGN_ORG_NR_HEADER, getOrgNumber(x509Certificate));
                    return;
                }
            }
        }
        throw soapFaultMissingOrgno();
    }

    private Fault soapFaultMissingOrgno() {
        String errorText = "Organization number not found";
        ElhubSOAPFaultType elhubSOAPFault = new ElhubSOAPFaultType();
        elhubSOAPFault.setCodeGroup(CodeGroupType.SECURITY);
        elhubSOAPFault.setDescription(errorText);
        elhubSOAPFault.setExceptionDateTime(zonedDateTimeToXmlGregorian(now()));
        return new Fault(new ElhubSOAPFault(errorText, elhubSOAPFault));
    }

    /**
     * Get the org number from a X509 enterprise cert from buypass or comfides.
     * These certs contains the norwegian orgno in the serial number field for the subject.
     */
    String getOrgNumber(X509Certificate x509Certificate) {
        try {
            LdapName metadata = new LdapName(x509Certificate.getSubjectX500Principal().getName());
            Rdn orgNo = metadata.getRdns()
                                .stream()
                                .filter(rdn -> rdn.getType().equals(SERIAL_NUMBER_OID))
                                .findFirst()
                                .orElseThrow(this::soapFaultMissingOrgno);
            return new String((byte[]) orgNo.getValue()).trim().replaceAll(" ", "");
        } catch (InvalidNameException ignored) {
            throw soapFaultMissingOrgno();
        }
    }
}
