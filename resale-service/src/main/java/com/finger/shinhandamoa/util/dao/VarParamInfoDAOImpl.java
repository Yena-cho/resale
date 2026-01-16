package com.finger.shinhandamoa.util.dao;

import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

/**
 * @author by puki
 * @date 2018. 5. 10.
 * @desc 최초생성
 */
@Repository
public class VarParamInfoDAOImpl implements VarParamInfoDAO {

    @Inject
    private SqlSession sqlSession;

    @Override
    public String selCusName(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selCusName", map);
    }

    @Override
    public String selMasMonth(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selMasMonth", map);
    }

    @Override
    public String selPayAmt(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selPayAmt", map);
    }

    @Override
    public String selInsDate(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selInsDate", map);
    }

    @Override
    public String selMobileClaim(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selCusName", map);
    }

    @Override
    public String selPayVano(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selPayVano", map);
    }

    @Override
    public String selNonPay(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selNonPay", map);
    }

    @Override
    public String selPrintDate(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selPrintDate", map);
    }

    @Override
    public String selCusGubn(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selCusGubn", map);
    }

    @Override
    public String selChaInfo(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selChaInfo", map);
    }

    @Override
    public String selChaTel(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selChaTel", map);
    }

    @Override
    public String selAccPay(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("MsgDao.selAccPay", map);
    }

}
