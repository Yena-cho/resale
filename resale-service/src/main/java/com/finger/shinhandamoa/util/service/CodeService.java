package com.finger.shinhandamoa.util.service;

import java.util.HashMap;
import java.util.List;

import com.finger.shinhandamoa.util.dto.CodeDTO;

/**
 * @author by puki
 * @date 2018. 4. 13.
 * @desc 공통코드 Service
 */
public interface CodeService {

    // 청구항목
    public List<CodeDTO> claimItemCd(String chaCd) throws Exception;

    // 구분항목
    public List<CodeDTO> cusGubnCd(String chaCd) throws Exception;

    // 구분항목 count
    public int cusGubnCdCnt(String chaCd) throws Exception;

    // 입금통장명
    public List<CodeDTO> moneyPassbookName(String chaCd) throws Exception;

    List<CodeDTO> claimItemForExcelTrans(HashMap<String, Object> map) throws Exception;
}
