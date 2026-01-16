package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleService {

    @Autowired
    private BatchWorkerDao batchWorkerDao;

    private Logger LOG = LoggerFactory.getLogger(getClass());
    public boolean requestPermission(String startDate, String jobId) {
        try {
            batchWorkerDao.requestPermission(startDate, jobId);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return false;
        }
    }

    public void finishExecution(String startDate, String workId, int count,boolean isOk) {
        batchWorkerDao.finishExecution(startDate, workId,count,isOk);
    }
}
