package com.finger.shinhandamoa.org.claimMgmt.dao;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;

import java.util.List;
import java.util.Map;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
public interface ClaimExcelDAO {

    // 엑셀파일양식 다운로드 샘플데이터 - 전체고객선택
    public List<ClaimDTO> excelList(Map<String, Object> map) throws Exception;

    // 엑셀파일양식 다운로드 샘플데이터 - 전월기준
    public List<ClaimDTO> excelBeforeList(Map<String, Object> map) throws Exception;

    // 청구 대량 등록 테이블 DELETE
    public void claimFailDelete(String chaCd) throws Exception;

    // 청구 대향 등록 실패건 등록 - excel
    public void claimExcelFailInsert(Map<String, Object> map) throws Exception;

    // 청구 대량 등록 실패 목록
    public List<ClaimDTO> failList(String chaCd, int start, int end) throws Exception;

    // 청구 대량 등록 실패 목록 - total count
    public int failTotalCount(String chaCd) throws Exception;

    // 청구 실패 내역 엑셀 다운로드
    public List<ClaimDTO> failExcelList(Map<String, Object> map) throws Exception;

    // 청구 중복 확인
    public String selClaimConfirm(Map<String, Object> map) throws Exception;

    // 청구 대상 목록 - 파일 저장
    public List<ClaimDTO> excelSave(Map<String, Object> map) throws Exception;

    // 청구원장번호
    public String selNotiMasCd(Map<String, Object> map) throws Exception;

    // 납부제외고객 확인
    public int selPcpGubn(Map<String, Object> map) throws Exception;

    // 엑셀파일양식 다운로드 샘플데이터  - 전체고객선택 항목컬럼
    public List<ClaimDTO> excelItemList(Map<String, Object> map) throws Exception;

    // 엑셀파일양식 다운로드 샘플데이터  - 전체고객선택 고객목록
    public List<ClaimDTO> excelCusList(Map<String, Object> map) throws Exception;

    // 엑셀파일양식 다운로드 샘플데이터 - 전월고객
    public List<ClaimDTO> selExcelBeforeMonth(Map<String, Object> map) throws Exception;

    // 청구 대량 등록
    public int bulkUploadExcel(List<Map<String, Object>> list, int rowNo, int rowNum) throws Exception;

    public List<ClaimItemDTO> selectDetailsForExcel(String notimasCd) throws Exception;

    public List<ClaimDTO> excelMasterList(Map<String, Object> map) throws Exception;

    public List<ClaimDTO> selectClaimFailMasterExcel(String chaCd) throws Exception;

    public List<ClaimItemDTO> selectFailDetailsForExcel(Map<String, Object> map) throws Exception;

    ClaimDTO listForExcelTrans(Map<String, Object> map) throws Exception;
}
