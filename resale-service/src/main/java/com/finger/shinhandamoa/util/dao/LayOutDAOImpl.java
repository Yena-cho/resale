package com.finger.shinhandamoa.util.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.util.dto.LayOutDTO;

/**
 * @author  by puki
 * @date    2018. 5. 13.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class LayOutDAOImpl implements LayOutDAO {

	@Inject
	private SqlSession sqlSession;

	@Override
	public List<LayOutDTO> selChaCd(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("MsgDao.selChaCd", map);
	}
	
	@Override
	public LayOutDTO printMsg(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("MsgDao.printMsg", map);
	}
	
	@Override
	public List<LayOutDTO> printMsgData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("MsgDao.printMsgData", map);
	}

	@Override
	public List<LayOutDTO> printItemData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("MsgDao.printItemData", map);
	}

	@Override
	public List<LayOutDTO> printReqData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("MsgDao.printReqData", map);
	}

	@Override
	public void updateReqSt(HashMap<String, Object> map) throws Exception {
		sqlSession.update("MsgDao.updateReqSt", map);
	}

	@Override
	public List<LayOutDTO> remarkMsgData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("MsgDao.remarkMsgData", map);
	}

}
