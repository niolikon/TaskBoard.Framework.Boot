package com.niolikon.taskboard.framework.security.keycloak.client;

import com.niolikon.taskboard.framework.exceptions.rest.server.BadGatewayRestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class KeycloakRestClientTest {

    private KeycloakRestClient keycloakRestClient;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        keycloakRestClient = new KeycloakRestClient(restTemplate);
    }

    @Test
    void givenValidRequest_whenPostToKeycloak_thenReturnsSuccessfulResponse() {
        String requestUrl = "http://keycloak.test/auth";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, new HttpHeaders());

        ResponseEntity<String> successResponse = new ResponseEntity<>("OK", HttpStatus.OK);
        when(restTemplate.postForEntity(eq(requestUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(successResponse);

        ResponseEntity<String> response = keycloakRestClient.postToKeycloak(requestUrl, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("OK");
    }

    @Test
    void givenInvalidRequest_whenPostToKeycloak_thenThrowsBadGatewayException() {
        String requestUrl = "http://keycloak.test/auth";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, new HttpHeaders());

        ResponseEntity<String> errorResponse = new ResponseEntity<>("Error", HttpStatus.BAD_GATEWAY);
        when(restTemplate.postForEntity(eq(requestUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(errorResponse);

        assertThatThrownBy(() -> keycloakRestClient.postToKeycloak(requestUrl, requestEntity, String.class))
                .isInstanceOf(BadGatewayRestException.class)
                .hasMessage("Keycloak request failed");
    }
}
