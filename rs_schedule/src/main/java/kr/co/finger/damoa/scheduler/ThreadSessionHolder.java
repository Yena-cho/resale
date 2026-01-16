package kr.co.finger.damoa.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 쓰레드 세션 홀더
 * 
 * @author wisehouse@finger.co.kr
 */
public class ThreadSessionHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadSessionHolder.class);
    
    private static ThreadSessionHolder INSTANCE;
    
    private ThreadLocal<ThreadSession> threadLocal;
    
    public static final synchronized ThreadSessionHolder getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ThreadSessionHolder();
        }
        
        return INSTANCE;
    }
    
    public ThreadSessionHolder() {
        threadLocal = new ThreadLocal<>();
    }
    
    public ThreadSession getSession(String owner) {
        if(threadLocal.get() == null) {
            ThreadSession value = new ThreadSession();
            value.setAttribute(ThreadSession.SESSION_ATTRIBUTE_KEY_OWNER, owner);
            
            threadLocal.set(value);
        }
        
        return threadLocal.get();
    }
    
    public void removeSession() {
        threadLocal.remove();        
    }
}
