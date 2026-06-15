package com.wazado.authstarter.domain.filter;

import com.wazado.authstarter.domain.entity.user.UserPrinciple;
import com.wazado.authstarter.domain.service.jwt.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<UserPrinciple> userPrinciple = Optional.ofNullable(jwtProvider.parseToken(request));
        if(userPrinciple.isPresent()) {
            UserPrinciple user = userPrinciple.get();
            List<SimpleGrantedAuthority> authorities = getGrantedAuthorities(user);
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(UserPrinciple userPrinciple) {
        Stream<SimpleGrantedAuthority> roles = userPrinciple.roles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_%s".formatted(r)));
        Stream<SimpleGrantedAuthority> permissions = userPrinciple.permissions().stream()
                .map(SimpleGrantedAuthority::new);
        return Stream.concat(roles, permissions).toList();
    }
}
