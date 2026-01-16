package com.finger.shinhandamoa.sys.addServiceMgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XNotimasreqDTO;

/**
 * @author  
 * @date    
 * @desc    
 * @version 
 * 
 */
@Repository
public class AddServiceMgmtDAOImpl implements AddServiceMgmtDAO {

	@Inject
	private SqlSession sqlSession;

	@Override
	public List<XNotimasreqDTO> notiPrintListAll(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("AdditionalService.notiPrintListAll", map);
	}

	@Override
	public int notiPrintCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("AdditionalService.notiPrintCount", map);
	}

	@Override
	public int notiReqPrintCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("AdditionalService.notiReqPrintCount", map);
	}

	@Override
	public void notiPrintUpdate(Map<String, Object> map) throws Exception {
		sqlSession.update("AdditionalService.notiPrintUpdate", map);
	}

	@Override
	public void quickPrint(Map<String, Object> map) throws Exception {
		sqlSession.update("AdditionalService.quickPrint", map);
	}

	@Override
	public void rePrint(Map<String, Object> map) throws Exception {
		sqlSession.update("AdditionalService.rePrint", map);
	}

	@Override
	public HashMap<String, Object> smsRegListCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("AddServiceMgmt.smsRegListCount", reqMap);
	}

	@Override
	public HashMap<String, Object> smsRegListWaitCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("AddServiceMgmt.smsRegListWaitCount", reqMap);
	}
	
	@Override
	public List<AddServiceMgmtDTO> smsRegList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("AddServiceMgmt.smsRegList", reqMap);
	}

	@Override
	public int failNotiPrintCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("AdditionalService.failNotiPrintCount", map);
	}

	@Override
	public AddServiceMgmtDTO getCallerNum(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("AddServiceMgmt.getCallerNum", reqMap);
	}

	public void updateContents(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.updateContents", reqMap);
	}

	public void updateSmsSendTel(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.updateSmsSendTel", reqMap);
	}

	@Override
	public HashMap<String, Object> cardPayHistoryCount(HashMap<String, Object> reqMap) throws Exception{
		return sqlSession.selectOne("AddServiceMgmt.cardPayHistoryCount", reqMap);
	}

	@Override
	public List<AddServiceMgmtDTO> cardPayHistoryList(HashMap<String, Object> reqMap) throws Exception{
		return sqlSession.selectList("AddServiceMgmt.cardPayHistoryList", reqMap);
	}

	@Transactional
	@Override
	public void updateUseSmsYn(HashMap<String, Object> reqMap) throws Exception {
		sqlSession.update("AddServiceMgmt.updateUseSmsYn", reqMap);
		sqlSession.update("AddServiceMgmt.updateDay1", reqMap);
	}

	@Override
	public AddServiceMgmtDTO smsRegFileInfo(HashMap<String, Object> reqMap) {
		return sqlSession.selectOne("AddServiceMgmt.smsRegFileInfo", reqMap);
	}
	
	//문자신청서 삭제
	@Override
	public void updateSmsRegInfo(HashMap<String, Object> reqMap) {
		// board update
		sqlSession.update("AddServiceMgmt.updateSmsRegInfo", reqMap);
		// xchalist status update
		sqlSession.update("AddServiceMgmt.updateSmsRegSt", reqMap);
	}
	
	//해지
	@Override
	public void deleteUseSmsYn(HashMap<String, Object> reqMap) {
		// board update
//		sqlSession.update("AddServiceMgmt.updateSmsRegInfo", reqMap);
		// xchalist status update
		sqlSession.update("AddServiceMgmt.deleteUseSmsYn", reqMap);
	}

	@Override
	public HashMap<String, Object> pastRcpHistListCount(HashMap<String, Object> reqMap) throws Exception {
		if(reqMap.get("statusCheck").equals("rcpMas")){
			return sqlSession.selectOne("AddServiceMgmt.pastRcpHistListMasCount", reqMap);
		}else{
			return sqlSession.selectOne("AddServiceMgmt.pastRcpHistListDetCount", reqMap);
		}
	}

	@Override
	public List<AddServiceMgmtDTO> pastRcpHistList(HashMap<String, Object> reqMap) throws Exception {
		if(reqMap.get("statusCheck").equals("rcpMas")){
			return sqlSession.selectList("AddServiceMgmt.pastRcpHistListMas", reqMap);
		}else{
			return sqlSession.selectList("AddServiceMgmt.pastRcpHistListDet", reqMap);
		}
	}

	@Override
	public HashMap<String, Object> pastPaymentHistListCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("AddServiceMgmt.pastPaymentHistListCount", reqMap);
	}

	@Override
	public List<AddServiceMgmtDTO> pastPaymentHistList(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("AddServiceMgmt.pastPaymentHistList", reqMap);
	}

	@Override
	public List<AddServiceMgmtDTO> atRegList(HashMap<String, Object> reqMap) throws Exception{
		return sqlSession.selectList("AddServiceMgmt.atRegList", reqMap);
	}

	@Override
	public HashMap<String, Object> atRegListCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("AddServiceMgmt.atRegListCount", reqMap);
	}

	@Override
	public HashMap<String, Object> atRegListWaitCount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("AddServiceMgmt.atRegListWaitCount", reqMap);
	}

	@Override
	public void updateAtChaName(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.updateAtChaName", reqMap);
	}

	@Override
	public void updateAtChaNameHis(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.updateAtChaNameHis", reqMap);
	}

	@Override
	public void updateAtAcptDt(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.updateAtAcptDt", reqMap);
	}

	@Override
	public void updateAtAcptHis(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.updateAtAcptHis", reqMap);
	}

	@Override
	public void deleteAtYn(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.deleteAtYn", reqMap);
	}

	@Override
	public void deleteAtYnHis(HashMap<String, Object> reqMap) throws Exception{
		sqlSession.update("AddServiceMgmt.deleteAtYnHis", reqMap);
	}

	@Override
	public List<AddServiceMgmtDTO> atUseList(HashMap<String, Object> reqMap) {
		return sqlSession.selectList("AddServiceMgmt.atUseList", reqMap);
	}

	@Override
	public HashMap<String, Object> atUseListCount(HashMap<String, Object> reqMap) {
		return sqlSession.selectOne("AddServiceMgmt.atUseListCount", reqMap);
	}

	@Override
	public HashMap<String, Object> atUseListSuccessCount(HashMap<String, Object> reqMap) {
		return sqlSession.selectOne("AddServiceMgmt.atUseListSuccessCount", reqMap);
	}

	@Override
	public HashMap<String, Object> atUseListFailCount(HashMap<String, Object> reqMap) {
		return sqlSession.selectOne("AddServiceMgmt.atUseListFailCount", reqMap);
	}
}
