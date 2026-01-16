package com.finger.shinhandamoa.sys.otp.service;

import com.finger.shinhandamoa.sys.setting.dto.AdminInfoDTO;
import com.finger.shinhandamoa.sys.setting.service.SettingService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    @Autowired
    private SqlSessionTemplate sqlSession;



    @Inject
    private SettingService settingService;

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();


    @Override
    public String generateAndSaveSecretKey(String user) throws Exception {
        try{
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("username",user);
        paramMap.put("admGroup","DA");
        paramMap.put("otpSecret",key.getKey());
        paramMap.put("adminId",user);
        sqlSession.update("MainDao.updateOtp", paramMap);
        return key.getKey();
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Override
    public String getQrCodeUrl(String user) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username",user);
        paramMap.put("admGroup","DA");
        Map<String, Object> userMap =  sqlSession.selectOne("MainDao.selectByAdminInfo", paramMap);

        String otpSecret = String.valueOf(userMap.get("otpsecret"));
        String userID = String.valueOf(userMap.get("name"));
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey.Builder(otpSecret).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("RESALE", userID, key);
    }

    @Override
    public boolean verifyOtp(String userOtp, int otpCode) {

        return gAuth.authorize(userOtp,otpCode);
    }

    @Override
    public boolean verifyAndGenerateJwt() {
        return false;
    }

    @Override
    public String getOtpFromContext() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attr != null) {
            HttpServletRequest request = attr.getRequest();
            return request.getParameter("userOtp");
        }
        return null;
    }
}
