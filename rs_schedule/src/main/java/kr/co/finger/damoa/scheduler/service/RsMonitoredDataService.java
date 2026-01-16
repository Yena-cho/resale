package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import kr.co.finger.damoa.scheduler.utils.MonitoringFileGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RsMonitoredDataService {

    @Autowired
    private BatchWorkerDao batchWorkerDao;



    private static final Logger LOGGER = LoggerFactory.getLogger(RsMonitoredDataService.class);

    //재판매 하위가맹점 등록,변경,해지 데이터
    public List<Map<String, Object>> getRsOrgMonitoredData(String fgCd) {
        return batchWorkerDao.getRsOrgMonitoredData(fgCd);
    }

    //재판매 가상계좌 등로그해지 데이터
    public List<Map<String, Object>> getRsVaMonitoredData(String fgcd) {
        return batchWorkerDao.getRsVaMonitoredData(fgcd);
    }

    public String getChacdByChaOffNoAndChaNameFromChaList(Map<String, Object> paramMap) {
        return batchWorkerDao.getChacdByChaOffNoAndChaNameFromChaList(paramMap);
    }

    public String getChacdByChaOffNoAndChaNameFromVaList(String vaNo){
        return batchWorkerDao.getChacdByChaOffNoAndChaNameFromVaList(vaNo);
    }

    @Transactional
    public void insertMonitoringFileHist(Map<String,Object> paramMap) {
        batchWorkerDao.insertMonitoringFileHist(paramMap);
    }

    public List<Map<String, Object>> selectMonitoringFileList() {
        return batchWorkerDao.selectMonitoringFileList();
    }

    public void updateMonitoringFileHist(Map<String,Object> fileMap) {
        batchWorkerDao.updateMonitoringFileHist(fileMap);
    }


    public void insertMonitoringFileHistDet(Map<String,Object> map) {
        batchWorkerDao.insertMonitoringFileHistDet(map);
    }





}
