package com.finger.shinhandamoa.sys.setting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.setting.dto.TransInfoDTO;
import com.finger.shinhandamoa.sys.setting.dto.VanoMgmt2DTO;
import com.finger.shinhandamoa.sys.setting.dto.VanoMgmtDTO;
import com.finger.shinhandamoa.sys.setting.dto.VanoPgDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public interface VanoMgmtService {
	// 청구월별수납현황
	public VanoMgmtDTO getSysMainInfo01(Map<String, Object> map) throws Exception;

	/* 가상계좌관리-보유현황목록조회 */
	public int getVano01ListTotalCount(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmtDTO> getVano01ListAll(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmtDTO> getVano01ListExcel(HashMap<String, Object> map) throws Exception;

	/* 가상계좌관리-가상계좌발급요청관리 */
	public List<VanoMgmtDTO> getVanoIssuedListExcel(HashMap<String, Object> map) throws Exception;

	/* 가상계좌관리-기관별가상계좌보유현황조회 */
	public List<VanoMgmt2DTO> getChaVanoListTotalCount(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmt2DTO> getChaVanoListAll(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmt2DTO> getChaVanoListExcel(HashMap<String, Object> map) throws Exception;
	public void insertVirtualAccount(HashMap<String, Object> map) throws Exception;

	public void deleteVirtualAccount() throws Exception;


	public int getTempVanoAccount(HashMap<String, Object> map) throws Exception;

	public void csvUploadResultInsert(HashMap<String, Object> map) throws Exception;




	/* 가상계좌관리-가상계좌 거래내역조회 */
	public int getVanoTranHisListTotalCount(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmtDTO> getVanoTranHisListAll(HashMap<String, Object> map) throws Exception;	
	public List<VanoMgmtDTO> getVanoTranHisListExcel(HashMap<String, Object> map) throws Exception;

	/* 가상계좌관리-발급현황목록조회 */
	public int getVanoIssuedListTotalCount(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmtDTO> getVanoIssuedListAll(HashMap<String, Object> map) throws Exception;
	public List<VanoMgmtDTO> getMisassignVanoCount() throws Exception;

	//가상계좌원장등록
	public void insertValist(HashMap<String, Object> map) throws Exception;

	// 가상계좌 발급관리 요청 new
	public void doVanoIssueReq(HashMap<String, Object> map) throws Exception;

	// 가상계좌입금거래전문 카운트
    int getVanoTranChkListCnt(HashMap<String,Object> reqMap) throws Exception;

	// 가상계좌입금거래전문 리스트
	List<TransInfoDTO> getVanoTranChkList(HashMap<String,Object> reqMap) throws Exception;

	// 가상계좌개시거래전문 카운트
    int getVanoTranInitChkCnt(HashMap<String,Object> reqMap) throws Exception;

	// 가상계좌개시거래전문 리스트
	List<TransInfoDTO> getVanoTranInitChk(HashMap<String,Object> reqMap) throws Exception;

	int pgVanoSendListCnt(VanoPgDTO dto) throws Exception;
	//가상계좌 전송 내역 리스트
	List<VanoPgDTO> pgVanoSendList(VanoPgDTO dto) throws Exception;

	//가상계좌 전송후 상태값변경
	void updateSendListStat(Map<String,Object> dto) throws Exception;

}
