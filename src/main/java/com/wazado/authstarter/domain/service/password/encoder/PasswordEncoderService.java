package com.wazado.authstarter.domain.service.password.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordEncoderService {
    PasswordEncoder encoder();
}
