package com.finger.damo.rcpcusttestserver.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class XrcpmasDTO {

    private String rcpMasCd; // 수납원장코드

    private String rcpMasSt; // 수납상태(수납:PA03, 취소:PA09)

    private String payDay; // 은행전문 수납일자

    private String payTime; // 은행전문 수납시간

    private String rcpAmt; // 수납금액

    private String rcpUsrName; // 출금계좌 성명

    private String chaCd; // 기관코드

    private String vano; // 가상계좌번호

    private String svecd; // 수납방법코드 (VAS-가상계좌, DCS-창구현금(현장), DCD-창구카드(현장), DVA-기관계좌입금

//    private String masMonth; // 청구월

//    private String cusName; // 고객명
}