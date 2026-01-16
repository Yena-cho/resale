package com.finger.shinhandamoa.home.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public class UserDeniedHandler implements AccessDeniedHandler{

	private static final Logger logger = LoggerFactory.getLogger(UserDeniedHandler.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		request.setAttribute("errMsg",accessDeniedException.getMessage());
		request.getRequestDispatcher("/WEB-INF/views/error/denied.jsp").forward(request, response);
	}

}
