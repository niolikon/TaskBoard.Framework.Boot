package com.niolikon.taskboard.framework.test.extensions;

import com.niolikon.taskboard.framework.security.keycloak.KeycloakProperties;
import com.niolikon.taskboard.framework.test.annotations.WithIsolatedKeycloakTestScenario;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.extension.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.BeansException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;

public class IsolatedKeycloakTestScenarioExtension implements BeforeEachCallback, AfterEachCallback {

    private Keycloak keycloakAdmin;

    private KeycloakProperties keycloakProperties;
    private String beanAuthServerUrl;
    private String beanRealm;
    private String beanClientId;
    private String beanClientSecret;

    @Override
    public void beforeEach(ExtensionContext context) {
        KeycloakContainer keycloakContainer = SpringExtension.getApplicationContext(context).getBean(KeycloakContainer.class);
        keycloakAdmin = SpringExtension.getApplicationContext(context).getBean(Keycloak.class);

        try {
            keycloakProperties = SpringExtension.getApplicationContext(context).getBean(KeycloakProperties.class);
            beanAuthServerUrl = keycloakProperties.getAuthServerUrl();
            beanRealm = keycloakProperties.getRealm();
            beanClientId = keycloakProperties.getClientId();
            beanClientSecret = keycloakProperties.getClientSecret();
        }
        catch (BeansException beansException) {
            keycloakProperties = null;
        }

        context.getTestMethod().ifPresent(method -> {
            WithIsolatedKeycloakTestScenario annotation = method.getAnnotation(WithIsolatedKeycloakTestScenario.class);
            if (annotation != null) {
                try {
                    Class<?> dataClass = annotation.dataClass();
                    String methodName = annotation.methodName();

                    Method datasetMethod = dataClass.getDeclaredMethod(methodName);
                    RealmRepresentation realm = (RealmRepresentation) datasetMethod.invoke(null);

                    if (keycloakProperties != null) {
                        if (! realm.getClients().isEmpty()) {
                            keycloakProperties.setClientId(realm.getClients().get(0).getClientId());
                            keycloakProperties.setClientSecret(realm.getClients().get(0).getSecret());
                        }

                        keycloakProperties.setAuthServerUrl(keycloakContainer.getAuthServerUrl());
                        keycloakProperties.setRealm(realm.getRealm());
                    }

                    keycloakAdmin.realms().create(realm);
                } catch (Exception e) {
                    throw new RuntimeException("Error loading Keycloak realm for test: " + method.getName(), e);
                }
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (keycloakProperties != null) {
            keycloakProperties.setAuthServerUrl(beanAuthServerUrl);
            keycloakProperties.setRealm(beanRealm);
            keycloakProperties.setClientId(beanClientId);
            keycloakProperties.setClientSecret(beanClientSecret);
        }

        if (keycloakAdmin == null) {
            throw new IllegalStateException("KeycloakAdminClient was not initialized!");
        }

        context.getTestMethod().ifPresent(method -> {
            WithIsolatedKeycloakTestScenario annotation = method.getAnnotation(WithIsolatedKeycloakTestScenario.class);
            if (annotation != null) {
                try {
                    Class<?> dataClass = annotation.dataClass();
                    String methodName = annotation.methodName();

                    Method datasetMethod = dataClass.getDeclaredMethod(methodName);
                    RealmRepresentation realm = (RealmRepresentation) datasetMethod.invoke(null);

                    keycloakAdmin.realms().realm(realm.getRealm()).remove();
                } catch (Exception e) {
                    throw new RuntimeException("Error cleaning up Keycloak realm for test: " + method.getName(), e);
                }
            }
        });
    }
}

