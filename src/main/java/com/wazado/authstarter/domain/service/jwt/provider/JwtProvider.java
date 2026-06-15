package com.wazado.authstarter.domain.service.jwt.provider;

import com.wazado.authstarter.domain.entity.user.UserPrinciple;

public interface JwtProvider {
    String generateToken(UserPrinciple userPrinciple);
    UserPrinciple parseToken();
}
