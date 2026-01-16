package kr.co.finger.msgagent.command;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.client.NettyProducer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

public interface CommandWithResult extends Command {
    public MsgIF executeWithResult(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception;
}
