package kr.co.finger.damoa.scheduler.controller;

import kr.co.finger.damoa.scheduler.model.Result;
import kr.co.finger.damoa.scheduler.task.monthly.MonthlyMonitoringFileReceiveTask;
import kr.co.finger.damoa.scheduler.task.monthly.MonthlyMonitoringFileTransferTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private Logger LOG = LoggerFactory.getLogger(getClass());


    @Autowired
    MonthlyMonitoringFileTransferTask monthlyMonitoringFileTransferTask;
    @Autowired
    MonthlyMonitoringFileReceiveTask monthlyMonitoringFileReceiveTask;

    @RequestMapping(value = "/test/send", method = RequestMethod.POST)
    @ResponseBody
    public Result testSend() throws Exception {

        LOG.info("모니터링 파일 생성 컨트롤러 시작");
        try {
            monthlyMonitoringFileTransferTask.trigger();
            return new Result("OK");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new Result("ERROR");
        }

    }


    @RequestMapping(value = "/test/recv", method = RequestMethod.POST)
    @ResponseBody
    public Result testRecv() throws Exception {

        LOG.info("모니터링 파일 수신 컨트롤러 시작");
        try {
            monthlyMonitoringFileReceiveTask.trigger();
            return new Result("OK");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new Result("ERROR");
        }

    }



}
