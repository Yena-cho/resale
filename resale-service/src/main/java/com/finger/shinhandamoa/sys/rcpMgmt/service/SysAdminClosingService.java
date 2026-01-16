package com.finger.shinhandamoa.sys.rcpMgmt.service;

import com.finger.shinhandamoa.common.PageBounds;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAdminClosingDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SysAdminClosingService {
	
	int getDayCloseCount(Map<String, Object> map);

	int getMonthCloseCount(Map<String, Object> map);

	List<SysAdminClosingDTO> getDayCloseList(Map<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getMonthCloseList(Map<String, Object> map);
	
	/* 일마감 실행 */
	void dayCloseGo(Map<String, Object> map);
	
	/* 월마감 실행 */
	void monthCloseGo(Map<String, Object> map);

    int countMonthlySettle(String month, String clientId, String clientName);

	List<Map<String,Object>> getMonthlySettleList(String month, String clientId, String clientName, String pageBounds, PageBounds orderBy);

	/* 가상계좌 정산 count */
	List<SysAdminClosingDTO> getVirtualAccountCalculateCount(HashMap<String, Object> map) throws Exception;

	/* 가상계좌 정산 List */
	List<SysAdminClosingDTO> getVirtualAccountCalculateList(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountCalculateListExcel(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountSettleMas(HashMap<String, Object> map) throws Exception;

	/* 가상계좌 정산 결과 처리 > 성공 */
	void updateVirtualAccountSettleMas(HashMap<String, Object> map) throws Exception;

	/* 가상계좌 정산 결과 처리 > 이력 추가 */
	void insertVirtualAccountSettleDet(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountSettleDetCount(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountSettleDetList(HashMap<String, Object> map) throws Exception;
}
