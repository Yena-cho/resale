package com.finger.shinhandamoa.sys.setting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;

/**
 * @author  by puki
 * @date    2018. 5. 23.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class AutoTranMgmtDAOImpl implements AutoTranMgmtDAO {

	@Inject
	private SqlSession sqlSession;

	@Override
	public int selAutoTranCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoTranCnt", map);
	}

	@Override
	public List<AutoDTO> selAutoTranList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AutoTran.selAutoTranList", map);
	}

	@Override
    public List<Map<String, Object>> findNewAutomaticWithdrawal(HashMap<String, Object> map) {
        return sqlSession.selectList("AutoTran.findNewAutomaticWithdrawal", map);
    }

	@Override
	public void updateEBI13Result(Map<String, Object> map) {
		map.put("CMS_REQ_ST", "CST03");
		sqlSession.update("AutoTran.updateEB13Result", map);
	}

	@Override
	public int selAutoTranTargetCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoTargetCnt", map);
	}

	@Override
	public List<AutoDTO> selAutoTranTarget(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AutoTran.selAutoTargetList", map);
	}

	@Override
	public void applyFinish(HashMap<String, Object> map) throws Exception {
		sqlSession.update("AutoTran.applyFinish", map);
	}

	@Override
	public int selAccSumCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAccSumCnt", map);
	}

	@Override
	public List<AutoDTO> selAccSumList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AutoTran.selAccSumList", map);
	}

	@Override
	public List<Map<String, Object>> findMonthlyAutomaticWithdrawal(String month) {
		return sqlSession.selectList("AutoTran.findMonthlyAutomaticWithdrawal", month);
	}

	@Override
	public void updateEB21Result(Map<String, Object> map) {
		sqlSession.update("AutoTran.updateEB21Result", map);
	}

	@Override
	public int selAutoProcOrgCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoProcOrgCnt", map);
	}

	@Override
	public List<AutoDTO> selAutoProcOrgList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AutoTran.selAutoProcOrg", map);
	}

	@Override
	public int selAutoProcAccCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoProcAccCnt", map);
	}

	@Override
	public List<AutoDTO> selAutoProcAccList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AutoTran.selAutoProcAcc", map);
	}

	@Override
	public int selAutoOrgFailCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoOrgFailCnt", map);
	}

	@Override
	public int selAutoAccFailCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoAccFailCnt", map);
	}
	
	@Override
    public void updateEB14Result(Map<String, Object> map) {
        sqlSession.update("AutoTran.updateEB14Result", map);
    }
	
	@Override
    public void updateEB22Result(Map<String, Object> map, String failReson) {
		map.put("failReson", failReson);
        sqlSession.update("AutoTran.updateEB22Result", map);	// 실패
    }

	@Override
	public void updateCmsReq(Map<String, Object> map) throws Exception {
		sqlSession.update("AutoTran.updateCmsReq", map);
	}

	@Override
	public void updateEB22Success(Map<String, Object> map) {
		sqlSession.update("AutoTran.updateEB22Success", map);	// 성공		
	}

	@Override
	public int selAutoTranRegCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.selAutoTranRegCount", map);
	}

	@Override
	public List<AutoDTO> selAutoTranRegList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AutoTran.selAutoTranRegList", map);
	}

	@Override
	public String getCmsFileName(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AutoTran.getCmsFileName", map);
	}

    @Override
    public void UpdateAutoTranSt(HashMap<String, Object> map) throws Exception {
        sqlSession.update("AutoTran.UpdateAutoTranSt", map);
    }

	@Override
	public void UpdateFeeTranSt(HashMap<String, Object> map) throws Exception {
		sqlSession.update("AutoTran.UpdateFeeTranSt", map);
	}

    @Override
    public void insertAutoTranFeeInfo(Map<String, Object> map) throws Exception {
		sqlSession.insert("AutoTran.insertAutoTranFeeInfo", map);
    }
}
