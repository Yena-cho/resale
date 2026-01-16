package com.finger.shinhandamoa.org.receiptmgmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.org.receiptmgmt.dto.RefundRcpDTO;

@Repository("directReceiptMgmtDAO")
public class DirectReceiptMgmtDAO {

	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 청구 마지막 년월 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 21.
	 */
	public Map<String, String> selectNoticeMasMaxMonth(Map<String, Object> params) throws Exception {
		return sqlSession.selectOne("DirectReceiptMgmt.selectNoticeMasMaxMonth", params);
	}
	
	/**
	 * 직접수납관리 > 수납등록 목록조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 12.
	 */
	public List<ReceiptMgmtDTO> selectDirectNoticeList(Map<String, Object> params) throws Exception {
        return sqlSession.selectList("DirectReceiptMgmt.selectDirectNoticeList", params);
    }
	
	/**
	 * 직접수납관리 > 수납등록 건수조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 12.
	 */
	public Map<String, Object> selectDirectNoticeListCount(Map<String, Object> params) throws Exception {
		return sqlSession.selectOne("DirectReceiptMgmt.selectDirectNoticeListCount", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 목록조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 12.
	 */
	public List<ReceiptMgmtDTO> selectCompleteNoticeList(Map<String, Object> params) throws Exception {
		return sqlSession.selectList("DirectReceiptMgmt.selectCompleteNoticeList", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 건수조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 12.
	 */
	public Map<String, Object> selectCompleteNoticeListCount(Map<String, Object> params) throws Exception {
		return sqlSession.selectOne("DirectReceiptMgmt.selectCompleteNoticeListCount", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 취소 > 청구마스터 상태 미납으로 변경
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 13.
	 */
	public int updateXnotiMasSt(Map<String, Object> params) throws Exception {
		return sqlSession.update("DirectReceiptMgmt.updateXnotiMasSt", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 취소 > 청구상세 상태 미납으로 변경
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 13.
	 */
	public int updateXnotiDetSt(Map<String, Object> params) throws Exception {
		return sqlSession.update("DirectReceiptMgmt.updateXnotiDetSt", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 취소 > 수납마스터 상태 취소로 변경
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 13.
	 */
	public int updateXrcpMasSt(Map<String, Object> params) throws Exception {
		return sqlSession.update("DirectReceiptMgmt.updateXrcpMasSt", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 취소 > 수납상세 상태 취소로 변경
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 13.
	 */
	public int updateXrcpDetSt(Map<String, Object> params) throws Exception {
		return sqlSession.update("DirectReceiptMgmt.updateXrcpDetSt", params);
	}
	
	/**
	 * 직접수납관리 > 수납완료 취소 > 현금영수증 삭제로 변경
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 17.
	 */
	public int updateXcashMasDisabled(Map<String, Object> params) throws Exception {
		return sqlSession.update("DirectReceiptMgmt.updateXcashMasDisabled", params);
	}
	
	/**
	 * 직접수납관리 > 수기환불내역 > 환불등록 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 20.
	 */
	public List<RefundRcpDTO> selectRcpCatalog(Map<String, Object> params) throws Exception {
		return sqlSession.selectList("DirectReceiptMgmt.selectRcpCatalog", params);
	}
	
	/**
	 * 직접수납관리 > 수기환불내역 > 환불등록 목록 건수 및 총금액
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 20.
	 */
	public Map<String, Object> selectRcpCatalogCount(Map<String, Object> params) throws Exception {
		return sqlSession.selectOne("DirectReceiptMgmt.selectRcpCatalogCount", params);
	}
	
	/**
	 * 직접수납관리 > 수기환불내역 > 환불완료 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 20.
	 */
	public List<RefundRcpDTO> selectRefundRcpCatalog(Map<String, Object> params) throws Exception {
		return sqlSession.selectList("DirectReceiptMgmt.selectRefundRcpCatalog", params);
	}
	
	/**
	 * 직접수납관리 > 수기환불내역 > 환불완료 목록 건수 및 총금액
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 9. 20.
	 */
	public Map<String, Object> selectRefundRcpCatalogCount(Map<String, Object> params) throws Exception {
		return sqlSession.selectOne("DirectReceiptMgmt.selectRefundRcpCatalogCount", params);
	}
	
	/**
	 * 직접수납관리 > 수기환불내역 > 환불완료 취소시 환불내역 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @param rcpdetcd 
	 * @modified 2018. 9. 21.
	 */
	public int deleteRefundCatalog(String repaymascd, String rcpmascd, String rcpdetcd) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("repaymascd", repaymascd);
		params.put("rcpmascd", rcpmascd);
		params.put("rcpdetcd", rcpdetcd);
		return sqlSession.delete("DirectReceiptMgmt.deleteXrepaymasByPK", params);
	}
}
