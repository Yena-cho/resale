package kr.co.finger.damoa.scheduler.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.finger.damoa.scheduler.model.LayOut;

/**
 * @author  by puki
 * @date    2018. 5. 13.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class BillsSendDaoImpl implements BillsSendDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<LayOut> selChaCd(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("scheduler.selChaCd", map);
	}
	
	@Override
	public LayOut printMsg(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("scheduler.printMsg", map);
	}
	
	@Override
	public List<LayOut> printMsgData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("scheduler.printMsgData", map);
	}

	@Override
	public List<LayOut> printItemData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("scheduler.printItemData", map);
	}

	@Override
	public List<LayOut> printReqData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("scheduler.printReqData", map);
	}

	@Override
	public void updateReqSt(HashMap<String, Object> map) throws Exception {
		sqlSession.update("scheduler.updateReqSt", map);
	}
	
	@Override
	public List<LayOut> remarkMsgData(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("scheduler.remarkMsgData", map);
	}
}
