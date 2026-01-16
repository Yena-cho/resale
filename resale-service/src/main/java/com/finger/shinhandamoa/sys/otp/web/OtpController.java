package com.finger.shinhandamoa.sys.otp.web;

import com.finger.shinhandamoa.sys.otp.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpController {


    @Autowired
    private OtpService otpService;

    @RequestMapping("/generate/{userId}")
    @ResponseBody
    public Map<String,String> generateOtp(@PathVariable String userId) throws Exception {
        String secretKey = otpService.generateAndSaveSecretKey(userId);
        String qrCodeUrl = otpService.getQrCodeUrl(userId);

        Map<String,String> res = new HashMap<>();
        res.put("secretKey",secretKey);
        res.put("qrCodeUrl",qrCodeUrl);
        return res;

    }

}
