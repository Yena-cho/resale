package kr.co.finger.msgio.exception;

public class MsgException extends Exception {
    public MsgException(String message) {
        super(message);
    }

    public MsgException(String message, Throwable cause) {
        super(message, cause);
    }
}
