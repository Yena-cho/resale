package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 가상계좌 발급,
 * 입금(이체), 취소 전문 처리...
 */
@Service
public class CancelAPStatusService {

    @Autowired
    private BatchWorkerDao batchWorkerDao;

    private Logger LOG = LoggerFactory.getLogger(getClass());


    public List<Map<String, Object>> checkCancelAPStatus() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(date);
        return batchWorkerDao.checkCancelAPStatus(today);
    }

}
