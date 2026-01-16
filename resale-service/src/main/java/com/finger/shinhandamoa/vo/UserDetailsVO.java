package com.finger.shinhandamoa.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
public class UserDetailsVO extends User {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // 추가 param
    private String name;
    private String userCheck;
    private String email;
    private String tel;
    private String vano;
    private String loginId;
    private String chaOffNo;
    private String feeVano;
    private String feeBankCd;
    private static String unCus;

    public UserDetailsVO(String username,                                    // 계정이름
                         String password,                                    // 계정비밀번호
                         boolean enabled,                                    // 계정 만료 여부
                         boolean accountNonExpired,                            // 계정 잠김 여부
                         boolean credentialsNonExpired,                    // 계정 비밀번호 만료 여부
                         boolean accountNonLocked,                            // 계정 사용 여부
                         Collection<? extends GrantedAuthority> authorities,// 계정 권한
                         String name,                                        // 이름
                         String userCheck,                                    // 첫 로그인 체크
                         String email,                                        // 이메일
                         String tel,                                        // 핸드폰번호
                         String vano,                                        // 계좌번호
                         String loginId,                                    // 로그인id
                         String chaOffNo,                                    // 사업자번호
                         String feeVano,                                    // 출금계좌
                         String feeBankCd,                                    // 출금계좌 은행코드
                         String unCus                                       //납부자 청구없는경우
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.name = name;
        this.userCheck = userCheck;
        this.email = email;
        this.tel = tel;
        this.vano = vano;
        this.loginId = loginId;
        this.chaOffNo = chaOffNo;
        this.feeVano = feeVano;
        this.feeBankCd = feeBankCd;
        this.unCus = unCus;
    }

    public String getUnCus() {
        return unCus;
    }

    public void setUnCus(String unCus) {
        this.unCus = unCus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserCheck() {
        return userCheck;
    }

    public void setUserCheck(String userCheck) {
        this.userCheck = userCheck;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVano() {
        return vano;
    }

    public void setVano(String vano) {
        this.vano = vano;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getFeeVano() { return feeVano; }

    public void setFeeVano(String feeVano) {
        this.feeVano = feeVano;
    }

    public String getFeeBankCd() { return feeBankCd; }

    public void setFeeBankCd(String feeBankCd) {
        this.feeBankCd = feeBankCd;
    }

}
