package com.wazado.authstarter.domain.service.jwt.provider;

import com.wazado.authstarter.domain.entity.user.UserPrinciple;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtProvider {
    String generateAccessToken(UserPrinciple userPrinciple);
    String generateRefreshToken(Long userId, String sid);
    UserPrinciple parseToken(HttpServletRequest request);
}
