package com.finger.shinhandamoa.sys.chaMgmt.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class SysChaMgmtDTO {
//    private String chacd;
    private String chaCd;
    private String chaName;
    private String owner;
    private String ownerTel;
    private String chaOffNo;
    private String usePurpose;
    private String chaStatus;
    private String chatrty;
    private String chast;
    private String chaType;
    private String chrName;
    private String chrTelNo;
    private String chrHp;
    private String chaZipCode;
    private String chaAddress1;
    private String chaAddress2;
    private String chrMail;
    private String fingerFeeAccountNo;
    private String fingerFeeBankCode;
    private String fingerFeeBankValue;
    private String fingerFeePayty;
    private String adjaccyn;
    private String amtChkTy;
    private String partialPayment;
    private String rcpDueChk;
    private String cusNameTy;
    private String rcpReqYn;
    private String mandRcpYn;
    private String rcpReqTy;
    private String noTaxYn;
    private String rcpReqSveTy;
    private String mnlRcpReqTy;
    private long notMinLimit;
    private long notMinFee;
    private long notCntFee;
    private long rcpCntFee;
    private long rcpBnkFee;
    private String daMngMemo;
    private String fgCd;
    private String maker;
    private String regDt;
    private String loginId;
    private int rCnt;
    private String accNoCnt;

    private List<HashMap<String, Object>> accountList;
    private String accStr;

    //필수항목
    private String rcpdtdupyn;      // 납부기간중복[Y:허용, N:불가, P:부분납]
    private String usesmsyn;        // 기관 SMS 발송기능 사용여부 [N:미사용,  W:신청중, Y:사용가능, C:취소
    private String notsmsyn;        // 청구시SMS전송방식(M:수동,   N:미사용,  A:자동 )
    private String rcpsmsyn;        // 납부시SMS전송여부(Y:사용 , N:미사용)
    private String rcpsmsty;        // 납부시SMS전송방식(M:수동,   N:미사용,  A:자동 )
    private String usemailyn;       // 전자메일사용여부
    private String cusgubnyn1;      // 고지서출력여부1
    private String cusgubnyn2;      // 고지서출력여부2
    private String cusgubnyn3;      // 고지서출력여부3
    private String cusgubnyn4;      // 고지서출력여부4
    private String chasvcyn;        // 약관동의여부
    private String billimgty;       // 고지서로고위치
    private String usepgyn;         // 온라인 카드결제 사용여부
    private String chkcash;         // 현금영수증신청서수령여부
    private String chkoff;          // 사업자등록증수령여부
    private String mchtId;          // 사업자등록증수령여부

    // 수납통지 기관의 수신정보
    private String rcpNoticeUrl;     // 수납통지 수신 URL(고객사의 수납수신 API 주소)
    private String chaApiUrlQuery;   // 수취조회 API URL (고객사의 수취조회 API 주소)
    private String chaApiUrlNotice;  // 입금통지 API URL (고객사의 수취조회 API 주소)
//    private String rcpNoticeUseYn;  // 수납통지 사용여부


    private int rn;
    private int curPage;
    private int pageScale;
    private int cnt;
}
