package com.niolikon.taskboard.framework.security.keycloak;

import com.niolikon.taskboard.framework.exceptions.rest.server.BadGatewayRestException;
import com.niolikon.taskboard.framework.exceptions.rest.client.ForbiddenRestException;
import com.niolikon.taskboard.framework.security.JwtAuthenticationService;
import com.niolikon.taskboard.framework.security.dto.UserLoginRequest;
import com.niolikon.taskboard.framework.security.dto.UserLogoutRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenRefreshRequest;
import com.niolikon.taskboard.framework.security.dto.UserTokenView;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakRequestEntityBuilder;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakRestClient;
import com.niolikon.taskboard.framework.security.keycloak.client.KeycloakTokenResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Log
public class KeycloakJwtAuthenticationService implements JwtAuthenticationService {
    private static final String REQUEST_FORMED_FORMAT = "Request formed: %s";
    private static final String CONNECTING_TO_FORMAT = "Connecting to: %s";
    private static final String RESPONSE_RECEIVED_FORMAT = "Response received: %s";

    private final KeycloakProperties keycloakProperties;
    private final KeycloakRestClient keycloakRestClient;

    public KeycloakJwtAuthenticationService(KeycloakProperties keycloakProperties, KeycloakRestClient keycloakRestClient) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakRestClient = keycloakRestClient;
    }

    @Override
    public UserTokenView login(UserLoginRequest userLoginRequest) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = KeycloakRequestEntityBuilder.builder()
                .withClientId(keycloakProperties.getClientId())
                .withClientSecret(keycloakProperties.getClientSecret())
                .withUsername(userLoginRequest.getUsername())
                .withPassword(userLoginRequest.getPassword())
                .withPasswordGrantType()
                .build();
        log.finer(String.format(REQUEST_FORMED_FORMAT, requestEntity));

        String keycloakLoginUrl = buildKeycloakLoginUrl(keycloakProperties);
        log.finer(String.format(CONNECTING_TO_FORMAT, keycloakLoginUrl));

        ResponseEntity<KeycloakTokenResponse> responseEntity;
        try {
            responseEntity = keycloakRestClient.postToKeycloak(
                    keycloakLoginUrl,
                    requestEntity,
                    KeycloakTokenResponse.class
            );
        }
        catch(HttpClientErrorException errorException) {
            throw new ForbiddenRestException(errorException.getMessage());
        }
        log.finer(String.format(RESPONSE_RECEIVED_FORMAT, responseEntity));

        if (! responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BadGatewayRestException("Authentication request failed");
        }
        UserTokenView userTokenView = new UserTokenView();
        userTokenView.setAccessToken(responseEntity.getBody().getAccessToken());
        userTokenView.setRefreshToken(responseEntity.getBody().getRefreshToken());

        return userTokenView;
    }

    @Override
    public UserTokenView refreshToken(UserTokenRefreshRequest userTokenRefreshRequest) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = KeycloakRequestEntityBuilder.builder()
                .withClientId(keycloakProperties.getClientId())
                .withClientSecret(keycloakProperties.getClientSecret())
                .withRefreshToken(userTokenRefreshRequest.getRefreshToken())
                .withRefreshTokenGrantType()
                .build();
        log.finer(String.format(REQUEST_FORMED_FORMAT, requestEntity));

        String keycloakLoginUrl = buildKeycloakLoginUrl(keycloakProperties);
        log.finer(String.format(CONNECTING_TO_FORMAT, keycloakLoginUrl));

        ResponseEntity<KeycloakTokenResponse> responseEntity;
        try {
            responseEntity = keycloakRestClient.postToKeycloak(
                    keycloakLoginUrl,
                    requestEntity,
                    KeycloakTokenResponse.class
            );
        }
        catch(HttpClientErrorException errorException) {
            throw new ForbiddenRestException(errorException.getMessage());
        }
        log.finer(String.format(RESPONSE_RECEIVED_FORMAT, responseEntity));

        if (! responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BadGatewayRestException("Authentication request failed");
        }
        UserTokenView userTokenView = new UserTokenView();
        userTokenView.setAccessToken(responseEntity.getBody().getAccessToken());
        userTokenView.setRefreshToken(responseEntity.getBody().getRefreshToken());

        return userTokenView;
    }

    @Override
    public void logout(UserLogoutRequest userLogoutRequest) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = KeycloakRequestEntityBuilder.builder()
                .withClientId(keycloakProperties.getClientId())
                .withClientSecret(keycloakProperties.getClientSecret())
                .withRefreshToken(userLogoutRequest.getRefreshToken())
                .withRefreshTokenGrantType()
                .build();
        log.finer(String.format(REQUEST_FORMED_FORMAT, requestEntity));

        String keycloakLogoutUrl = buildKeycloakLogoutUrl(keycloakProperties);
        log.finer(String.format(CONNECTING_TO_FORMAT, keycloakLogoutUrl));

        ResponseEntity<Void> responseEntity;
        try {
            responseEntity = keycloakRestClient.postToKeycloak(
                    keycloakLogoutUrl,
                    requestEntity,
                    Void.class
            );
        }
        catch(HttpClientErrorException errorException) {
            throw new ForbiddenRestException(errorException.getMessage());
        }
        log.finer(String.format(RESPONSE_RECEIVED_FORMAT, responseEntity));

        if (! responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BadGatewayRestException("Logout request failed");
        }
    }

    String buildKeycloakLoginUrl(KeycloakProperties keycloakProperties) {
        return UriComponentsBuilder.fromUriString(keycloakProperties.getAuthServerUrl())
                .pathSegment("realms", keycloakProperties.getRealm(), "protocol", "openid-connect", "token")
                .toUriString();
    }

    String buildKeycloakLogoutUrl(KeycloakProperties keycloakProperties) {
        return UriComponentsBuilder.fromUriString(keycloakProperties.getAuthServerUrl())
                .pathSegment("realms", keycloakProperties.getRealm(), "protocol", "openid-connect", "logout")
                .toUriString();
    }
}
