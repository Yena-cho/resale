package kr.co.finger.msgagent.command;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public Command findCommand(Class<? extends Command> commandClass) {
        return applicationContext.getBean(commandClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
