package com.finger.shinhandamoa.bank;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.finger.shinhandamoa.common.SessionUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author  by puki
 * @date    2018. 5. 4.
 * @desc    최초생성
 * @version 
 * 
 */
@Controller
public class BankController {
	
	private static final Logger logger = LoggerFactory.getLogger(BankController.class);
	
	/*
	 * 은행관리자 > 로그인화면
	 */
	@RequestMapping(value = "/bank", method = RequestMethod.GET)
	public String bankHome(HttpServletRequest request, HttpServletResponse response , Authentication authentication) throws Exception {
		// ip 대역을 체크. 허용대역은 211.232.21.39, 58.151.154.189, 58.151.154.190  만 허용
		request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		String remoteIP = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
			remoteIP = request.getHeader("X-Forwarded_For");
		}
		if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
			remoteIP = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
			remoteIP = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
			remoteIP = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
			remoteIP = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(remoteIP) || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
			remoteIP = request.getRemoteAddr();
		}

		logger.info("remoteIP = " + remoteIP);
		int pos = remoteIP.lastIndexOf(".");
		String rIpEnd = remoteIP.substring(pos+1);
		if (remoteIP.startsWith("127.0.0.1") || remoteIP.startsWith("59.7.254."+rIpEnd)
				|| remoteIP.startsWith("221.151.63.193") || remoteIP.startsWith("58.151.154."+rIpEnd) || remoteIP.startsWith("0:0:0:0:0:0:0:1") || remoteIP.startsWith("10.20.30."+rIpEnd) || remoteIP.startsWith("10.20.20."+rIpEnd) || remoteIP.startsWith("123.140.10.74")) {
			if (!SessionUtil.hasRole("ROLE_BANKADMIN")) {
				// 로그인 되어있으면 무조건 강제로 로그아웃 시킨다.
				if (authentication != null) {

					SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
					securityContextLogoutHandler.logout(request, response, authentication);

				}
			}
			return "bank/index";
		} else {
			return "redirect:common/denied";
		}
	}
	
	/*
	 * 은행관리자 > 신규등록기관조회
	 */
	@RolesAllowed({"ROLE_BANKADMIN"})
	@RequestMapping("bank/selManage")
	public String manageCollecter() throws Exception {
		
		if (!SessionUtil.hasRole("ROLE_BANKADMIN")) {
			return "common/denied";
		}
		
		return "bank/newChaMgmt/newChaList";
	}
}
