package com.finger.shinhandamoa.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CSRF 취약점 보안을 위해 Request Header의 Referer를 확인하는 필터<br />
 * properties에 설정된 도메인 리스트에 이외의 요청이 들어오면 에러페이지<br />
 *
 * @author denny91@finger.co.kr
 */
@Component("CsrfFilter")
public class CsrfFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfFilter.class);

    @Value("${allowed.domain}")
    private String domainUrl;

    private String[] allowed;

    @PostConstruct
    public void postConstruct() throws  Exception {
        // 설정에 1개이상의 도메인이 있을수 있으므로 , 로 구분하여 배열로 담는다
        allowed = StringUtils.defaultString(StringUtils.replace(domainUrl, " ", "")).split(",");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;
        String referrer = StringUtils.defaultString(httpServletRequest.getHeader("referer"));

        try{
            // referer가 없는경우는 확인하지 않는다
            if(StringUtils.isBlank(referrer)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // 도메인리스트 설정이 없는경우 확인하지 않는다
            if(ArrayUtils.isEmpty(allowed)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // 리스트에 설정된 도메인중 한곳이라도 있으면 패스
            if(StringUtils.containsAny(referrer, allowed)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // 위의 조건들에 맞지않는 경우 500 에러
            httpServletResponse.sendError(500);
            LOGGER.warn("Domain not allowed {} ", referrer);
        }catch (Exception e){
            LOGGER.error("error message", e);
        }

    }

    @Override
    public void destroy() {

    }
}
