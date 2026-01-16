package com.finger.shinhandamoa.org.custMgmt.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
public interface CustExcelDAO {
    // 고객등록 목록 - 파일 저장
    public List<CustReg01DTO> selectExcelSaveCustReg(Map<String, Object> map) throws Exception;

    // 엑셀파일양식 다운로드 샘플데이터 - 항목선택
    public List<CustReg01DTO> excelList(Map<String, Object> map) throws Exception;

    // 고객 대량 등록 - 고객서
    public void custExcelInsert(CustReg01DTO dto) throws Exception;

    // 고객 대량 등록 - 고객서 상세
    public void custDetailExcelInsert(CustReg01DTO dto) throws Exception;

    public void custDetExcelInsert(CustReg01DTO dto) throws Exception;

    // 고객 대량 등록 테이블 DELETE
    public void custFailDelete(String chaCd) throws Exception;

    // 고객 대향 등록 실패건 등록 - excel
    public void custExcelFailInsert(CustReg01DTO dto) throws Exception;

    // 고객 대량 등록 실패 목록
    public List<CustReg01DTO> failList(String chaCd, int start, int end) throws Exception;

    // 고객 대량 등록 실패 목록 - total count
    public int failTotalCount(String chaCd) throws Exception;

    // 고객 실패 내역 엑셀 다운로드
    public List<CustReg01DTO> failExcelList(Map<String, Object> map) throws Exception;

    // 고객 대량등록 - 중복 확인
    public int selDupCusInfo(Map<String, Object> map) throws Exception;

    // 고객 정보 수정
    public void custExcelUpdate(Map<String, Object> map) throws Exception;
}
