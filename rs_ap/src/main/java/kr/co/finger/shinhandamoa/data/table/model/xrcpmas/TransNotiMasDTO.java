package kr.co.finger.shinhandamoa.data.table.model.xrcpmas;

import lombok.Data;

@Data
public class TransNotiMasDTO {

    private String rcpMasCd; // 수납원장코드

    private String rcpMasSt; // 수납상태 (수납:PA03, 취소:PA09)

    private String payDay; // 은행전문 수납일자

    private String payTime; // 은행전문 수납시간

    private int rcpAmt; // 수납금액

    private String rcpUsrName; // 출금계좌 성명

    private String chaCd; // 기관코드

    private String vano; // 가상계좌번호

//    private String cusName; // 고객명

    private String svecd; // 수납방법코드 (VAS-가상계좌, DCS-창구현금(현장), DCD-창구카드(현장), DVA-기관계좌입금



//    private String notiMasCd; // 청구원장코드

//    private String masMonth; // 청구월

//    private String masDay; // 청구일자

    private int tranTotCnt; // 수납통지 총 발생횟수

    private String tranFstDt; // 수납통지 최초발송일시

    private String tranLastDt; // 수납통지 최종발송일시

    private String tranSuccYn; // 수납통지 성공여부: Y(성공), N(실패)

}