package kr.co.finger.msgagent.command;

import kr.co.finger.damoa.model.msg.MsgIF;

public interface CommandExecutor {
    void execute(Class<? extends Command> commandClass, MsgIF request) throws Exception;
    MsgIF executeWithResult(Class<? extends Command> commandClass, MsgIF request) throws Exception;
}
