package com.niolikon.taskboard.framework.security.keycloak;

import com.niolikon.taskboard.framework.exceptions.rest.client.ForbiddenRestException;
import com.niolikon.taskboard.framework.exceptions.rest.server.BadGatewayRestException;
import com.niolikon.taskboard.framework.security.dto.UserLoginRequest;
import com.niolikon.taskboard.framework.security.dto.UserLogoutRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenRefreshRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenView;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakRestClient;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KeycloakJwtAuthenticationServiceTest {

    private KeycloakJwtAuthenticationService keycloakJwtAuthenticationService;

    private static final KeycloakProperties keycloakProperties = KeycloakProperties.builder()
            .authServerUrl("http://keycloak.test")
            .realm("test-realm")
            .clientId("test-client")
            .clientSecret("test-secret")
            .build();

    private KeycloakRestClient keycloakRestClient;

    @BeforeEach
    void setUp() {
        keycloakRestClient = mock(KeycloakRestClient.class);
        keycloakJwtAuthenticationService = new KeycloakJwtAuthenticationService(keycloakProperties, keycloakRestClient);
    }

    @Test
    void givenValidLoginRequest_whenLogin_thenReturnsUserTokenView() {
        UserLoginRequest loginRequest = new UserLoginRequest("testuser", "password");
        KeycloakTokenResponse tokenResponse = new KeycloakTokenResponse();
        tokenResponse.setAccessToken("access-token");
        tokenResponse.setRefreshToken("refresh-token");

        ResponseEntity<KeycloakTokenResponse> responseEntity = new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(KeycloakTokenResponse.class)))
                .thenReturn(responseEntity);

        UserTokenView result = keycloakJwtAuthenticationService.login(loginRequest);

        assertThat(result).isNotNull()
                .extracting(
                        UserTokenView::getAccessToken,
                        UserTokenView::getRefreshToken)
                .containsExactly(
                        "access-token",
                        "refresh-token");
    }

    @Test
    void givenInvalidCredentials_whenLogin_thenThrowsForbiddenRestException() {
        UserLoginRequest loginRequest = new UserLoginRequest("wronguser", "wrongpassword");
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(KeycloakTokenResponse.class)))
                .thenThrow(new ForbiddenRestException("Invalid credentials"));

        assertThatThrownBy(() -> keycloakJwtAuthenticationService.login(loginRequest))
                .isInstanceOf(ForbiddenRestException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void givenKeycloakError_whenLogin_thenThrowsBadGatewayRestException() {
        UserLoginRequest loginRequest = new UserLoginRequest("testuser", "password");
        ResponseEntity<KeycloakTokenResponse> errorResponse = new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(KeycloakTokenResponse.class)))
                .thenReturn(errorResponse);

        assertThatThrownBy(() -> keycloakJwtAuthenticationService.login(loginRequest))
                .isInstanceOf(BadGatewayRestException.class)
                .hasMessage("Authentication request failed");
    }

    @Test
    void givenValidRefreshRequest_whenRefreshToken_thenReturnsNewUserTokenView() {
        UserTokenRefreshRequest refreshRequest = new UserTokenRefreshRequest("refresh-token");
        KeycloakTokenResponse tokenResponse = new KeycloakTokenResponse();
        tokenResponse.setAccessToken("new-access-token");
        tokenResponse.setRefreshToken("new-refresh-token");

        ResponseEntity<KeycloakTokenResponse> responseEntity = new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(KeycloakTokenResponse.class)))
                .thenReturn(responseEntity);

        UserTokenView result = keycloakJwtAuthenticationService.refreshToken(refreshRequest);

        assertThat(result).isNotNull()
                .extracting(
                        UserTokenView::getAccessToken,
                        UserTokenView::getRefreshToken)
                .containsExactly(
                        "new-access-token",
                        "new-refresh-token");
    }

    @Test
    void givenInvalidRefreshToken_whenRefreshToken_thenThrowsForbiddenRestException() {
        UserTokenRefreshRequest refreshRequest = new UserTokenRefreshRequest("invalid-refresh-token");
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(KeycloakTokenResponse.class)))
                .thenThrow(new ForbiddenRestException("Invalid refresh token"));

        assertThatThrownBy(() -> keycloakJwtAuthenticationService.refreshToken(refreshRequest))
                .isInstanceOf(ForbiddenRestException.class)
                .hasMessage("Invalid refresh token");
    }

    @Test
    void givenValidLogoutRequest_whenLogout_thenCompletesSuccessfully() {
        UserLogoutRequest logoutRequest = new UserLogoutRequest("refresh-token");
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(Void.class)))
                .thenReturn(responseEntity);

        keycloakJwtAuthenticationService.logout(logoutRequest);

        verify(keycloakRestClient, times(1)).postToKeycloak(any(), any(), eq(Void.class));
    }

    @Test
    void givenKeycloakError_whenLogout_thenThrowsBadGatewayRestException() {
        UserLogoutRequest logoutRequest = new UserLogoutRequest("refresh-token");
        ResponseEntity<Void> errorResponse = new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        when(keycloakRestClient.postToKeycloak(any(), any(), eq(Void.class)))
                .thenReturn(errorResponse);

        assertThatThrownBy(() -> keycloakJwtAuthenticationService.logout(logoutRequest))
                .isInstanceOf(BadGatewayRestException.class)
                .hasMessage("Logout request failed");
    }
}
