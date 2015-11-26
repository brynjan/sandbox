package no.embriq.quant.flow.elhub.pollingservice.webservice;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.assertj.core.api.Assertions.assertThat;

public class GetOrgNumberFromCertInterceptorTest {

    @Test
    //The cert used in the test expires on 03 oct 2022.
    public void getOrgNo() throws CertificateException {
        InputStream certInputStream = this.getClass().getClassLoader().getResourceAsStream("test_cert.cer");
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certInputStream);

        GetOrgNumberFromCertInterceptor interceptor = new GetOrgNumberFromCertInterceptor();
        assertThat(interceptor.getOrgNumber(certificate)).isEqualTo("958935420");
    }
}