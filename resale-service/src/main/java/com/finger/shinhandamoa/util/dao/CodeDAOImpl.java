package com.finger.shinhandamoa.util.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.util.dto.CodeDTO;


/**
 * @author by puki
 * @date 2018. 4. 13.
 * @desc 공통코드 DAOImpl
 */
@Repository
public class CodeDAOImpl implements CodeDAO {

    @Inject
    private SqlSession sqlSession;

    // 청구항목
    @Override
    public List<CodeDTO> claimItemCd(String chaCd) throws Exception {
        return sqlSession.selectList("CodeDao.claimItemSel", chaCd);
    }

    @Override
    public List<CodeDTO> cusGubnCd(String chaCd) throws Exception {
        return sqlSession.selectList("CodeDao.cusGubnCdSel", chaCd);
    }

    @Override
    public int cusGubnCdCnt(String chaCd) throws Exception {
        return sqlSession.selectOne("CodeDao.cusGubnCdCnt", chaCd);
    }

    @Override
    public List<CodeDTO> moneyPassbookName(String chaCd) throws Exception {
        return sqlSession.selectList("CodeDao.moneyPassbookName", chaCd);
    }

    @Override
    public List<CodeDTO> claimItemForExcelTrans(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("CodeDao.claimItemForExcelTrans", map);
    }
}
