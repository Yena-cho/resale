package com.finger.shinhandamoa.common.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidOtpException extends AuthenticationException {
    public InvalidOtpException() {
        super("OTP가 틀렸습니다");
    }

    public InvalidOtpException(String message) {
        super(message);
    }
}
