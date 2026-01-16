package com.finger.shinhandamoa.org.notimgmt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtPdfResDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtResDTO;

public interface NotiMgmtService {

    /**
     * 세션에 넣는 값(기관관리자)
     *
     * @param user
     * @return
     * @throws Exception
     */
    public NotiMgmtBaseDTO selectNotiBaseInfo(String user) throws Exception;

    /**
     * 고지서출력/조회 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<NotiMgmtResDTO> getNotiMgmtList(Map<String, Object> map) throws Exception;

    /**
     * 고지서출력/조회 카운트
     *
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getNotiMgmtTotalCount(Map<String, Object> map) throws Exception;

    /**
     * 고지서 출력양식 조회
     *
     * @param map
     * @return
     * @throws Exception
     */
    public String getExistsBillForm(HashMap<String, Object> map) throws Exception;

    /**
     * pdf 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<NotiMgmtPdfResDTO> getNotiBillList(HashMap<String, Object> map) throws Exception;

    public List<NotiMgmtPdfResDTO> getPrivewBillList(HashMap<String, Object> map) throws Exception;

}
