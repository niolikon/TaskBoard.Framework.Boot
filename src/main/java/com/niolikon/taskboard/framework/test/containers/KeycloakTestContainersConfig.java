package com.niolikon.taskboard.framework.test.containers;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.java.Log;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@Log
public class KeycloakTestContainersConfig {
    private static final String BOOTSTRAP_ADMIN_USER = "admin";
    private static final String BOOTSTRAP_ADMIN_PASSWORD = "admin";

    private static final KeycloakContainer KEYCLOAK = new KeycloakContainer()
            .withAdminUsername(BOOTSTRAP_ADMIN_USER)
            .withAdminPassword(BOOTSTRAP_ADMIN_PASSWORD);

    static {
        KEYCLOAK.start();
    }

    @Bean
    public KeycloakContainer keycloakContainer() {
        return KEYCLOAK;
    }

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK.getAuthServerUrl())
                .realm("master")
                .clientId("admin-cli")
                .username(BOOTSTRAP_ADMIN_USER)
                .password(BOOTSTRAP_ADMIN_PASSWORD)
                .build();
    }
}
