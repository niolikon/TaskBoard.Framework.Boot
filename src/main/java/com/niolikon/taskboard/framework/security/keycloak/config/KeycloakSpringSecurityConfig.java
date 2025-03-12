package com.niolikon.taskboard.framework.security.keycloak.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class KeycloakSpringSecurityConfig {

    @Bean
    @ConditionalOnClass(JwtAuthenticationConverter.class)
    @ConditionalOnProperty(
            name = {
                    "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri"
            },
            matchIfMissing = false
    )
    public JwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);

            Map<String, Object> claimsMap = jwt.getClaimAsMap("realm_access");
            if (claimsMap != null && claimsMap.containsKey("roles")) {
                Collection<GrantedAuthority> realmRoles = ((Collection<String>) claimsMap.get("roles"))
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                authorities.addAll(realmRoles);
            }

            return authorities;
        });

        return jwtAuthenticationConverter;
    }
}
