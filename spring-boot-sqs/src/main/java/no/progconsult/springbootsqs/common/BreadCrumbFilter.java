package no.progconsult.springbootsqs.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static no.embriq.quant.flow.common.config.Constants.BREADCRUMB_ID;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2018-11-15.
 */
@Component
@Order(-101)//Note: Spring Security’s DelegateFilterProxy filter order is set at the value -100
public class BreadCrumbFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            final String breadCrumbid = Optional.ofNullable(httpServletRequest.getHeader(BREADCRUMB_ID)).orElse("ID-" + UUID.randomUUID().toString());
            MDC.put(BREADCRUMB_ID, breadCrumbid);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
