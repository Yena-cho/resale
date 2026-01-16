package damoa.comm.util;

public class UserException extends Exception {
    public UserException() {
    }

    public UserException(String s) {
        super(s);
        System.out.println(s);
    }
}
