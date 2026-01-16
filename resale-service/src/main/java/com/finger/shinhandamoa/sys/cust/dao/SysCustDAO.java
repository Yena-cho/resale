package com.finger.shinhandamoa.sys.cust.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;

public interface SysCustDAO {
    //고객상담 total count
    public int custTotalCount(Map<String, Object> map) throws Exception;

    //고객상담 전체리스트
    public List<SysCustDTO> custListAll(Map<String, Object> map) throws Exception;

    //고객상담 대기건수
    public int custWaitCount(Map<String, Object> map) throws Exception;

    public SysCustDTO custListModal(Map<String, Object> map) throws Exception;

    public int updateCust(Map<String, Object> map) throws Exception;

    public void insertCust(Map<String, Object> map) throws Exception;

    public void insertExcelCust(Map<String, Object> map) throws Exception;

    public int deleteCust(Map<String, Object> map) throws Exception;
}
