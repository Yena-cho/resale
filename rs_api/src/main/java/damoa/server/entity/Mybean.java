package damoa.server.entity;

public class Mybean {
    private String rcpmascd;
    private String maskey;

    public Mybean(String rcpmascd, String maskey) {
        this.rcpmascd = rcpmascd;
        this.maskey = maskey;
    }

    public String getRcpmascd() {
        return rcpmascd;
    }

    public String getMaskey() {
        return maskey;
    }
}

