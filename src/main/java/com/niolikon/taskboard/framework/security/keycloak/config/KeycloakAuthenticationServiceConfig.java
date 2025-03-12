package com.niolikon.taskboard.framework.security.keycloak.config;

import com.niolikon.taskboard.framework.security.JwtAuthenticationService;
import com.niolikon.taskboard.framework.security.keycloak.KeycloakJwtAuthenticationService;
import com.niolikon.taskboard.framework.security.keycloak.KeycloakProperties;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakRestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KeycloakAuthenticationServiceConfig {

    @Bean
    @ConditionalOnProperty(
            name = {
                    "taskboard.security.keycloak.auth-server-url",
                    "taskboard.security.keycloak.realm",
                    "taskboard.security.keycloak.client-id"
            }
    )
    public KeycloakProperties keycloakProperties(Environment environment) {
        return KeycloakProperties.builder()
                .authServerUrl(environment.getProperty("taskboard.security.keycloak.auth-server-url"))
                .realm(environment.getProperty("taskboard.security.keycloak.realm"))
                .clientId(environment.getProperty("taskboard.security.keycloak.client-id"))
                .clientSecret(environment.getProperty("taskboard.security.keycloak.client-secret"))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        return restTemplate;
    }

    @Bean
    @ConditionalOnBean(KeycloakProperties.class)
    public KeycloakRestClient keycloakRestClient(RestTemplate restTemplate) {
        return new KeycloakRestClient(restTemplate);
    }

    @Bean
    @ConditionalOnBean(KeycloakProperties.class)
    public JwtAuthenticationService keycloakAuthService(KeycloakProperties keycloakProperties, KeycloakRestClient keycloakRestClient) {
        return new KeycloakJwtAuthenticationService(keycloakProperties, keycloakRestClient);
    }
}
