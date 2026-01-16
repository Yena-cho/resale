package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.PollingMessage;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * 폴링전문 처리.
 */
@Component
public class PollingMsgCommand extends BasicCommand {

    @Autowired
    private MessageFactory messageFactory;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        PollingMessage input = (PollingMessage) msgIF;
        if ("HDRRESPOLL".equalsIgnoreCase(input.getPollingType())) {
            // 폴링응답전문은 무시한다.
            LOG.info("<<< SKIP[" + input.getDesc() + "]" + input);
        } else {
            LOG.info("<<< [" + input.getDesc() + "]" + input);
            PollingMessage response = (PollingMessage) response(input);
            String responseMsg = encode(messageFactory, response,"HDRRESPOLL");
            nettyProducer.write(responseMsg);
        }

    }


}
