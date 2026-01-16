package com.finger.shinhandamoa.sys.chaMgmt.dao;

import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;
import com.finger.shinhandamoa.sys.setting.dto.ChaUpdateDTO;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SysChaDAOImpl implements SysChaDAO {

	@Inject
	private SqlSession sqlSession;
	
	@Override
	public int selChaListCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.selChaListCnt",  map);
	}

	@Override
	public List<SysChaDTO> selChaList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.selChaList",  map);
	}

	@Override
	public int selNewChaCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.selNewChaCount", map);
	}

	@Override
	public List<SysChaDTO> selNewChaList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.selNewChaList", map);
	}

	@Override
	public int getChangeChaListCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.getChangeChaListCnt", map);
	}

	@Override
	public List<SysChaDTO> getChangeChaList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.getChangeChaList", map);
	}

	@Override
	public SysChaDTO changeChaListInfo(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.changeChaListInfo", map);
	}

	@Override
	public void updateChangeChaInfo(HashMap<String, Object> map) throws Exception {
		sqlSession.update("ChaMgmtDao.updateChangeChaInfo", map);
	}

	@Override
	public void updateChaInfo(HashMap<String, Object> map) throws Exception {
		sqlSession.update("ChaMgmtDao.updateChaInfo", map);
	}

	@Override
	public int selChaCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.selChaCount", map);
	}

	@Override
	public List<SysChaDTO> selChaInfoList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.selChaInfoList", map);
	}

	@Override
	public List<SysChaDTO> selChrInfoList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.selChrInfoList", map);
	}

	@Override
	public int selSmsSeq() throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.selSmsSeq");
	}

	@Override
	public void insertSmsSysList(HashMap<String, Object> map) throws Exception {
		sqlSession.insert("ChaMgmtDao.insertSmsSysList", map);
	}

	@Override
	public void insertSmsSysMng(HashMap<String, Object> map) throws Exception {
		sqlSession.insert("ChaMgmtDao.insertSmsSysMng", map);
	}

	@Override
	public SysChaDTO selectJobhistoryLast(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.selectJobhistoryLast", map);
	}

	@Override
	public List<SysChaDTO> selCloseChaList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.selCloseChaList", map);
	}

	@Override
	public int selCloseChaCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.selCloseChaCount", map);
	}

	@Override
	public void updateJobhistory(HashMap<String, Object> map) throws Exception {
		sqlSession.update("ChaMgmtDao.updateJobhistory", map);
	}

	@Override
	public void insertWebUser(HashMap<String, Object> map) throws Exception {
		sqlSession.insert("ChaMgmtDao.insertWebUser", map);
	}

	@Override
	public void insertXadjgroup(HashMap<String, Object> map) throws Exception {
		sqlSession.insert("ChaMgmtDao.insertXadjgroup", map);
	}

	@Override
	public void deleteXadjgroup(HashMap<String, Object> map) throws Exception {
		sqlSession.delete("ChaMgmtDao.deleteXadjgroup", map);
	}

	@Override
	public void updateSettleAccno(HashMap<String, Object> map) throws Exception {
		sqlSession.update("ChaMgmtDao.updateSettleAccno", map);
	}

    @Override
    public long countGroup(String chaCd, String chaName, String groupId, String groupName, String status) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("chaCd", chaCd);
		paramMap.put("groupId", groupId);
		paramMap.put("groupName", groupName);
		paramMap.put("status", status);
		
		return sqlSession.selectOne("ChaMgmtDao.countGroup", paramMap);
    }

    @Override
    public List<Map<String, Object>> selectGroup(String chaCd, String chaName, String groupId, String groupName, String status, int offset, int limit, String orderBy) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("chaCd", chaCd);
		paramMap.put("chaName", chaCd);
		paramMap.put("groupId", groupId);
		paramMap.put("groupName", groupName);
		paramMap.put("status", status);
		paramMap.put("orderBy", orderBy);
		
		return sqlSession.selectList("ChaMgmtDao.selectGroup", paramMap, new RowBounds(offset, limit));
    }

	@Override
	public long nextChaGroupId() {
		return sqlSession.selectOne("ChaMgmtDao.nextChaGroupId");
	}

	@Override
	public void insertXChaGroup(Map<String, String> chaGroupMap) {
		sqlSession.insert("ChaMgmtDao.insertXChaGroup", chaGroupMap);
	}

	@Override
    public Map<String, Object> selectXChaGroupByGroupId(String groupId) {
        return sqlSession.selectOne("ChaMgmtDao.selectXChaGroupByGroupId", groupId);
    }

	@Override
	public Map<String, Object> selectWebUserByChaCd(String chaCd) {
		return sqlSession.selectOne("ChaMgmtDao.selectWebUserByChaCd", chaCd);
	}

	@Override
	public List<Map<String, Object>> selectXChaListByGroupId(String groupId) {
		return sqlSession.selectList("ChaMgmtDao.selectXChaListByGroupId", groupId);
	}

	@Override
	public void blankChaGroupIdOnXChaList(String chaCd) {
		sqlSession.update("ChaMgmtDao.blankChaGroupIdOnXChaList", chaCd);
	}

	@Override
	public List<Map<String, Object>> selectXChaList(String query, RowBounds rowBounds) {
		return sqlSession.selectList("ChaMgmtDao.selectXChaList", query, rowBounds);
	}

	@Override
	public void updateChaGroupIdOnXChaList(String chaCd, String groupId) {
		final HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("chaCd", chaCd);
		parameter.put("groupId", groupId);
		
		sqlSession.update("ChaMgmtDao.updateChaGroupIdOnXChaList", parameter);
	}

	@Override
	public void updateXChaGroup(Map<String, String> chaGroupMap) {
		sqlSession.update("ChaMgmtDao.updateXChaGroup", chaGroupMap);
	}

	@Override
	public void updateWebUser(HashMap<String, Object> webUserMap) {
		sqlSession.update("ChaMgmtDao.updateWebUser", webUserMap);
	}

	@Override
	public void resetFailCntAdm(HashMap<String, Object> reqMap) throws Exception {
		sqlSession.update("ChaMgmtDao.resetFailCntAdm", reqMap);
	}

	@Override
	public void resetPwAdm(HashMap<String, Object> reqMap) throws Exception {
		sqlSession.update("ChaMgmtDao.resetPwAdm", reqMap);
	}

	@Override
	public void updateSessionMax(HashMap<String, Object> reqMap) throws Exception {
		sqlSession.update("MainDao.updateSessionMax", reqMap);
	}


	@Override
	public int pgUpdateChaInfoListCnt(ChaUpdateDTO body) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.pgUpdateChaInfoListCnt", body);

	}

	@Override
	public List<ChaUpdateDTO> pgUpdateChaInfoList(ChaUpdateDTO body) throws Exception {
		return sqlSession.selectList("ChaMgmtDao.pgUpdateChaInfoList", body);

	}

	@Override
	public void pgUpdateCha(ChaUpdateDTO body) throws Exception {
		sqlSession.update("ChaMgmtDao.pgUpdateCha", body);

	}

	@Override
	public ChaUpdateDTO pgUpdateChaBefore(ChaUpdateDTO body) throws Exception {
		return sqlSession.selectOne("ChaMgmtDao.pgUpdateChaBefore", body);

	}

	@Override
	public void pgUpdateChaAfter(ChaUpdateDTO body) throws Exception {
		 sqlSession.update("ChaMgmtDao.pgUpdateChaAfter", body);

	}

	@Override
	public void deleteXchalist(HashMap<String,Object> map) throws Exception {
		 sqlSession.delete("ChaMgmtDao.deleteXchalist", map);
	}
}
