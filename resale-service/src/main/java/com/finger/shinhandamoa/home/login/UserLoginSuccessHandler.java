package com.finger.shinhandamoa.home.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 * @version
 * 
 */
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(UserLoginSuccessHandler.class);

	private SqlSessionTemplate sqlSession;
	
	public UserLoginSuccessHandler(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String[] id = request.getParameter("userId").split(":///:");
		String session = request.getSession().getId();

		String concurrentBlockYn = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> user = null;
		map.put("stat", "success");
		map.put("username", SecurityContextHolder.getContext().getAuthentication().getName());
		map.put("username", id[1]);

		try {
			if(id[0].equals("ORG")) {
				sqlSession.update("MainDao.failCountUpdate", map);
			} else if(id[0].equals("BANK") || id[0].equals("SYS")) {
				sqlSession.update("MainDao.adminFailCountUpdate", map);
			}

			//동시접속블록설정이 되어있는지확인후 동시접속블록을 사용하는경우 로그인시 해당 세션을 제외하고 모든 다른 세션 삭제
			user = sqlSession.selectOne("MainDao.concurrentBlockYn", map);
			if(user != null){
				concurrentBlockYn= user.get("CONCURRENTBLOCKYN").toString();
				if(concurrentBlockYn.equals("Y")) {
					logger.debug("자동로그아웃 기관코드:" + map.get("username").toString());
					map.put("sessionId", session);
					sqlSession.delete("MainDao.deleteOtherSession", map);
				}
			}
//			sqlSession.clearCache();

		} catch (Exception e) {
			e.printStackTrace();
		}

		response.sendRedirect("/");
	}

}
