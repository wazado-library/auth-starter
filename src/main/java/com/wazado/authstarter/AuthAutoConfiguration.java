package com.wazado.authstarter;

import com.wazado.authstarter.application.usecase.JwtProviderUseCase;
import com.wazado.authstarter.domain.service.jwt.provider.JwtProvider;
import com.wazado.authstarter.infrastructure.bootstrap.properties.AuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JwtProvider jwtProvider(AuthProperties properties, HttpServletRequest request) {
        return new JwtProviderUseCase(properties, request);
    }
}
