package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import kr.co.finger.msgio.msg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class WithdrawalService {
    @Autowired
    private BatchWorkerDao batchWorkerDao;

    public List<Map<String, Object>> findNewAutomaticWithdrawal() {
        return batchWorkerDao.findNewAutomaticWithdrawal();
    }

    public List<Map<String, Object>> findMonthlyAutomaticWithdrawal(String month) {
        return batchWorkerDao.findMonthlyAutomaticWithdrawal(month);
    }


    public void updateMonthlyAutomaticWithdrawal(String pk, String result) {

    }

    public void updateNewWithdrawal(String pk, String ok) {

    }

    public void updateReceivedMonthlyAutomaticWithdrawal(EB22 eb22) {

    }

    public void updateReceivedMonthlyAutomaticWithdrawal(String pk, String message) {

    }
    @Transactional
    public void updateEB14Result(EB14 eb14) {
        List<NewData> dataList = eb14.getDataList();
        for (NewData data : dataList) {
            batchWorkerDao.updateEB14Result(data.toUpdateParam());
        }
    }
    @Transactional
    public void updateEBI13Result(EB13 eb13) {
        List<NewData> dataList = eb13.getDataList();
        for (NewData data : dataList) {
            batchWorkerDao.updateEBI13Result(data.toEB13UpdateParam());
        }
    }
    @Transactional
    public void updateEB21Result(EB21 eb21, String previousMonth) {
        List<BatchData> dataList = eb21.getDataList();
        for (BatchData data : dataList) {
            batchWorkerDao.updateEB21Result(data.toEB21UpdateParam(previousMonth));
        }
    }
    @Transactional
    public void updateEB22Result(EB22 eb22,String previousMonth) {
        List<BatchData> dataList = eb22.getDataList();
        for (BatchData data : dataList) {
            batchWorkerDao.updateEB22Result(data.toEB22UpdateParam(previousMonth));
        }
    }
}
