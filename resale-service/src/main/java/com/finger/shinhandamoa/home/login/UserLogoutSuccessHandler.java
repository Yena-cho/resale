package com.finger.shinhandamoa.home.login;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 사용자 로그아웃 핸들러
 * 
 * @author lsy
 * @author wisehouse@finger.co.kr
 * @since 2018. 8. 4.
 */
public class UserLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        final String referer = request.getHeader("Referer");
        LOGGER.debug("### referer = " + referer);
        final StringBuffer uri = request.getRequestURL();

        // 메인로그인 화면
        final String file = getFile(referer);
        final String proto = getProto(request, uri);
        final String host = getHost(uri);
        
        final StringBuilder sb = new StringBuilder();
        sb.append(proto);
        sb.append("://");
        sb.append(host);
        sb.append(request.getContextPath());
        sb.append(file);

        setDefaultTargetUrl(sb.toString());
        
        LOGGER.debug("LogoutSuccessTargetURL : {}", sb.toString());
        super.onLogoutSuccess(request, response, authentication);
    }

    private String getHost(StringBuffer uri) {
        return StringUtils.substringBetween(uri.toString(), "://", "/logout");
    }

    private String getProto(HttpServletRequest request, StringBuffer uri) {
        final String forwardedProto = request.getHeader("X-Forwarded-Proto");
        final String simpleProto = StringUtils.substringBefore(uri.toString(), "://");
        return StringUtils.defaultString(forwardedProto, simpleProto);
    }

    private String getFile(String referer) {
        final String file;

        if (referer.indexOf("common") > 0) {
            // 로그인 화면
            file = "/common/login";
        } else if (referer.indexOf("bank") > 0) {
            // 은행관리자 로그인 화면
            file = "/bank";
        } else if (referer.indexOf("sys") > 0) {
            // 신한다모아관리자 로그인 화면
            file = "/sys";
        } else {
            file = "/main";
        }
        return file;
    }

}
