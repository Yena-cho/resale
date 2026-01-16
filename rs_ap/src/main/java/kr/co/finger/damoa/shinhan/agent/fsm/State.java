package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;

public interface State {
    public void execute(Context context, MsgIF msgIF, NettyProducer nettyProducer, MessageFactory messageFactory, DamoaContext damoaContext) throws Exception;
    boolean isValid();
    public String getStateCode();

}
