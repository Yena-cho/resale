package com.finger.shinhandamoa.sys.cust.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.cust.dao.SysCustDAO;
import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;

@Service
public class SysCustServiceImpl implements SysCustService {

    @Autowired
    private SysCustDAO SysCustDAO;

    //고객상담 total count
    @Override
    public int custTotalCount(Map<String, Object> map) throws Exception {
        return SysCustDAO.custTotalCount(map);
    }

    //고객상담 전체 리스트
    @Override
    public List<SysCustDTO> custListAll(Map<String, Object> map) throws Exception {
        return SysCustDAO.custListAll(map);
    }

    @Override
    public int custWaitingCount(Map<String, Object> map) throws Exception {
        return SysCustDAO.custWaitCount(map);
    }

    @Override
    public SysCustDTO custListModal(Map<String, Object> map) throws Exception {
        return SysCustDAO.custListModal(map);
    }

    @Override
    public int updateCust(Map<String, Object> map) throws Exception {
        return SysCustDAO.updateCust(map);
    }

    @Override
    public void insertCust(Map<String, Object> map) throws Exception {
        SysCustDAO.insertCust(map);
    }

    @Override
    public void insertExcelCust(Map<String, Object> map) throws Exception {
        SysCustDAO.insertExcelCust(map);
    }

    @Override
    public int deleteCust(Map<String, Object> map) throws Exception {
        return SysCustDAO.deleteCust(map);
    }

}
