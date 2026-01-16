//package com.finger.shinhandamoa.common;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.session.web.http.CookieSerializer;
//import org.springframework.session.web.http.DefaultCookieSerializer;
//
///**
// * @author denny91@finger.co.kr
// */
//
//@Configuration
//public class SessionCookieInitializer {
//
//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        cookieSerializer.setUseHttpOnlyCookie(true);
//        cookieSerializer.setUseSecureCookie(true);
//
//        return cookieSerializer;
//    }
//
//}