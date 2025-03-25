package com.niolikon.taskboard.framework.security.keycloak;

import com.niolikon.taskboard.framework.exceptions.rest.client.ForbiddenRestException;
import com.niolikon.taskboard.framework.security.JwtAuthenticationService;
import com.niolikon.taskboard.framework.security.config.FrameworkSecurityConfig;
import com.niolikon.taskboard.framework.security.dto.UserLoginRequest;
import com.niolikon.taskboard.framework.security.dto.UserLogoutRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenRefreshRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenView;
import com.niolikon.taskboard.framework.security.scenarios.RealmWithClientSecretTestScenario;
import com.niolikon.taskboard.framework.test.containers.KeycloakTestContainersConfig;
import com.niolikon.taskboard.framework.test.extensions.IsolatedKeycloakTestScenarioExtension;
import com.niolikon.taskboard.framework.test.annotations.WithIsolatedKeycloakTestScenario;
import com.niolikon.taskboard.framework.security.scenarios.RealmWithSingleUserTestScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {FrameworkSecurityConfig.class, KeycloakTestContainersConfig.class})
@ExtendWith(IsolatedKeycloakTestScenarioExtension.class)
class KeycloakJwtAuthenticationServiceIT {
    @Autowired
    private JwtAuthenticationService keycloakJwtAuthenticationService;

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithSingleUserTestScenario.class)
    void givenDefaultRealm_whenLogin_thenReturnTokenView() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithSingleUserTestScenario.USER_USERNAME,
                RealmWithSingleUserTestScenario.USER_PASSWORD
        );
        UserTokenView result = keycloakJwtAuthenticationService.login(userLoginRequest);

        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();
    }

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithClientSecretTestScenario.class)
    void givenSecuredRealm_whenLogin_thenReturnTokenView() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithClientSecretTestScenario.USER_USERNAME,
                RealmWithClientSecretTestScenario.USER_PASSWORD
        );
        UserTokenView result = keycloakJwtAuthenticationService.login(userLoginRequest);

        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();
    }

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithSingleUserTestScenario.class)
    void givenDefaultRealm_whenLoginWithInvalidCredentials_thenForbiddenRestExceptionIsThrown() {
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
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithClientSecretTestScenario.class)
    void givenSecuredRealm_whenRefreshToken_thenReturnTokenView() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithClientSecretTestScenario.USER_USERNAME,
                RealmWithClientSecretTestScenario.USER_PASSWORD
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

    @Test
    @WithIsolatedKeycloakTestScenario(dataClass = RealmWithClientSecretTestScenario.class)
    void givenSecuredRealm_whenLogout_thenReturnTokenView() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                RealmWithClientSecretTestScenario.USER_USERNAME,
                RealmWithClientSecretTestScenario.USER_PASSWORD
        );
        UserTokenView loginResult = keycloakJwtAuthenticationService.login(userLoginRequest);

        UserLogoutRequest userLogoutRequest = new UserLogoutRequest(
                loginResult.getRefreshToken()
        );

        assertThatCode(() -> keycloakJwtAuthenticationService.logout(userLogoutRequest))
                .doesNotThrowAnyException();
    }
}
