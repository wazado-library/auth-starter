package com.wazado.authstarter.domain.service.jwt.provider;

import com.wazado.authstarter.domain.entity.user.UserPrinciple;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtProvider {
    String generateToken(UserPrinciple userPrinciple);
    UserPrinciple parseToken(HttpServletRequest request);
}
