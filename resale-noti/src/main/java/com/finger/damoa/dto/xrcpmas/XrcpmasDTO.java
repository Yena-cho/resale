package com.finger.damoa.dto.xrcpmas;

import lombok.Data;

@Data
public class XrcpmasDTO {

    private String svecd; // 수납방법코드 (VAS-가상계좌, DCS-창구현금(현장), DCD-창구카드(현장), DVA-기관계좌입금

    private String rcpMasCd; // 수납원장코드

    private String chaCd; // 기관코드

    private String masMonth; // 청구월

    private String cusName; // 고객명

    private String vano; // 가상계좌번호

    private String rcpAmt; // 수납금액

    private String payDay; // 은행전문 수납일자

    private String rcpUsrName; // 출금계좌 성명

    private String rcpMasSt; // 수납상태(수납:PA03, 취소:PA09)
}