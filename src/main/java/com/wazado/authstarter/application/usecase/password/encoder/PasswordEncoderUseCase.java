package com.wazado.authstarter.application.usecase.password.encoder;

import com.wazado.authstarter.domain.service.password.encoder.PasswordEncoderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordEncoderUseCase implements PasswordEncoderService {
    @Override
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
