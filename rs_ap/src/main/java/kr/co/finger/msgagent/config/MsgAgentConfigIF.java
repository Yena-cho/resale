package kr.co.finger.msgagent.config;

import io.netty.channel.ChannelInitializer;

public interface MsgAgentConfigIF {
    /**
     * netty 클라이언트 channelInitializer 생성.
     * @return
     */
    public ChannelInitializer clientChannelInitializer();

    /**
     * netty 서버 channelInitializer 생성
     * @return
     */
    public ChannelInitializer serverChannelInitializer();

    /**
     * MessageFactoyr 설정.
     * charset를 생성할 수 있음.
     */
    public void initializeMessageFactory();
}
