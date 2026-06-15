package com.wazado.authstarter.infrastructure.bootstrap.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthProperties {
    String secretKey;
    Long expiration;
    List<String> permitAll;
}
