package com.niolikon.taskboard.framework.test.annotations;

import java.lang.annotation.*;

/**
 * Marks a test method with an isolated Keycloak test scenario.
 * <p>
 * This annotation is used to specify a Keycloak realm configuration
 * that will be loaded into the Keycloak server before the test runs.
 * The configuration is defined within the specified {@code dataClass},
 * which must contain a static method that returns a {@code RealmRepresentation}.
 * </p>
 *
 * Example usage:
 * <pre>
 * {@code
 * @Test
 * @WithIsolatedKeycloakTestScenario(dataClass = SampleKeycloakTestScenario.class)
 * void givenTestRealm_whenCheckingIfExists_thenRealmIsPresent() {
 *     // Test logic here...
 * }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithIsolatedKeycloakTestScenario {

    /**
     * Specifies the class that contains the Keycloak realm configuration.
     * <p>
     * This class must define a static method that returns a {@code RealmRepresentation}
     * representing the test realm to be loaded into Keycloak before test execution.
     * </p>
     *
     * @return the class containing the realm configuration
     */
    Class<?> dataClass();

    /**
     * Specifies the name of the static method within {@code dataClass} that provides the Keycloak realm.
     * <p>
     * By default, this method is expected to be named {@code "getRealm"}.
     * If a different name is used, it must be explicitly specified.
     * </p>
     *
     * @return the method name returning the realm configuration (default: "getRealm")
     */
    String methodName() default "getRealm";
}
