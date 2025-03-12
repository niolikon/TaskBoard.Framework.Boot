package com.niolikon.taskboard.framework.security.keycloak;

import com.niolikon.taskboard.framework.exceptions.rest.client.ForbiddenRestException;
import com.niolikon.taskboard.framework.security.config.FrameworkSecurityConfig;
import com.niolikon.taskboard.framework.security.dto.UserLoginRequest;
import com.niolikon.taskboard.framework.security.dto.UserLogoutRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenRefreshRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenView;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakRestClient;
import com.niolikon.taskboard.framework.test.containers.KeycloakTestContainersConfig;
import com.niolikon.taskboard.framework.test.extensions.IsolatedKeycloakTestScenarioExtension;
import com.niolikon.taskboard.framework.test.annotations.WithIsolatedKeycloakTestScenario;
import com.niolikon.taskboard.framework.security.scenarios.RealmWithSingleUserTestScenario;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {FrameworkSecurityConfig.class, KeycloakTestContainersConfig.class})
@ExtendWith(IsolatedKeycloakTestScenarioExtension.class)
class KeycloakJwtAuthenticationServiceIT {
    @Autowired
    private KeycloakContainer keycloakContainer;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithSingleUserTestScenario.class)
    void givenDefaultRealm_whenLogin_thenReturnTokenView() {
        KeycloakJwtAuthenticationService keycloakJwtAuthenticationService = getKeycloakJwtAuthenticationService(
                RealmWithSingleUserTestScenario.getRealm()
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithSingleUserTestScenario.USER_USERNAME,
                RealmWithSingleUserTestScenario.USER_PASSWORD
        );
        UserTokenView result = keycloakJwtAuthenticationService.login(userLoginRequest);

        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();
    }

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithSingleUserTestScenario.class)
    void givenDefaultRealm_whenLoginWithInvalidCredentials_thenForbiddenRestExceptionIsThrown() {
        KeycloakJwtAuthenticationService keycloakJwtAuthenticationService = getKeycloakJwtAuthenticationService(
                RealmWithSingleUserTestScenario.getRealm()
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithSingleUserTestScenario.USER_USERNAME,
                "A-WRONG-PASSWORD"
        );

        assertThatThrownBy(() -> keycloakJwtAuthenticationService.login(userLoginRequest))
                .isInstanceOf(ForbiddenRestException.class);
    }

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithSingleUserTestScenario.class)
    void givenDefaultRealm_whenRefreshToken_thenReturnTokenView() {
        KeycloakJwtAuthenticationService keycloakJwtAuthenticationService = getKeycloakJwtAuthenticationService(
                RealmWithSingleUserTestScenario.getRealm()
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithSingleUserTestScenario.USER_USERNAME,
                RealmWithSingleUserTestScenario.USER_PASSWORD
        );
        UserTokenView loginResult = keycloakJwtAuthenticationService.login(userLoginRequest);

        UserTokenRefreshRequest userTokenRefreshRequest = new UserTokenRefreshRequest(
                loginResult.getRefreshToken()
        );
        UserTokenView result = keycloakJwtAuthenticationService.refreshToken(userTokenRefreshRequest);

        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();
    }

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithSingleUserTestScenario.class)
    void givenDefaultRealm_whenLogout_thenReturnTokenView() {
        KeycloakJwtAuthenticationService keycloakJwtAuthenticationService = getKeycloakJwtAuthenticationService(
                RealmWithSingleUserTestScenario.getRealm()
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithSingleUserTestScenario.USER_USERNAME,
                RealmWithSingleUserTestScenario.USER_PASSWORD
        );
        UserTokenView loginResult = keycloakJwtAuthenticationService.login(userLoginRequest);

        UserLogoutRequest userLogoutRequest = new UserLogoutRequest(
                loginResult.getRefreshToken()
        );

        assertThatCode(() -> keycloakJwtAuthenticationService.logout(userLogoutRequest))
                .doesNotThrowAnyException();
    }

    private KeycloakJwtAuthenticationService getKeycloakJwtAuthenticationService(RealmRepresentation realm) {
        KeycloakProperties keycloakProperties = new KeycloakProperties();
        keycloakProperties.setAuthServerUrl(keycloakContainer.getAuthServerUrl());
        keycloakProperties.setRealm(realm.getRealm());

        String clientId = realm.getClients().get(0).getClientId();
        String clientSecret = realm.getClients().get(0).getSecret();

        keycloakProperties.setClientId(clientId);
        keycloakProperties.setClientSecret(clientSecret);

        KeycloakRestClient keycloakRestClient = new KeycloakRestClient(restTemplate);
        return new KeycloakJwtAuthenticationService(keycloakProperties, keycloakRestClient);
    }
}
