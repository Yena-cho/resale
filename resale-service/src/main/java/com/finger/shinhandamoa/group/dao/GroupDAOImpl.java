package com.finger.shinhandamoa.group.dao;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.group.dto.GroupDTO;

@Repository
public class GroupDAOImpl implements GroupDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int selGroupCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("GroupDao.selectGroupCount", map);
    }

    @Override
    public List<GroupDTO> selGroupList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("GroupDao.selectGroupList", map);
    }

    @Override
    public GroupDTO selGroupInfo(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("GroupDao.selGroupInfo", map);
    }

    @Override
    public int selGroupPaymentCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("GroupDao.selectGroupPaymentCount", map);
    }

    @Override
    public long selGroupPaymentSum(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("GroupDao.selectGroupPaymentSum", map);
    }

    @Override
    public List<GroupDTO> selGroupPaymentList(HashMap<String, Object> map) throws Exception {
        int start = NumberUtils.toInt(String.valueOf(map.get("start")), 0);
        int end = NumberUtils.toInt(String.valueOf(map.get("end")), Integer.MAX_VALUE - 1);
        int offset = start - 1;
        int limit = end - start + 1;

        return sqlSession.selectList("GroupDao.selectGroupPaymentList", map, new RowBounds(offset, limit));
    }

    @Override
    public List<GroupDTO> selGroupPaymentExcelList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("GroupDao.selectGroupPaymentExcelList", map);
    }

    @Override
    public int getCollectorListTotalCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("GroupDao.getCollectorListTotalCount", map);
    }

    @Override
    public List<BankReg01DTO> getCollectorListAll(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("GroupDao.getCollectorListAll", map);
    }
}
