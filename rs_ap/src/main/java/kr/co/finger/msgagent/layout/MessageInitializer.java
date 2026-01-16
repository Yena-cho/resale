package kr.co.finger.msgagent.layout;

import kr.co.finger.damoa.model.msg.MsgIF;

import java.util.Map;

public interface MessageInitializer {
    public void initialize(Map<String,MsgIF> messageInstanceByTranTypeCode);
    public TranTypeCodeResolver getTranTypeCodeResolver();
}
