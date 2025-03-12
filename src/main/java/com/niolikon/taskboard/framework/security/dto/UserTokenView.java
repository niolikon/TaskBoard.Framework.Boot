package com.niolikon.taskboard.framework.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTokenView {
    @JsonProperty("AccessToken")
    private String accessToken;

    @JsonProperty("RefreshToken")
    private String refreshToken;
}