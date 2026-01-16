package kr.co.finger.msgagent.command;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.client.NettyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandExecutorImpl implements CommandExecutor {
    @Autowired
    private CommandFactory commandFactory;
    @Autowired
    private ConcurrentMap<String,CompletableFuture<MsgIF>> concurrentMap;
    @Autowired
    private NettyProducer nettyProducer;
    @Override
    public void execute(Class<? extends Command> commandClass, MsgIF request) throws Exception {
        Command command = commandFactory.findCommand(commandClass);
        command.execute(request,concurrentMap,nettyProducer);
    }

    @Override
    public MsgIF executeWithResult(Class<? extends Command> commandClass, MsgIF request) throws Exception {
        CommandWithResult command = (CommandWithResult)commandFactory.findCommand(commandClass);
        return command.executeWithResult(request,concurrentMap,nettyProducer);
    }

}
