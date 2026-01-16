package com.finger.shinhandamoa.org.receiptmgmt;

import com.finger.shinhandamoa.org.receiptmgmt.dto.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("receiptMgmtDao")
public class ReceiptMgmtDAO {

    @Inject
    private SqlSession sqlSession;

    /**
     * 수납관리 조회
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getReceiptList(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getReceiptList", map);
    }

    /**
     * 수납관리 조회 카운트
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getReceiptCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getReceiptCount", map);
    }

    /**
     * 카드결제 취소(환불)
     *
     * @param map
     * @return
     */
    public int insertRepay(Map<String, Object> map) throws Exception {
        return sqlSession.insert("ReceiptMgmt.insertRepay", map);
    }

    /**
     * 청구상태 변경 환불
     *
     * @param map
     * @return
     * @throws Exception
     */
    public int updateXnotiMasSt(Map<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateXnotiMasSt", map);
    }

    /**
     * notidet 청구상태 변경 환불
     *
     * @param map
     * @return
     * @throws Exception
     */
    public int updateXnotiDetSt(Map<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateXnotiDetSt", map);
    }

    /**
     * 영수증 출력시 XCHAOPTION 테이블 조회해서 refcd 값 추출
     *
     * @param chaCd
     * @return
     */
    public String getXchaoption(String chaCd) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getXchaoption", chaCd);
    }

    /**
     * 영수증 조회
     *
     * @param map
     * @return
     */
    public List<PdfReceiptMgmtDTO> selectRcpBillCutDet(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.selectRcpBillCutDet", map);
    }

    /**
     * 영수증 조회
     *
     * @param map
     * @return
     */
    public List<PdfReceiptMgmtDTO> selectRcpBillCut(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.selectRcpBillCut", map);
    }

    /**
     * 교육비납입증명서
     *
     * @param map
     * @return
     */
    public List<PdfReceiptMgmtDTO> getEduList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getEduList", map);
    }


    /**
     * 교육비납입증명서
     *
     * @param vano              가상계좌번호
     * @param paymentYear       수납년도
     * @return
     */
    public List<PdfReceiptDetailsDTO> selectEduDetailsList(String vano, String paymentYear, ArrayList<String> checkList) throws Exception {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("vano", vano);
        paramMap.put("paymentYear", paymentYear);
        paramMap.put("checkList", checkList);

        return sqlSession.selectList("ReceiptMgmt.selectEduDetailsList", paramMap);
    }

    /**
     * 장기요양급여 납부확인서
     *
     * @param map
     * @return
     */
    public List<PdfReceiptMgmtDTO> getRecpList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getRecpList", map);
    }

    /**
     * 장기요양 계좌 리스트
     *
     * @param map
     * @return
     */
    public List<PdfReceiptMgmtDTO> getRecpAccNoList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getRecpAccNoList", map);
    }

    /**
     * 기부리스트
     *
     * @param map
     * @return
     */
    public List<PdfReceiptMgmtDTO> getDntnList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getDntnList", map);
    }

    /**
     * 수납내역 조회
     *
     * @param reqMap
     * @return
     * @throws Exception
     */
    public ReceiptMgmtDTO selectRcpMas(HashMap<String, Object> reqMap) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.selectRcpMas", reqMap);
    }

    /**
     * 카드취소대상 원장코드 조회
     *
     * @param map
     * @return
     * @throws Exception
     */
    public ReceiptMgmtDTO getCancelRcp(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getCancelRcp", map);
    }

    /**
     * 수납정보 수정
     *
     * @param map
     * @return
     * @throws Exception
     */
    public int updateRcpMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateRcpMas", map);
    }

    /**
     * 수납상세항목 수정
     *
     * @param map
     * @return
     * @throws Exception
     */
    public void updateRcpDet(HashMap<String, Object> map) throws Exception {
        sqlSession.update("ReceiptMgmt.updateRcpDet", map);
    }

    /**
     * 고지정보 수정
     *
     * @param map
     * @return
     * @throws Exception
     */
    public void updateNotiBill(HashMap<String, Object> map) throws Exception {
        sqlSession.update("ReceiptMgmt.updateNotiMas", map);
        sqlSession.update("ReceiptMgmt.updateNotiDet", map);
    }

    /**
     * 현장수납 카운트
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getActCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getActCount", map);
    }

    /**
     * notimascd로 수납마스터 찾기
     *
     * @param map
     * @return
     */
    public ReceiptMgmtDTO getRcpCd(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getRcpCd", map);
    }

    /**
     * 가상계좌 금액
     *
     * @param map
     * @return
     */
    public ReceiptMgmtDTO getVasAmt(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getVasAmt", map);
    }

    /**
     * 현장수납 카운트 완납
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getActCount2(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getActCount2", map);
    }

    /**
     * 현장수납 리스트
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getActList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getActList", map);
    }

    /**
     * 현장수납 리스트 완납
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getActList2(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.getActList2", map);
    }

    /**
     * 현장수납관리 수납테이블 저장
     *
     * @param map
     * @return
     */
    public int insertXrcpMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.insertXrcpMas", map);
    }

    /**
     * 현장수납관리 수납상세테이블 저장
     *
     * @param map
     * @return
     */
    public int insertXrcpDet(HashMap<String, Object> map) throws Exception {
        return sqlSession.insert("ReceiptMgmt.insertXrcpDet", map);
    }

    /**
     * 현장수납관리 수납테이블 수정
     *
     * @param map
     * @return
     */
    public int updateXrcpMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateXrcpMas", map);
    }

    /**
     * 현장수납관리 수납상세테이블 수정
     *
     * @param map
     * @return
     */
    public int updateXrcpDet(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateXrcpDet", map);
    }

    /**
     * 현장수납관리 청구테이블 update
     *
     * @param map
     * @return
     */
    public int updateXnotiMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateXnotiMas", map);
    }

    /**
     * 현장수납관리 청구상세테이블 update
     *
     * @param map
     * @return
     */
    public int updateXnotiDet(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateXnotiDet", map);
    }

    /**
     * 청구상세 항목 list
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getXnotiDetList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getXnotiDetList", map);
    }

    /**
     * 시퀀스 가져오기
     *
     * @return
     */
    public ReceiptMgmtDTO getSeq(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getSeq", map);
    }

    /**
     * 환불가능 내역 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getReplyYList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getReplyYList", map);
    }

    /**
     * 환불가능 내역 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getReplyNList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getReplyNList", map);
    }

    /**
     * 환불완료 내역 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getEndReplyYList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getEndReplyYList", map);
    }

    /**
     * 환불완료 내역 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getEndReplyNList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getEndReplyNList", map);
    }

    /**
     * 환불가능 내역 카운트 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getReplyYCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getReplyYCount", map);
    }

    /**
     * 환불가능 내역 카운트 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getReplyNCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getReplyNCount", map);
    }

    /**
     * 환불완료 내역 카운트 조회 AMTCHKTY :[IR방식:Y]
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getEndReplyYCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getEndReplyYCount", map);
    }

    /**
     * 환불완료 내역 카운트 조회 AMTCHKTY :[SET방식:N]
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getEndReplyNCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getEndReplyNCount", map);
    }

    /**
     * 수납테이블에서 RCPMASCD 가져오기
     *
     * @param str
     * @return
     */
    public String getRcpMasCd(String str) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getRcpMasCd", str);
    }

    /**
     * 현금영수증 내역 카운트
     *
     * @param map
     * @return
     */
    public HashMap<String, Object> getCashMasCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getCashMasCount", map);
    }

    /**
     * 현금영수증 내역 리스트
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getCashMasList(Map<String, Object> map) throws Exception {
        List<ReceiptMgmtDTO> receiptMgmtDTOS = sqlSession.selectList("ReceiptMgmt.getCashMasList", map);
        return receiptMgmtDTOS;
    }
    
    /**
     * 현금영수증 내역 리스트(국세청 양식)
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getFWCashReceiptMasterList(Map<String, Object> map) throws Exception {
        List<ReceiptMgmtDTO> receiptMgmtDTOS = sqlSession.selectList("ReceiptMgmt.getFWCashReceiptMasterList", map);
        return receiptMgmtDTOS;
    }

    /**
     * 현금영수증 발행
     */
    public int insertCashMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.insertCashMas", map);
    }

    /**
     * 현금영수증 재발행
     */
    public int updateCashMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateCashMas", map);
    }

    /**
     * 현금영수증 발행 취소
     */
    public int deleteCashMas(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.deleteCashMas", map);
    }

    /**
     * 현금영수증 요청 취소
     */
    public int noDeleteIssue(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.noDeleteIssue", map);
    }

    /**
     * 현금영수증 발행 취소건 취소
     */
    public int reInsertIssueDelete(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.reInsertIssueDelete", map);
    }

    public int doInsertIssueDelete(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.doInsertIssueDelete", map);
    }

    public int updateCashMasU(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateCashMasU", map);
    }

    public int updateCashMasJobNull(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("ReceiptMgmt.updateCashMasJobNull", map);
    }

    /**
     * 청구항목 상세보기
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getReceiptDetail(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getReceiptDetail", map);
    }

    /**
     * 현금영수증 사전 데이터
     *
     * @param vaNo
     * @return
     */
    public NotiDTO getCashData(String vaNo) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getCashData", vaNo);
    }

    /**
     * 현금영수증 수정
     *
     * @param map
     * @return
     */
    public int insertCashMaster(Map<String, Object> map) throws Exception {
        return sqlSession.insert("ReceiptMgmt.insertCashMaster", map);
    }

    /**
     * 현금영수증 인서트
     *
     * @param map
     * @return
     */
    public int updateCashMaster(Map<String, Object> map) throws Exception {
        return sqlSession.insert("ReceiptMgmt.updateCashMaster", map);
    }

    public int deleteCashMaster(Map<String, Object> map) throws Exception {
        return sqlSession.delete("ReceiptMgmt.deleteCashMaster", map);
    }

    /**
     * 청구항목으로 수납상세 조회
     *
     * @param map
     * @return
     */
    public ReceiptMgmtDTO getXrcpDetList(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getXrcpDetList", map);
    }

    /**
     * 직접수납 상태 확인
     *
     * @param map
     * @return
     */
    public List<ReceiptMgmtDTO> getXnotiState(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getXnotiState", map);
    }

    /**
     * 변경 전 금액
     *
     * @param map
     * @return
     */
    public ReceiptMgmtDTO getXrcpAmt(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getXrcpAmt", map);
    }

    /**
     * XRCPDET 금액
     *
     * @param map
     * @return
     */
    public ReceiptMgmtDTO getRcpAmt(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.getRcpAmt", map);
    }

    /**
     * 수납마스터 삭제
     *
     * @param map
     * @return
     */
    public int deleteRcpmas(Map<String, Object> map) throws Exception {
        return sqlSession.delete("ReceiptMgmt.deleteRcpmas", map);
    }

    /**
     * 수납상세 삭제
     *
     * @param map
     * @return
     */
    public int deleteRcpdet(Map<String, Object> map) throws Exception {
        return sqlSession.delete("ReceiptMgmt.deleteRcpdet", map);
    }

    public ReceiptMgmtDTO selectCashMas(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.selectCashMas", map);
    }

    public List<ReceiptMgmtDTO> getChaMasCd(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.getChaMasCd", map);
    }

    /**
     * 미발행인 현금영수증 건이 있을경우 (UPDATE용 SELECT)
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<ReceiptMgmtDTO> selectFlagCashMas(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.selectFlagCashMas", map);
    }

    public ReceiptMgmtDTO selectCusMasByRcpMasCd(String rcpMasCd) {
        return sqlSession.selectOne("ReceiptMgmt.selectCusMasByRcpMasCd", rcpMasCd);
    }

    public Map<String, Object> selectNoticeMasterByRcpMasCd(String rcpMasCd) {
        return sqlSession.selectOne("ReceiptMgmt.selectNoticeMasterByRcpMasCd", rcpMasCd);
    }

    public List<Map<String, Object>> selectNoticeDetailsByRcpMasCd(String rcpMasCd) {
        return sqlSession.selectList("ReceiptMgmt.selectNoticeDetailsByRcpMasCd", rcpMasCd);
    }

    public HashMap<String, Object> getPayItemListCnt(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("ReceiptMgmt.selectPayItemListCnt", map);
    }

    public List<PayMgmtDTO> getPayItemList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.selectPayItemList", map);
    }

    public List<PayMgmtDTO> getPayItemExcelList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("ReceiptMgmt.selectPayItemExcelList", map);
    }

    public List<PdfReceiptMgmtDTO> getAdmRecpList(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.getAdmRecpList", map);
    }

    public List<PdfReceiptMgmtDTO> selectAdmBillCut(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.selectAdmBillCut", map);
    }

    public List<PdfReceiptMgmtDTO> getAdmEduList(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.getAdmEduList", map);
    }

    public List<PdfReceiptMgmtDTO> getAdmRcpAccNoList(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.getAdmRcpAccNoList", map);
    }

    public List<PdfReceiptMgmtDTO> getAdmDntnList(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.getAdmDntnList", map);
    }

    public List<PdfReceiptDetailsDTO> selectAdmEduDetailsList(String vano, String paymentYear, ArrayList<String> checkList, String amtchkty) {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("vano", vano);
        paramMap.put("paymentYear", paymentYear);
        if("N".equals(amtchkty)){
            paramMap.put("rcpMasList", checkList);
        }else{
            paramMap.put("notiMasList", checkList);
        }

        return sqlSession.selectList("ReceiptMgmt.selectAdmEduDetailsList", paramMap);
    }

    public List<PdfReceiptMgmtDTO> selectAdmBillCutWithoutNotice(HashMap<String, Object> map) {
        return sqlSession.selectList("ReceiptMgmt.selectAdmBillCutWithoutNotice", map);
    }

    public List<FwCashReceiptMasterDTO> getCashHistList(HashMap<String,Object> map) {
        return sqlSession.selectList("ReceiptMgmt.selectCashHistList", map);
    }

    public HashMap<String, Object> countCashHistList(HashMap<String,Object> map) {
        return sqlSession.selectOne("ReceiptMgmt.countCashHistList", map);
    }
}
