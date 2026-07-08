package com.wazado.authstarter.application.usecase.jwt.provider;

import com.wazado.authstarter.domain.entity.user.UserPrinciple;
import com.wazado.authstarter.domain.service.jwt.provider.JwtProvider;
import com.wazado.authstarter.infrastructure.bootstrap.properties.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProviderUseCase implements JwtProvider {
    AuthProperties properties;

    @Override
    public String generateAccessToken(UserPrinciple userPrinciple) {
        return Jwts.builder()
                .subject(String.valueOf(userPrinciple.userId()))
                .claim("username", userPrinciple.username())
                .claim("roles", userPrinciple.roles())
                .claim("permissions", userPrinciple.permissions())
                .claim("sid", userPrinciple.sid())
                .claim("type", "ACCESS")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(properties.getAccessExpiration())))
                .signWith(generateKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(Long userId, String sid) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("sid", sid)
                .claim("type", "REFRESH")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(properties.getRefreshExpiration())))
                .signWith(generateKey())
                .compact();
    }

    @Override
    public UserPrinciple parseToken(HttpServletRequest request) {
        try {
            Optional<String> token = getTokenFromHeader(request);
            if(token.isEmpty()) {
                return null;
            }
            Claims claims = Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(token.get())
                    .getPayload();
            return UserPrinciple.of(claims);
        } catch (JwtException e) {
            log.error("Parse token error: {}", e.getMessage(), e.getCause());
            throw new RuntimeException("Thông tin xác thực không hợp lệ");
        } catch (IllegalArgumentException e) {
            log.error("Parse token error: {}", e.getMessage(), e.getCause());
            throw new RuntimeException("Vui lòng bổ sung thông tin xác thực");
        }
    }

    @Override
    public UserPrinciple parseToken(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            return UserPrinciple.of(claims);
        } catch (JwtException e) {
            log.error("Parse token error: {}", e.getMessage(), e.getCause());
            throw new RuntimeException("Thông tin xác thực không hợp lệ");
        } catch (IllegalArgumentException e) {
            log.error("Parse token error: {}", e.getMessage(), e.getCause());
            throw new RuntimeException("Vui lòng bổ sung thông tin xác thực");
        }
    }

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(properties.getSecretKey().getBytes());
    }

    private Optional<String> getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return Optional.ofNullable(bearerToken)
                .filter(b -> b.contains("Bearer"))
                .map(b -> b.substring("Bearer".length() + 1));
    }
}
