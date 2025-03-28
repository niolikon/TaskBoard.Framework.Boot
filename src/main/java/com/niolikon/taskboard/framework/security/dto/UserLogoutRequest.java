package com.niolikon.taskboard.framework.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLogoutRequest {
    @NotEmpty
    @Size(min = 10)
    @JsonProperty("RefreshToken")
    private String refreshToken;
}
