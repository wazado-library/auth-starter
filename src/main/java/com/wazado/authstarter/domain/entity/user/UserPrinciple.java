package com.wazado.authstarter.domain.entity.user;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import org.apache.catalina.User;

import java.time.Instant;
import java.util.List;

@Builder
@SuppressWarnings("unchecked")
public record UserPrinciple(
        Long userId,
        String username,
        List<String> roles,
        List<String> permissions
) {
    public static UserPrinciple of(Claims claims) {
        return UserPrinciple.builder()
                .userId(Long.valueOf(claims.getSubject()))
                .username(claims.get("username", String.class))
                .roles(claims.get("roles", List.class))
                .permissions(claims.get("permissions", List.class))
                .build();
    }
}
