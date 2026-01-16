//package com.finger.shinhandamoa.common;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collection;
//
///**
// * @author denny91@finger.co.kr
// */
//@Component("SessionCookieFilter")
//public class SessionCookieFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(
//            ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        Cookie[] allCookies = req.getCookies();
//
//        if (allCookies != null) {
//            Cookie session = Arrays.stream(allCookies).filter(x -> x.getName().equals("SESSION")).findFirst().orElse(null);
//
//            if (session != null) {
//                session.setPath("/");
//                session.setHttpOnly(true);
//                session.setSecure(true);
//                res.addCookie(session);
//            }
//        }
//        chain.doFilter(req, res);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//}
