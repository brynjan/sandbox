package no.progconsult.maincontext.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author <a href="mailto:kgrodzicki@gmail.com">Krzysztof Grodzicki</a> 15/01/16.
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }
}
