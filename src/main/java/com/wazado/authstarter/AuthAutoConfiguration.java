package com.wazado.authstarter;

import com.wazado.authstarter.domain.filter.JwtAuthenticationFilter;
import com.wazado.authstarter.application.usecase.jwt.provider.JwtProviderUseCase;
import com.wazado.authstarter.domain.service.jwt.provider.JwtProvider;
import com.wazado.authstarter.infrastructure.bootstrap.config.authentication.AuthenticationConfiguration;
import com.wazado.authstarter.infrastructure.bootstrap.properties.AuthProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain securityFilterChain(JwtAuthenticationFilter filter, AuthProperties properties, HttpSecurity http) throws Exception {
        return new AuthenticationConfiguration(filter, properties).securityFilterChain(http);
    }
}
