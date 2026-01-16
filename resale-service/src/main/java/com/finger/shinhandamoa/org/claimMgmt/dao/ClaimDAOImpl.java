package com.finger.shinhandamoa.org.claimMgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;

/**
 * @author by puki
 * @date   2018. 4. 12.
 * @desc   최초생성
 * @version
 * 
 */
@Repository
public class ClaimDAOImpl implements ClaimDAO {

	@Inject
	private SqlSession sqlSession;
	
	@Override
	public String selectClaimMonth(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selectClaimMonth", map);
	}
	
	@Override
	public List<ClaimDTO> selectClaimAll(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectList("ClaimDao.selectClaimList", map);
	}

	@Override
	public int selectClaimTotalCnt(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectOne("ClaimDao.selectClaimTotalCnt", map);
	}

	@Override
	public int selectClaimClientCnt(Map<String, Object> map) throws Exception {

		return sqlSession.selectOne("ClaimDao.selectClaimClientCnt", map);
	}
	
	@Override
	public void deleteClaimXnotimas(Map<String, Object> map) throws Exception {
		sqlSession.delete("ClaimDao.deleteClaimXnotimas", map);
	}
	
	@Override
	public void deleteClaimXnotidet(Map<String, Object> map) throws Exception {
		sqlSession.delete("ClaimDao.deleteClaimXnotidet", map);
	}

	@Override
	public void selClaimDeleteMas(Map<String, Object> map) throws Exception {
		sqlSession.delete("ClaimDao.selClaimDeleteMas", map);
	}

	@Override
	public void selClaimDeleteDet(Map<String, Object> map) throws Exception {
		sqlSession.delete("ClaimDao.selClaimDeleteDet", map);
	}

	@Override
	public ClaimDTO selectClaimDetail(String notiMasCd) throws Exception {
		return sqlSession.selectOne("ClaimDao.selectDetailClaim", notiMasCd);
	}

	@Override
	public long selClaimRegSum(Map<String, Object> map) throws Exception {

		return sqlSession.selectOne("ClaimDao.selClaimRegSum", map);
	}

	@Override
	public void updateClaimTempMas(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateClaimTempMas", map);
	}
	
	@Override
	public void updateClaimTempDet(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateClaimTempDet", map);
	}

	@Override
	public void claimMasUpdate(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.claimMasUpdate", map);
	}

	@Override
	public void claimDetUpdate(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.claimDetUpdate", map);
	}

	@Override
	public ClaimDTO selDetToMasNo(String notiDetCd) throws Exception {
		return sqlSession.selectOne("ClaimDao.selDetToMasNo", notiDetCd);
	}

	@Override
	public List<ClaimDTO> selMasDetNoList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.selMasDetNo", map);
	}

	@Override
	public List<ClaimDTO> searchCusName(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.searchCusName", map);
	}

	@Override
	public int searchCusNameCnt(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.searchCusNameCnt", map);
	}

	@Override
	public ClaimDTO searchCustomer(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selCustomerInfo", map);
	}

	@Override
	public ClaimDTO selPayItem(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selPayItem", map);
	}

	@Override
	public void insClaimRegMas(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.insClaimRegMas", map);
	}
	
	@Override
	public void insClaimRegDet(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.insClaimRegDet", map);
	}

	@Override
	public int selClaimRegConfirm(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selClaimRegConfirm", map);
	}

	@Override
	public List<ClaimDTO> selItemList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.selClaimItem",  map);
	}

	@Override
	public void claimItemDelete(String notiDetCd) throws Exception {
		sqlSession.delete("ClaimDao.deleteItem", notiDetCd);
	}

	@Override
	public List<ClaimDTO> selBeforeMasYear(String chaCd) throws Exception {
		return sqlSession.selectList("ClaimDao.selMasYear", chaCd);
	}

	@Override
	public List<ClaimDTO> selBeforeMasMonth(String chaCd, String year) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chaCd", chaCd);
		map.put("year", year);
		
		return sqlSession.selectList("ClaimDao.selMasMonth", map);
	}

	@Override
	public void beforeXnotimasInsert(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.copyXnotimas", map);
	}

	@Override
	public void beforeXnotidetInsert(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.copyXnotidet", map);
	}

	@Override
	public String autoSmsSendYn(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.notSmsYn", map);
	}

	@Override
	public List<NotiSendDTO> autoSmsList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.autoSmsClaimSendData",  map);
	}

	@Override
	public void updateCancelMasAll(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateCancelMas", map);
	}

	@Override
	public void updateCancelDetAll(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateCancelDet", map);
	}

	@Override
	public ClaimDTO selNotiMasCd(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selNotiMasCd", map);
	}

	@Override
	public String selectNotiMasCd(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selectNotiMasCd", map);
	}

	@Override
	public int selDupPayItem(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selDupPayItem", map);
	}

	@Override
	public void updateClaimNotiMas(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateClaimNotiMas", map);
	}

	@Override
	public void updateClaimNotiDet(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateClaimNotiDet", map);
	}

	@Override
	public String selctMasMonth(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selctMasMonth", map);
	}

	@Override
	public int updateMasBundle(Map<String, Object> map) throws Exception {
		return sqlSession.update("ClaimDao.updateMasBundle", map);
	}

	@Override
	public int updateDetBundle(Map<String, Object> map) throws Exception {
		return sqlSession.update("ClaimDao.updateDetBundle", map);
	}

	@Override
	public int selectClaimDetCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selectClaimDetCount", map);
	}

	@Override
	public int selectClaimCancelCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selectClaimCancelCount", map);
	}

	@Override
	public List<ClaimDTO> selectClaimDetList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.selectClaimDetList", map);
	}

	@Override
	public List<ClaimDTO> selectPayItemDetailView(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.selectPayItemDetailView", map);
	}

	@Override
	public int selDetCdCnt(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.selDetCdCnt", map);
	}

	@Override
	public int useClaimCnt(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.useClaimCnt", map);
	}

	@Override
	public void selClaimDeleteDetAll(HashMap<String, Object> smap) throws Exception {
		sqlSession.delete("ClaimDao.selClaimDeleteDetAll", smap);
	}

	@Override
	public void selClaimDeleteDetB(HashMap<String, Object> smap) throws Exception {
		sqlSession.delete("ClaimDao.selClaimDeleteDetB", smap);
	}

	@Override
	public void updateClaimDay(Map<String, Object> map) throws Exception {
		sqlSession.update("ClaimDao.updateClaimDay", map);
	}

	@Override
	public void updateCusPreClaim(HashMap<String, Object> cmap) throws Exception {
		sqlSession.update("ClaimDao.updateCusPreClaim", cmap);
	}

	@Override
	public String autoAtSendYn(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimDao.notAtYn", map);
	}

	@Override
	public List<NotiSendDTO> autoAtList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimDao.autoAtClaimSendData",  map);
	}
}
