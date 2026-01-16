package com.finger.shinhandamoa.org.claimMgmt.dao;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author  by puki
 * @date    2018. 4. 13.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class ClaimItemDAOImpl implements ClaimItemDAO {

	@Inject
	private SqlSession sqlSession;
	
	@Override
	public List<ClaimItemDTO> claimItemList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimItemDao.selectClaimItem", map);
	}

	@Override
	public int claimItemTotalCount(String chaCd) throws Exception {
		return sqlSession.selectOne("ClaimItemDao.selectClaimItemCnt", chaCd);
	}
	
	@Override
	public List<ClaimItemDTO> moneyPassbookList(String chaCd) throws Exception {
		return sqlSession.selectList("ClaimItemDao.selectAccNum", chaCd);
	}

	@Override
	public void deleteClaimItem(Map<String, Object> map) throws Exception {
		sqlSession.delete("ClaimItemDao.updateItemST02", map);
	}

	@Override
	public ClaimItemDTO detailClaimItem(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimItemDao.detailClaimItem", map);
	}

	@Override
	public void modifyClaimItem(ClaimItemDTO claimItem) throws Exception {
		sqlSession.update("ClaimItemDao.modifyClaimItem", claimItem);
	}

	@Override
	public void updatePtritemOrder(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimItemDao.updatePtritemOrder", map);
	}

	@Override
	public String payItemNameCheck(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimItemDao.payItemNameCheck", map);
	}

	@Override
	public void updatePayItem(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimItemDao.updatePayItem", map);		
	}

	@Override
	public int cusPayItemCnt(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimItemDao.cusPayItemCnt", map);
	}

	@Override
	public String selXadjGroup(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimItemDao.selXadjGroup", map);
	}

}
