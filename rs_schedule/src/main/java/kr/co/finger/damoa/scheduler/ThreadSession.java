package kr.co.finger.damoa.scheduler;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 쓰레드 세션. 실행 중인 쓰레드의 세션
 * 
 * @author wisehouse@finger.co.kr
 */
public class ThreadSession {
    private Map<String, String> sessionAttributeMap;
    
    public static final String SESSION_ATTRIBUTE_KEY_OWNER = "owner";
    
    public ThreadSession() {
        sessionAttributeMap = new HashMap<>();
    }
    
    public String getAttribute(String key) {
        return StringUtils.defaultString(sessionAttributeMap.get(key));
    }
    
    public void setAttribute(String key, String value) {
        sessionAttributeMap.put(StringUtils.defaultString(key), StringUtils.defaultString(value));
    }
}
