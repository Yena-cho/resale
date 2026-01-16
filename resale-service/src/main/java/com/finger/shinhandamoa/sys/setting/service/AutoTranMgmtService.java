package com.finger.shinhandamoa.sys.setting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;

import kr.co.finger.msgio.msg.EB13;
import kr.co.finger.msgio.msg.EB14;
import kr.co.finger.msgio.msg.EB21;
import kr.co.finger.msgio.msg.EB22;

/**
 * @author  by puki
 * @date    2018. 5. 23.
 * @desc    최초생성
 * @version 
 * 
 */
public interface AutoTranMgmtService {

	// 자동이체 신청관리 total count
	public int selAutoTranCount(HashMap<String, Object> map) throws Exception;
	// 자동이체 신청관리 목록
	public List<AutoDTO> selAutoTranList(HashMap<String, Object> map) throws Exception;
	// 신청파일 생성
	public List<Map<String, Object>> findNewAutomaticWithdrawal(HashMap<String, Object> map) throws Exception;
	// 신청 update
	public void updateEBI13Result(EB13 eb13) throws Exception;
	// 자동이체 대상생성 total count
	public int selAutoTranTargetCnt(HashMap<String, Object> map) throws Exception;
	// 자동이체 대상성성 목록
	public List<AutoDTO> selAutoTranTarget(HashMap<String, Object> map) throws Exception;
	// 금결월 신청완료
	public void applyFinish(HashMap<String, Object> map) throws Exception;
	// 자동이체 대상생성 - 수수료 출금 total count
	public int selAccSumCnt(HashMap<String, Object> map) throws Exception;
	// 자동이체 대상생성 - 수수료 출금
	public List<AutoDTO> selAccSumList(HashMap<String, Object> map) throws Exception;
	// 자동이체 대상생성 - 수수료 출금 
	public List<Map<String, Object>> findMonthlyAutomaticWithdrawal(String month);
	// 자동이체 대상생성 - 수수료출금 update
	public void updateEB21Result(EB21 eb21, String previousMonth) throws Exception;
	// 자동이체 처리현황 total count - 신규기관
	public int selAutoProcOrgCnt(HashMap<String, Object> map) throws Exception;
	// 자동이체 처리현황 목록 - 신규기관
	public List<AutoDTO> selAutoProcOrgList(HashMap<String, Object> map) throws Exception;
	// 자동이체 처리현황 total count - 수수료출금
	public int selAutoProcAccCnt(HashMap<String, Object> map) throws Exception;
	// 자동이체 처리현황 목록 - 신규기관
	public List<AutoDTO> selAutoProcAccList(HashMap<String, Object> map) throws Exception;
	// 자동이체  처리현황 불능건수 - 신규기관
	public int selAutoOrgFailCnt(HashMap<String, Object> map) throws Exception;
	// 자동이체 처리현황 불능건수 - 수수료출금
	public int selAutoAccFailCnt(HashMap<String, Object> map) throws Exception;
	// 자동이체 처리현황 - 파일 업로드 후 update EB14
	public void updateEB14Result(EB14 eb14) throws Exception;
	// 자동이체 처리현황 - 파일 업로드 후 update EB22
	public void updateEB22Result(EB22 eb22, String previousMonth) throws Exception;
	// 자동이체 신청관리 - cms상태 수정
	public void updateCmsReq(Map<String, Object> map) throws Exception;
	//자동이체 동의관리 리스트 카운트
    int selAutoTranRegCount(HashMap<String,Object> map) throws Exception;
	//자동이체 동의관리 리스트
	List<AutoDTO> selAutoTranRegList(HashMap<String,Object> map) throws Exception;
	//CSM보기용 파일이름 가져오기
    String getCmsFileName(HashMap<String,Object> map) throws Exception;

    void UpdateAutoTranSt(HashMap<String,Object> map) throws Exception;

	void UpdateFeeTranSt(HashMap<String,Object> map) throws Exception;

    void insertAutoTranFeeInfo(Map<String,Object> map) throws Exception;
}
