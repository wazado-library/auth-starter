package com.wazado.authstarter.domain.constant.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum EnumAuthErrorCode {
    ACCESS_DENIED("ACCESS_DENIED", "Truy cập bị từ chối"),
    UNAUTHORIZED("UNAUTHORIZED", "Vui lòng đăng nhập để tiếp tục.");

    String code;
    String message;
}
