package com.wazado.authstarter.domain.entity.user;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import org.apache.catalina.User;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@SuppressWarnings("unchecked")
public record UserPrinciple(
        Long userId,
        String username,
        String sid,
        List<String> roles,
        List<String> permissions
) {
    public UserPrinciple {
        sid = StringUtils.hasText(sid) ? sid : UUID.randomUUID().toString();
    }

    public static UserPrinciple of(Claims claims) {
        return UserPrinciple.builder()
                .userId(Long.valueOf(claims.getSubject()))
                .username(claims.get("username", String.class))
                .sid(claims.get("sid", String.class))
                .roles(claims.get("roles", List.class))
                .permissions(claims.get("permissions", List.class))
                .build();
    }
}
