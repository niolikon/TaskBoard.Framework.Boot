package com.niolikon.taskboard.framework.test.extensions;

import com.niolikon.taskboard.framework.test.annotations.WithIsolatedKeycloakTestScenario;
import org.junit.jupiter.api.extension.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;

public class IsolatedKeycloakTestScenarioExtension implements BeforeEachCallback, AfterEachCallback {

    private Keycloak keycloakAdmin;

    @Override
    public void beforeEach(ExtensionContext context) {
        keycloakAdmin = SpringExtension.getApplicationContext(context).getBean(Keycloak.class);

        context.getTestMethod().ifPresent(method -> {
            WithIsolatedKeycloakTestScenario annotation = method.getAnnotation(WithIsolatedKeycloakTestScenario.class);
            if (annotation != null) {
                try {
                    Class<?> dataClass = annotation.dataClass();
                    String methodName = annotation.methodName();

                    Method datasetMethod = dataClass.getDeclaredMethod(methodName);
                    RealmRepresentation realm = (RealmRepresentation) datasetMethod.invoke(null);

                    keycloakAdmin.realms().create(realm);
                } catch (Exception e) {
                    throw new RuntimeException("Error loading Keycloak realm for test: " + method.getName(), e);
                }
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
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

