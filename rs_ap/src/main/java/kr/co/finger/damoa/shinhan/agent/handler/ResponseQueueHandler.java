package kr.co.finger.damoa.shinhan.agent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;

@Component
public class ResponseQueueHandler implements Runnable {

    @Autowired
    private BizHandler bizHandler;

    @Resource(name = "responseBlockingQueue")
    private BlockingQueue<String> responseBlockingQueue;

    private volatile boolean doStop;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void start() {
        new Thread(this).start();
    }

    @PreDestroy
    public void stop() {
        doStop = true;

    }

    @Override
    public void run() {
        LOG.info("******************** ResponseQueueHandler run ********************");
        while (doStop == false) {
            try {
                String response = responseBlockingQueue.take();
                LOG.info("******************** ResponseQueueHandler run response ********************"+ response);
                bizHandler.execute(response);
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

    }
}
