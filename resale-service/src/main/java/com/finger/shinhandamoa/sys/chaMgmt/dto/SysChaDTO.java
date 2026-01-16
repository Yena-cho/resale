package com.finger.shinhandamoa.sys.chaMgmt.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class SysChaDTO {
    private String fgCd;
    private String chaCd;
    private String chaName;
    private String agtCd;
    private String owner;
    private String ownerTel;
    private String chaOffNo;
    private String chaStatus;
    private String chaType;
    private String chaZipCode;
    private String chaAddress1;
    private String chaAddress2;
    private String chrName;
    private String chaNewRegDt;
    private String chrTelNo;
    private String chrHp;
    private String chrMail;
    private String calDateFrom;
    private String calDateTo;
    private String searchOrderBy;
    private String regDt;
    private String chaCloseChk;
    private String chatrty;
    private String cmsFileName;
    private String preChast;
    private String chast;
    private String daMngMemo;
    private String flag;
    private String searchGb;
    private String searchValue;
    private String rcpSt;
    private String msg;
    private String title;
    private String billGubn;
    private String ecareNo;
    private String jonmun;
    private String chaStItem;
    private String amtchkTy;
    private String accNoCnt;
    private String mchtId; //가맹정코드 (PG)
    private String limitOnce; // 1회한도 (PG)
    private String limitDay; // 1일 한도 (PG)
    private String limitDayCnt; // 1일 입금 횟수 한도 (PG)

    private int rn;
    private int curPage;
    private int pageScale;
    private int cnt;

    private List<String> stList;
    private List<String> valList;
    private List<String> payList;
    private List<String> nmList;

    private String startDate;
    private String EndDate;
    private String jobId;
    private String status;
    private int totCnt;
    private String chaCloseVarDt;
    private String chaCloseVarReson;
    private String chaCloseSt;
    private String masSt;
    private String usePgYn;
    private String pgAprDt;
    private String pgAprMemo;
    private String useSmsYn;
    private String smsAplDt;
    private String smsAplMemo;
    private String pgComName;
    private String pgLicenKey;
    private String pgRevCommiRate;
    private String remark;
    private String loginId;
    private String bnkCd;
    private String finFeeAccNo;
    private String waId;
    private String finFeeOwnNo;
    private String bnkAcptDt;
    private String bnkRegiDt;

    private String pullDt;
    private String typeCd;
    private String typeName;
    private String statusCd;
    private String modifyDt;

    private String chaStName;
    private String rcpCntFee;
    private String rcpBnkFee;
    private String clientStatusName;

    private String clientId;
    private String clientIdNo;
    private String clientName;
    private String clientStatus;
    private String statusName;
    private String brchCode;
    private String agtName;
    private String paymentFeeAmt;
    private String fingerFeeRate;
    private String tobeUseYn;
    private String paymentFinerFeeAmt;

    private String asisClientId;
    private String asisClientIdNo;
    private String asisClientName;
    private String asisClientStatus;
    private String asisStatusName;
    private String asisBrchCode;
    private String asisAgtName;
    private String asisPaymentFeeAmt;
    private String asisFingerFeeRate;
    private String asisUseYn;
    private String asisPaymentFinerFeeAmt;

    private String asisCode1;
    private String asisName1;
    private String asisCode2;
    private String asisName2;
    private String asisCode3;
    private String asisName3;
    private String asisCode4;
    private String asisName4;
    private String asisCode5;
    private String asisName5;
    private String asisCode6;
    private String asisName6;
    private String asisCode7;
    private String asisName7;
    private String asisCode8;
    private String asisName8;
    private String asisCode9;
    private String asisName9;
    private String asisCode10;
    private String asisName10;

    private String tobeCode1;
    private String tobeName1;
    private String tobeCode2;
    private String tobeName2;
    private String tobeCode3;
    private String tobeName3;
    private String tobeCode4;
    private String tobeName4;
    private String tobeCode5;
    private String tobeName5;
    private String tobeCode6;
    private String tobeName6;
    private String tobeCode7;
    private String tobeName7;
    private String tobeCode8;
    private String tobeName8;
    private String tobeCode9;
    private String tobeName9;
    private String tobeCode10;
    private String tobeName10;

    private String agencyStatus;
    private String adjAccYn;

    private String chaSvcDt;
    private String fingerFeeBankCode;
    private String value;
    private String fingerFeeAccountNo;
    private String fingerFeeAccountOwner;
    private String partialPayment;
    private String chaGroupId;
    private String chaGroupName;

    private String usePurpose;
    private String fingerFeeBankValue;
    private String fingerFeePayty;
    private String adjaccyn;
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
    private String maker;
    private int rCnt;

    private List<HashMap<String, Object>> accountList;
    private String accStr;
}
