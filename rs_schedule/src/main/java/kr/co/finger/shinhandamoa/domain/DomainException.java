package kr.co.finger.shinhandamoa.domain;

/**
 * 도메인 예외
 * 
 * @author wisehouse@finger.co.kr
 */
public class DomainException extends Exception {
    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(Throwable throwable) {
        super(throwable);
    }
    
    public DomainException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
