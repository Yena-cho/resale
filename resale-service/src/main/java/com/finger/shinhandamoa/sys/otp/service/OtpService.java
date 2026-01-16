package com.finger.shinhandamoa.sys.otp.service;

import java.util.Map;

public interface OtpService {
    String generateAndSaveSecretKey(String user) throws Exception;
    String getQrCodeUrl(String user);
    boolean verifyOtp(String userOtp, int otpCode);
    boolean verifyAndGenerateJwt();

    String getOtpFromContext();

}
