package kr.co.finger.msgagent.client;

public interface NettyClient {
    public void write(String msg) throws Exception;
}
