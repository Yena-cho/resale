package kr.co.finger.damoa.shinhan.agent.handler.command;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.biz.VirtualAccountNoChecker;
import kr.co.finger.damoa.model.msg.*;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaException;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * 가상계좌범위전문
 * 이 전문은 화면에서 요청함.
 */
@Component
@Deprecated
public class RangeAccountMsgCommand extends BasicCommand {
    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private DamoaContext damoaContext;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        VirtualAccountRangeMessage msg = (VirtualAccountRangeMessage)msgIF;
        ResultBean _result = validateCommons(msg, messageFactory,damoaContext,LOG);
        if ("0".equalsIgnoreCase(_result.getCode())==false) {
            throw new DamoaException(_result);
        }

        if (MessageCode.RANGE_ACCOUNT_RESPONSE.equals(Code.createByMsgIF(msgIF))) {
            LOG.info("<<< [가상계좌응답전문]"+msgIF.getKey());
            CompletableFuture<MsgIF> completableFuture = concurrentMap.get(msgIF.getKey());
            if (completableFuture == null) {
                // V768 응답코드. TIME OUT 또는 시간경과후 응답
                LOG.error(msgIF.getKey()+" TIMEOUT 발생...");
            } else {
                completableFuture.complete(msgIF);
            }
        } else {
            // 이런 경우는 예외임...
            LOG.warn("<<< SKIP [가상계좌요청]"+msgIF);
        }
    }
}
