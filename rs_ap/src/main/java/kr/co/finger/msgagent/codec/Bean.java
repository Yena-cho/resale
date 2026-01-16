package kr.co.finger.msgagent.codec;

import java.nio.charset.Charset;

public class Bean {
    private String message;
    private String type;
    private String length;
    private String lengthFromHeader;
    private boolean isOk;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLengthFromHeader() {
        return lengthFromHeader;
    }

    public void parseSkipHeader(byte[] bytes, Charset charset) {
        String start = new String(bytes, 0, 4, charset);
        String _length = new String(bytes, 4, 4, charset);
        String length =  new String(bytes, 8, 5, charset);
        lengthFromHeader = (Integer.valueOf(length) - 30) + "";
        System.out.println("start "+start);
        System.out.println("_length "+_length);
        System.out.println("lengthFromHeader "+lengthFromHeader);
    }

    public static void main(String[] args) {
        Bean bean = new Bean();
        bean.parseSkipHeader("####    00120 DHR1DHRTB   ".getBytes(Charset.forName("EUC-KR")),Charset.forName("EUC-KR"));
    }

}
