package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * @author Josh Cummings
 */
@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
		http
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
//					.antMatchers(HttpMethod.GET, "/message/**").hasAuthority("SCOPE_message:read")
//					.antMatchers(HttpMethod.POST, "/message/**").hasAuthority("SCOPE_message:write")
					.anyRequest().authenticated()
			)
//			.oauth2ResourceServer(oauth2ResourceServer ->
//				oauth2ResourceServer
//					.opaqueToken(opaqueToken ->
//						opaqueToken
//							.introspectionUri(this.introspectionUri)
//							.introspectionClientCredentials(this.clientId, this.clientSecret)
//					)
//			);
					.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);


//			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		// @formatter:on
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }
}
