package kr.co.finger.msgio.msg;

public class MsgFactory {
    private static MsgFactory ourInstance = new MsgFactory();

    public static MsgFactory getInstance() {
        return ourInstance;
    }

    private MsgFactory() {
    }


    public String encode(Object o) {

        return "";
    }

    public Object decode(String msg) {
        return null;
    }


}
