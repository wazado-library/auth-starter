package com.wazado.authstarter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wazado.authstarter.domain.filter.JwtAuthenticationFilter;
import com.wazado.authstarter.application.usecase.jwt.provider.JwtProviderUseCase;
import com.wazado.authstarter.domain.service.jwt.provider.JwtProvider;
import com.wazado.authstarter.infrastructure.bootstrap.config.authentication.AuthenticationConfiguration;
import com.wazado.authstarter.infrastructure.bootstrap.config.handler.CustomAccessDeniedHandler;
import com.wazado.authstarter.infrastructure.bootstrap.config.handler.CustomAuthenticationEntryPoint;
import com.wazado.authstarter.infrastructure.bootstrap.properties.AuthProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JwtProvider jwtProvider(AuthProperties properties) {
        return new JwtProviderUseCase(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(
            JwtAuthenticationFilter filter,
            AuthProperties properties,
            AccessDeniedHandler accessDeniedHandler,
            AuthenticationEntryPoint authenticationEntryPoint,
            HttpSecurity http
    ) throws Exception {
        return new AuthenticationConfiguration(accessDeniedHandler, authenticationEntryPoint, filter, properties).securityFilterChain(http);
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new CustomAccessDeniedHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationConfiguration(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
