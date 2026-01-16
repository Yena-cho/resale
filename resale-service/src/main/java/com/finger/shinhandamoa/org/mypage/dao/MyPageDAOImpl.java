package com.finger.shinhandamoa.org.mypage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.mypage.dto.MyPageDTO;


/**
 * @author
 * @date
 * @desc
 */
@Repository
public class MyPageDAOImpl implements MyPageDAO {

    @Inject
    private SqlSession sqlSession;

    @Override
    public MyPageDTO getSmsConfig(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectOne("MyPage.getSmsConfig", reqMap);
    }

    @Override
    public void updateSmsNoti(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateSmsNoti", reqMap);
    }

    @Override
    public MyPageDTO selectChaInfo(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectOne("MyPage.selectChaInfo", reqMap);
    }

    @Override
    public void updateOrgInfo(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateOrgInfo", reqMap);
    }

    @Override
    public void updateCusGubn(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateCusGubn", reqMap);
    }

    @Override
    public void updateRcpChk(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateRcpChk", reqMap);
    }

    @Override
    public void updateCashRcp(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateCashRcp", reqMap);
    }

    @Override
    public List<MyPageDTO> selectBillForm(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("MyPage.selectBillForm", map);
    }

    @Override
    public void insertBillForm(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.insert("MyPage.insertBillForm", reqMap);
    }

    @Override
    public void updateBillForm(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.insert("MyPage.updateBillForm", reqMap);
    }

    @Override
    public MyPageDTO getBillConfig(HashMap<String, Object> reqMap) throws Exception {
        if ("01".equals(reqMap.get("billGubn"))) {
            return sqlSession.selectOne("MyPage.getBillConfig1", reqMap);
        } else if ("02".equals(reqMap.get("billGubn"))) {
            return sqlSession.selectOne("MyPage.getBillConfig2", reqMap);
        } else if ("03".equals(reqMap.get("billGubn"))) {
            return sqlSession.selectOne("MyPage.getBillConfig3", reqMap);
        } else {
            return null;
        }
    }

    @Override
    public String selectPwd(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectOne("MyPage.selectPwd", reqMap);
    }

    @Override
    public void updatePwd(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updatePwd", reqMap);
    }

    @Override
    public List<MyPageDTO> adjList(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectList("MyPage.adjList", reqMap);
    }

    @Override
    public void updateUsePgYn(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateUsePgYn", reqMap);
    }

    @Override
    public void updateOrgAccInfo(HashMap<String, Object> map) throws Exception {
        sqlSession.update("MyPage.updateOrgAccInfo", map);

    }

    @Override
    public void updateFailCnt(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateFailCnt", reqMap);
    }

    @Override
    public int selectFailCnt(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectOne("MyPage.selectFailCnt", reqMap);
    }

    @Override
    public MyPageDTO getAtConfig(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectOne("MyPage.getAtConfig", reqMap);
    }

    @Override
    public void updateAtNoti(HashMap<String, Object> reqMap) throws Exception {
        sqlSession.update("MyPage.updateAtNoti", reqMap);
    }


}
