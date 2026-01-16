package com.finger.shinhandamoa.home.login;

import com.finger.shinhandamoa.common.CmmnUtils;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.common.exception.InvalidOtpException;
import com.finger.shinhandamoa.payer.notification.service.NotificationService;
import com.finger.shinhandamoa.payer.payment.service.PaymentService;
import com.finger.shinhandamoa.sys.otp.service.OtpService;
import com.finger.shinhandamoa.vo.UserDetailsVO;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
public class UserAuthenticationService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

    // 기관담당자, 납부자 로그인 구분을 위한 param
    private final String delimiter = ":///:";

    private SqlSessionTemplate sqlSession;

    private ShaPasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    public UserAuthenticationService() {
        passwordEncoder = new ShaPasswordEncoder(256);
    }

    public UserAuthenticationService(SqlSessionTemplate sqlSession) {
        this();
        this.sqlSession = sqlSession;
    }

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] str = username.split(delimiter);

        Map<String, Object> user = null;

        /**
         * ORG		기관담당자
         * CUS		납부자 고객정보
         * BANK		은행관리자
         * SYS		신한다모아관리자
         * GROUP	그룹관리자
         */
        if (str[0].equals("ORG")) {
            user = sqlSession.selectOne("MainDao.selectByOrgInfo", str[1]);
            user.put("UNCUS", "cus");
        } else if (str[0].equals("NUM")) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("vano", str[1]);

            user = sqlSession.selectOne("MainDao.selectByPayNumInfo", map);
            if(user == null){
                user = sqlSession.selectOne("MainDao.selectByPayNumInfoVal", map);
            }
            //청구유무확인
            HashMap<String, Object> rtotValue = null;
            HashMap<String, Object> ntotValue = null;
            try {
                HashMap<String, Object> reqMap = new HashMap<String, Object>();
                reqMap.put("chaCd", user.get("USERNAME").toString());
                reqMap.put("vaNo", user.get("VANO").toString());
                rtotValue = paymentService.payListTotalCount(reqMap);
                ntotValue = notificationService.notificationTotalCount(reqMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(rtotValue.get("CNT").toString().equals("0") && ntotValue.get("SUBTOT").toString().equals("0")){
                user.put("UNCUS", "unCus");
            }else{
                user.put("UNCUS", "cus");
            }
            String password = (String) user.get("PASSWORD");
            user.put("PASSWORD", passwordEncoder.encodePassword(password, null));
        } else if (str[0].equals("CUS")) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", str[1]);
            map.put("orgname", str[2]);
            map.put("cushp", str[3]);

            user = sqlSession.selectOne("MainDao.selectByPayCusInfo", map);
            String password = null;
            try {
                password = CmmnUtils.clobToString((java.sql.Clob)user.get("PASSWORD"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String telno = null;
            try {
                telno = CmmnUtils.clobToString((java.sql.Clob)user.get("TEL"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            user.put("TEL", telno);
            user.put("PASSWORD", passwordEncoder.encodePassword(password, null));
            user.put("UNCUS", "cus");
        } else if (str[0].equals("BANK") || str[0].equals("SYS")) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", str[1]);
            map.put("admGroup", str[2]);
            user = sqlSession.selectOne("MainDao.selectByAdminInfo", map);
            //어드민 otp 인증 추가
            if(str[0].equals("SYS")) {
                int otpCode = Integer.parseInt(otpService.getOtpFromContext());
                if(!otpService.verifyOtp((String) user.get("otpsecret"),otpCode)) {
                    throw  new InvalidOtpException();
                } else {
                    sqlSession.insert("MainDao.insertAdminLoginHist",str[1]);
                }
            }
            user.put("UNCUS", "cus");
        } else if (str[0].equals("GROUP")) {
            user = sqlSession.selectOne("MainDao.selectByGroupInfo", str[1]);
            user.put("UNCUS", "cus");
        }

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
        gas.add(new SimpleGrantedAuthority(user.get("authority").toString()));

        /**
         * USERNAME                 계정 이름
         * PASSWORD                 계정 비밀번호
         * ENABLED                  해지
         * ACCOUNTNONEXPIRED        서비스 이용제한 (수수료 미납)
         * CREDENTIALSNONEXPIRED    계정 만료
         * ACCOUNTNONLOCKED         로그인 5회 오류
         * gas                      계정 권한
         * NAME                     이름
         * USERCHECK                첫 로그인 체크
         * EMAIL                    이메일
         * TEL                      전화번호
         * VANO                     가상계좌번호
         * LOGINID                  로그인 아이디
         * CHAOFFNO                 사업자번호
         * FEEVANO                  출금계좌
         * FEEBANKCD                출금계좌 은행코드
         */
        return new UserDetailsVO(user.get("username").toString(),
                user.get("password").toString(),
                user.get("enabled").toString().equals("1"),
                user.get("accountnonexpired").toString().equals("1"),
                user.get("credentialsnonexpired").toString().equals("1"),
                user.get("accountnonlocked").toString().equals("1"),
                gas,
                user.get("name").toString(),
                String.valueOf(user.get("usercheck")),
                StringUtils.defaultString(user.get("email").toString()),
                StringUtils.defaultString(user.get("tel").toString()),
                user.get("vano").toString(),
                user.get("loginid").toString(),
                user.get("chaoffno").toString(),
                StringUtils.defaultString((String) user.get("feevano")),
                StringUtils.defaultString((String) user.get("feebankcd")),
                StringUtils.defaultString(user.get("UNCUS").toString())
        );
    }
}
