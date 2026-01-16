package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.client.NettyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
@Component
public class ReqResSyncCommand extends BasicCommandWithResult {
    @Value("${netty.req-res.timeout}")
    private int reqResTimeout = 500;

    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {

    }

    /**
     * 핑거에서 신한으로 전문요청할때, 요청후 response를 매칭하기 위해 사용?
     * @param msgIF
     * @param concurrentMap
     * @param nettyProducer
     * @return
     * @throws Exception
     */
    @Override
    public MsgIF executeWithResult(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        try {
            CompletableFuture<MsgIF> completableFuture = new CompletableFuture();
            nettyProducer.write(msgIF.getOriginMessage());
            LOG.info("msgIF.getKey() "+msgIF.getKey());
            concurrentMap.put(msgIF.getKey(),completableFuture);
            MsgIF response = completableFuture.get(reqResTimeout, TimeUnit.MILLISECONDS);
            if (response != null) {
                concurrentMap.remove(msgIF.getKey());
                return response;
            } else {
                // TODO 타임아웃을 던지기??
                return null;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }

    }
}
