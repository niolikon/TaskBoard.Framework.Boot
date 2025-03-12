package com.niolikon.taskboard.framework.security.keycloak.client;

import com.niolikon.taskboard.framework.exceptions.rest.server.BadGatewayRestException;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log
public class KeycloakRestClient {

    private final RestTemplate restTemplate;

    public KeycloakRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    }

    public <T> ResponseEntity<T> postToKeycloak(String requestUrl, HttpEntity<MultiValueMap<String, String>> requestEntity, Class<T> responseType) {
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(
                requestUrl,
                requestEntity,
                responseType
        );
        log.finer(String.format("Response received: %s", responseEntity));

        if (! responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BadGatewayRestException("Keycloak request failed");
        }

        return responseEntity;
    }
}
