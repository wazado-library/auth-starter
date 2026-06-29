package com.wazado.authstarter.infrastructure.bootstrap.config.authentication;

import com.wazado.authstarter.domain.filter.JwtAuthenticationFilter;
import com.wazado.authstarter.domain.service.password.encoder.PasswordEncoderService;
import com.wazado.authstarter.infrastructure.bootstrap.properties.AuthProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationConfiguration {
    JwtAuthenticationFilter jwtAuthenticationFilter;
    AuthProperties properties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    if(CollectionUtils.isEmpty(properties.getPermitAll())) {
                        authorize.requestMatchers("/**").permitAll();
                    } else {
                        String[] permitAllPaths = properties.getPermitAll().toArray(new String[0]);
                        authorize.requestMatchers(permitAllPaths).permitAll().anyRequest().authenticated();
                    }
                })
                .build();
    }
}
