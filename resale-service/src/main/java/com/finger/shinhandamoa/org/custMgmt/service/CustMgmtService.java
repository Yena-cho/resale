package com.finger.shinhandamoa.org.custMgmt.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;

/**
 * @author 홍길동
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
public interface CustMgmtService {

    // 고객관리-고객등록01목록조회
    public int custReg01TotalCount(HashMap<String, Object> map) throws Exception;

    public List<CustReg01DTO> custReg01ListAll(HashMap<String, Object> map) throws Exception;

    // 고객관리 > 고객등록 > 작성중인 내역 삭제
    public void deleteXcusMas(HashMap<String, Object> map) throws Exception;

    // 고객관리 > 고객등록 > 선택삭제
    public void deleteCusInfo(HashMap<String, Object> map) throws Exception;

    public String insertXCustMas(CustReg01DTO dto) throws Exception;

    public void updateXcusMas(CustReg01DTO dto) throws Exception;

    // 고객관리 > 고객등록 > 정상등록
    public void updateCusmas(HashMap<String, Object> map) throws Exception;

    public CustReg01DTO selectDetailCustReg(HashMap<String, Object> map) throws Exception;

    /*고객관리 - 가상계좌조회 */
    public CustReg01DTO getVanoInfo(HashMap<String, Object> map) throws Exception;

    /*고객관리 - 가상계좌사용여부업데이트 */
    public void updateValist(CustReg01DTO dto) throws Exception;

    // 고객명 수정시 청구원장 고객명 수정(미납)
    public void updateNotiMasCusName(HashMap<String, Object> map) throws Exception;

    // 현금영수증 정보 업데이트
    public int updateCashMasInfo(HashMap<String, Object> map) throws Exception;
}
