package com.finger.shinhandamoa.util.dao;

import java.util.Map;

/**
 * @author by puki
 * @date 2018. 5. 10.
 * @desc 최초생성
 */
public interface VarParamInfoDAO {
    // 가변변수 - 고객명
    public String selCusName(Map<String, Object> map) throws Exception;

    // 가변변수 - 청구월
    public String selMasMonth(Map<String, Object> map) throws Exception;

    // 가변변수 - 청구금액
    public String selPayAmt(Map<String, Object> map) throws Exception;

    // 가변변수 - 입금가능기간
    public String selInsDate(Map<String, Object> map) throws Exception;

    // 가변변수 - 모바일청구서
    public String selMobileClaim(Map<String, Object> map) throws Exception;

    // 가변변수 - 납부계좌
    public String selPayVano(Map<String, Object> map) throws Exception;

    // 가변변수 - 미납액
    public String selNonPay(Map<String, Object> map) throws Exception;

    // 가변변수 - 고지서용마감일
    public String selPrintDate(Map<String, Object> map) throws Exception;

    // 가변변수 - 고객구분
    public String selCusGubn(Map<String, Object> map) throws Exception;

    // 가변변수 - 입금액
    public String selAccPay(Map<String, Object> map) throws Exception;

    // 가변변수 - 기관이름
    public String selChaInfo(Map<String, Object> map) throws Exception;

    // 가변변수 - 기관전화번호
    public String selChaTel(Map<String, Object> map) throws Exception;

}
