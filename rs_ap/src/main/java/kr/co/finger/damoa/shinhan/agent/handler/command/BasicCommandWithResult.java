package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.command.CommandWithResult;

public abstract class BasicCommandWithResult implements CommandWithResult {
    @Override
    public boolean executeWithPush(MsgIF request, MsgIF response) {
        return false;
    }
}
