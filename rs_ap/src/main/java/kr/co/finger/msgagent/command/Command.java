package kr.co.finger.msgagent.command;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.client.NettyProducer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

public interface Command {
    boolean executeWithPush(MsgIF request, MsgIF response) throws Exception;

    void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception;
}
