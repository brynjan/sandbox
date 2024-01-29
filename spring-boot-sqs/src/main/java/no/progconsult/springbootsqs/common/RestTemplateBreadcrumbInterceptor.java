package no.progconsult.springbootsqs.common;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;


public class RestTemplateBreadcrumbInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final String breadcrumbId = MDC.get(BREADCRUMB_ID);
        if (breadcrumbId != null) {
            request.getHeaders().set(BREADCRUMB_ID, breadcrumbId);
        }
        return execution.execute(request, body);
    }
}