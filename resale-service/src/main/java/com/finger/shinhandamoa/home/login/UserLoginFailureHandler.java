package com.finger.shinhandamoa.home.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.exception.InvalidOtpException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public class UserLoginFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(UserLoginFailureHandler.class);
	
	private SqlSessionTemplate sqlSession;
	
	public UserLoginFailureHandler(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
			throws IOException, ServletException {
		
		String[] id = request.getParameter("userId").split(":///:");
		
//		logger.info("로그인 실패 사유 === : " + authenticationException.getLocalizedMessage());
//		logger.info("로그인 실패 사유 === : " + id[2]);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stat", "fail");
		map.put("username", id[1]);
		
		String errorMsg = "unidentified";
		String auth = "hp";
		
		if(authenticationException instanceof InternalAuthenticationServiceException) { // 내부시스템 오류
			errorMsg = "internal";
			if(authenticationException.getCause() instanceof InvalidOtpException) {
				errorMsg = "otp";
				auth = "DA";
			}
		} else if(authenticationException instanceof BadCredentialsException) { 		// 계정 정보가 올바르지 않음 
			
			if(id[0].equals("ORG")) {			// ORG : 기관담당자
				int idx = sqlSession.selectOne("MainDao.selLoginOrgCheck", map);
				
				if(idx > 0) {
					auth = "pwd";
				} else {
					auth  = "id";
				}
				sqlSession.update("MainDao.failCountUpdate", map); // fail count 
			} else if(id[0].equals("NUM")) {	// NUM : 납부자 납부계좌 로그인
				int idx = sqlSession.selectOne("MainDao.selLoginVanoCheck", map);
				
				if(idx == 0) {
					auth = "acc";
				}
			} else if(id[0].equals("CUS")) {	// CUS : 납부자 고객정보 로그인
				map.put("orgname",  id[2]);
				int idx = sqlSession.selectOne("MainDao.selLoginCusCheck", map);
				if(idx == 0) {
					auth = "cus";
				} 
			} else if(id[0].equals("BANK") || id[0].equals("SYS")) { 	// BANK : 은행관리자 로그인, SYS : 다모아관리자 로그인
				map.put("admGroup", id[2]);
				int idx = sqlSession.selectOne("MainDao.selLoginAdminCheck", map);
				if(idx == 0) {
					auth = "admin";
				}
				sqlSession.update("MainDao.adminFailCountUpdate", map); // fail count 
			}
			
			errorMsg = "badCredential";
		} else if(authenticationException instanceof CredentialsExpiredException) { 	// 계정 권한 만료
			errorMsg = "credentialsExpired";
		} else if(authenticationException instanceof LockedException) { 				// locked
			errorMsg = "locked";
		} else if(authenticationException instanceof AccountExpiredException) { 		// accountExpired
			errorMsg = "accountExpired";
		} else if(authenticationException instanceof DisabledException) {				// disabled
			errorMsg = "disabled";
		}
		
		String str = request.getHeader("referer");
		String url = "/main";					// 메인로그인 화면
		
		if(str.indexOf("common") > 0) {			// 로그인 화면
			url = "/common/login";
		} else if(str.indexOf("bank") > 0) {	// 은행관리자 로그인 화면
			url = "/bank";
		} else if(str.indexOf("sys") > 0) {		// 신한다모아관리자 로그인 화면
			url = "/sys";
		}
		
		response.sendRedirect(request.getContextPath() + url + "?error=" + errorMsg + "&auth=" + auth);
	}

}
