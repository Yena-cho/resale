package com.finger.shinhandamoa.sys.rcpMgmt.dao;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAdminClosingDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SysAdminClosingDAO {

	/* 일마감 건수 */
	int getDayCloseCount(Map<String, Object> map);

	/* 월마감 건수 */
	int getMonthCloseCount(Map<String, Object> map);
	
	/* 일마감 내역 */
	List<SysAdminClosingDTO> getDayCloseList(Map<String, Object> map) throws Exception;
	
	/* 월마감 내역 */
	List<SysAdminClosingDTO> getMonthCloseList(Map<String, Object> map);
	
	/* 일마감 내역 */
	void dayCloseGo(Map<String, Object> map);
	
	/* 월마감 내역 */
	void monthCloseGo(Map<String, Object> map);

    int countMonthlySettle(String month, String clientId, String clientName);

	List<Map<String,Object>> selectMonthlySettle(String month, String clientId, String clientName, String orderBy, RowBounds rowBounds);

	/* 가상계좌 정산 count */
	List<SysAdminClosingDTO> getVirtualAccountCalculateCount(HashMap<String, Object> map) throws Exception;

	/* 가상계좌 정산 List */
	List<SysAdminClosingDTO> getVirtualAccountCalculateList(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountCalculateListExcel(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountSettleMas(HashMap<String, Object> map) throws Exception;

	/* 가상계좌 정산 결과 처리 > 성공 */
	void updateVirtualAccountSettleMas(HashMap<String, Object> map);

	/* 가상계좌 정산 결과 처리 > 이력 추가 */
	void insertVirtualAccountSettleDet(HashMap<String, Object> map);

	List<SysAdminClosingDTO> getVirtualAccountSettleDetCount(HashMap<String, Object> map) throws Exception;

	List<SysAdminClosingDTO> getVirtualAccountSettleDetList(HashMap<String, Object> map) throws Exception;
}
