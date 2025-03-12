package com.niolikon.taskboard.framework.security;

import com.niolikon.taskboard.framework.security.dto.*;

public interface JwtAuthenticationService {
    UserTokenView login(UserLoginRequest userLoginRequest);

    UserTokenView refreshToken(UserTokenRefreshRequest userTokenRefreshRequest);

    void logout(UserLogoutRequest userLogoutRequest);
}
