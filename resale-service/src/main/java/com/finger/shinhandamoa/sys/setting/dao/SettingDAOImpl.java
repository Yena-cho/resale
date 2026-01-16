package com.finger.shinhandamoa.sys.setting.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.sys.setting.dto.AdminInfoDTO;


/**
 * @author  by puki
 * @date    2018. 5. 21.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class SettingDAOImpl implements SettingDAO {

	@Inject
	private SqlSession sqlSession;
	
	@Override
	public int selAccAuthCnt(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("AccAuth.selAccAuthCnt", map);
	}

	@Override
	public List<AdminInfoDTO> selAccAuthList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("AccAuth.selAccAuthList", map);
	}

	@Override
	public AdminInfoDTO selAccAuthDetail(String admId) throws Exception {
		return sqlSession.selectOne("AccAuth.selAccDetail", admId);
	}

	@Override
	public void accAuthModify(HashMap<String, Object> map) throws Exception {
		sqlSession.update("AccAuth.accAuthModify", map);
	}

	@Override
	public void accAuthDelete(String admId) throws Exception {
		sqlSession.delete("AccAuth.accAuthDel", admId);
	}

	@Override
	public int selAdmId(String admId) throws Exception {
		return sqlSession.selectOne("AccAuth.selAdmId", admId);
	}

}
