package co.finger.socket.router.common;

import io.netty.channel.ChannelHandler;

import java.util.List;

public interface HandlerFactory {
    public List<ChannelHandler> createHandler();
}
