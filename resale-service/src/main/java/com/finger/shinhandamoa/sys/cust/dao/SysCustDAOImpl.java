package com.finger.shinhandamoa.sys.cust.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;

@Repository
public class SysCustDAOImpl implements SysCustDAO {


    @Autowired
    private SqlSession sqlSession;

    //고객상담 total count
    @Override
    public int custTotalCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysCust.custTotalCount", map);
    }

    //고객상담 전체리스트
    @Override
    public List<SysCustDTO> custListAll(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("SysCust.custListAll", map);
    }

    //고객상담 대기건수
    @Override
    public int custWaitCount(Map<String, Object> map) {
        return sqlSession.selectOne("SysCust.custWaitingCount", map);
    }

    @Override
    public SysCustDTO custListModal(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysCust.custListModal", map);
    }

    @Override
    public int updateCust(Map<String, Object> map) throws Exception {
        return sqlSession.update("SysCust.updateCust", map);
    }

    @Override
    public void insertCust(Map<String, Object> map) throws Exception {
        sqlSession.update("SysCust.insertCust", map);
    }

    @Override
    public void insertExcelCust(Map<String, Object> map) throws Exception {
        sqlSession.update("SysCust.insertExcelCust", map);
    }

    @Override
    public int deleteCust(Map<String, Object> map) throws Exception {
        return sqlSession.delete("SysCust.deleteCust", map);
    }
}
