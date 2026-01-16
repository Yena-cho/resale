package damoa.comm.tele;

import damoa.Constants;
import damoa.comm.log.GeneLog;
import damoa.comm.util.ComCheck;
import damoa.comm.util.DateUtil;
import damoa.server.entity.DamoaMsg;

public class DataTele {

    /*
     *  데이터부 작성
     */
    public static String makeDataTele(byte[] rcvDByte, DamoaMsg dMsg, boolean dataYN, int msgSize) {

        String sHeadYN = "N";
        if (dataYN) sHeadYN = "Y";

        String result_cd    = dMsg.getResult_cd();
        String result_msg   = dMsg.getResult_msg();

        return new String(rcvDByte,0,10,Constants.MY_CHARSET) + sHeadYN + result_cd + result_msg +
                new String(rcvDByte,45,msgSize-47,Constants.MY_CHARSET) + "\r\n";
    }


    /* 출금 데이터부 체크
     *
     * return boolean
     */
    public static boolean checkPayDataTele(byte[] rcvDByte, DamoaMsg dMsg, String custId,
                                           int msgSize, GeneLog gLog, boolean testFlag) {

        dMsg.setdRecordType(new String(rcvDByte,0,1,Constants.MY_CHARSET).trim());
        dMsg.setChaCd(new String(rcvDByte,1,8,Constants.MY_CHARSET).trim());
        dMsg.setWorkField(new String(rcvDByte,9,1,Constants.MY_CHARSET).trim());      		// 처리내역
        dMsg.setTrNo(new String(rcvDByte,45,30,Constants.MY_CHARSET).trim().toUpperCase());   // 청구거래번호
        dMsg.setVano(new String(rcvDByte,75,20,Constants.MY_CHARSET).trim());    				// 가상계좌번호
        dMsg.setPayMasMonth(new String(rcvDByte,95,6,Constants.MY_CHARSET).trim());     		// 청구월
        dMsg.setPayMasDt(new String(rcvDByte,101,8,Constants.MY_CHARSET).trim());    			// 청구일자
        dMsg.setRcpStartDt(new String(rcvDByte,109,8,Constants.MY_CHARSET).trim());    		// 수납시작일
        dMsg.setRcpStartTm(new String(rcvDByte,117,4,Constants.MY_CHARSET).trim());    		// 수납시작시각
        dMsg.setRcpEndDt(new String(rcvDByte,121,8,Constants.MY_CHARSET).trim());    			// 수납종료일
        dMsg.setRcpEndTm(new String(rcvDByte,129,4,Constants.MY_CHARSET).trim());    			// 수납종료시각
        dMsg.setRcpPrtEndDt(new String(rcvDByte,133,8,Constants.MY_CHARSET).trim());    		// 출력납부마감일
        dMsg.setRcpPrtEndTm(new String(rcvDByte,141,4,Constants.MY_CHARSET).trim());    		// 출력납부마감시각
        dMsg.setCusNm(new String(rcvDByte,145,30,Constants.MY_CHARSET).trim());   			// 고객명
        dMsg.setSmsYn(new String(rcvDByte,175,1,Constants.MY_CHARSET).trim());    			// sms전송여부
        dMsg.setMobileNo(new String(rcvDByte,176,12,Constants.MY_CHARSET).trim());   			// 휴대번호
        dMsg.setEmailYn(new String(rcvDByte,188,1,Constants.MY_CHARSET).trim());    			// email전송여부
        dMsg.setEmail(new String(rcvDByte,189,50,Constants.MY_CHARSET).trim());   			// email
        dMsg.setContent1(new String(rcvDByte,239,30,Constants.MY_CHARSET).trim());   			// 참조1
        dMsg.setContent2(new String(rcvDByte,269,30,Constants.MY_CHARSET).trim());   			// 참조2
        dMsg.setContent3(new String(rcvDByte,299,30,Constants.MY_CHARSET).trim());   			// 참조3
        dMsg.setContent4(new String(rcvDByte,329,30,Constants.MY_CHARSET).trim());   			// 참조4
        dMsg.setRegSeq(new String(rcvDByte,359,9,Constants.MY_CHARSET).trim());				// 전문순번
        dMsg.setChaTrNo(new String(rcvDByte,420,30,Constants.MY_CHARSET).trim().toUpperCase());   // 항목거래번호
        dMsg.setAdjfiGrpCd(new String(rcvDByte,450,5,Constants.MY_CHARSET).trim());    			// 신한은행발급배분키
        dMsg.setPayItemNm(new String(rcvDByte,455,50,Constants.MY_CHARSET).trim());   			// 항목명
        dMsg.setPayItemAmt(new String(rcvDByte,505,10,Constants.MY_CHARSET).trim());   			// 항목금액
        dMsg.setPayItemYN(new String(rcvDByte,515,1,Constants.MY_CHARSET).trim());    			// 항목필수여부
        dMsg.setRcpItemYN(new String(rcvDByte,516,1,Constants.MY_CHARSET).trim());    			// 현금영수증발행여부
        dMsg.setRcpBusinessNo(new String(rcvDByte,517,10,Constants.MY_CHARSET).trim());   		// 현금영수증발행사업자번호
        dMsg.setRcpPersonNo(new String(rcvDByte,527,13,Constants.MY_CHARSET).trim());   			// 현금영수증발행사업자번호
        dMsg.setPrtPayItemNm(new String(rcvDByte,540,50,Constants.MY_CHARSET).trim());   			// 출력항목명
        dMsg.setPrtContent1(new String(rcvDByte,590,50,Constants.MY_CHARSET).trim());   			// 출력비고내용
        dMsg.setPrtSeq(new String(rcvDByte,640,2,Constants.MY_CHARSET).trim());    			// 출력순서
        dMsg.setRcpPersonNo2(new String(rcvDByte,642,20,Constants.MY_CHARSET).trim());   		// 현금영수증발행사업자번호(카드)

        String DrecordType   = dMsg.getdRecordType();
        String chaCd	     	= dMsg.getChaCd();
        String workField     = dMsg.getWorkField();
        String trNo          = dMsg.getTrNo();
        String vano          = dMsg.getVano();
        String payMasMonth   = dMsg.getPayMasMonth();
        String payMasDt      = dMsg.getPayMasDt();
        String rcpStartDt    = dMsg.getRcpStartDt();
        String rcpStartTm    = dMsg.getRcpStartTm();
        String rcpEndDt      = dMsg.getRcpEndDt();
        String rcpEndTm      = dMsg.getRcpEndTm();
        String rcpPrtEndDt   = dMsg.getRcpPrtEndDt();
        String rcpPrtEndTm   = dMsg.getRcpPrtEndTm();
        String cusNm         = dMsg.getCusNm();
        String smsYn         = dMsg.getSmsYn();
        String mobileNo      = dMsg.getMobileNo();
        String emailYn       = dMsg.getEmailYn();
        String email         = dMsg.getEmail();
        String content1      = dMsg.getContent1();
        String content2      = dMsg.getContent2();
        String content3      = dMsg.getContent3();
        String content4      = dMsg.getContent4();
        String chaTrNo       = dMsg.getChaTrNo();
        String adjfiGrpCd    = dMsg.getAdjfiGrpCd();
        String payItemNm     = dMsg.getPayItemNm();
        String payItemAmt    = dMsg.getPayItemAmt();
        String payItemYN     = dMsg.getPayItemYN();
        String rcpItemYN     = dMsg.getRcpItemYN();
        String rcpBusinessNo = dMsg.getRcpBusinessNo();
        String rcpPersonNo   = dMsg.getRcpPersonNo();
        String prtPayItemNm  = dMsg.getPrtPayItemNm();
        String prtContent1   = dMsg.getPrtContent1();
        String prtSeq        = dMsg.getPrtSeq();
        String rcpPersonNo2  = dMsg.getRcpPersonNo2();

        String result_cd     = "D000";
        String result_msg    = "정상";
        boolean boolResult   = true;

        DateUtil du = new DateUtil();

        if (!ComCheck.chkRecordLen(rcvDByte, rcvDByte.length, msgSize)) {
            result_cd    = "D001";
            result_msg   = "길이오류["+rcvDByte.length+"]";
            boolResult   = false;
        } else if (!ComCheck.chkRecordCRLF(rcvDByte, rcvDByte.length, msgSize)) {
            result_cd    = "D002";
            result_msg   = "CRLF없음";
            boolResult   = false;
        } else if (!"D".equalsIgnoreCase(DrecordType)) {
            result_cd    = "D003";
            result_msg   = "구분오류["+DrecordType+"]";
            boolResult   = false;
//       } else if (!custId.equals(serviceId)) {
//           result_cd   = "D004";
//           result_msg  = "ID오류["+serviceId+"]";
//           boolResult  = false;
        } else if (!("N".equalsIgnoreCase(workField)||"A".equalsIgnoreCase(workField)
                ||"U".equalsIgnoreCase(workField)||"D".equalsIgnoreCase(workField))) {
            result_cd    = "D005";
            result_msg   = "내역오류["+workField+"]";
            boolResult   = false;
        } else if ("".equals(trNo)) {
            result_cd   = "D032";
            result_msg  = "청구번호오류";
            boolResult  = false;
        } else if (!"".equals(trNo)) {
            for (int iPos = 0; iPos < trNo.length(); iPos++) {
                String strOne = trNo.substring(iPos, iPos + 1);
                if (!ComCheck.isNumber(strOne)&&!ComCheck.isAlpha(strOne)) {
                    result_cd   = "D032";
                    result_msg  = "청구거래오류";
                    boolResult  = false;
                    break;
                }
            }
        } else if ("N".equalsIgnoreCase(workField) && "".equals(trNo)) {
            result_cd   = "D021";
            result_msg  = "가상계좌오류";
            boolResult  = false;
        }


        if (boolResult) {
            if ("N".equalsIgnoreCase(workField)||"U".equalsIgnoreCase(workField)) {
                if ("".equals(payMasMonth)) {
                    result_cd   = "D030";
                    result_msg  = "청구월오류["+payMasMonth+"]";
                    boolResult  = false;
                } else if (!(payMasMonth.length()==6&&ComCheck.isAllNumber(payMasMonth)&&
                        ComCheck.chkDateFormat(payMasMonth+"01")&&"20".equals(payMasMonth.substring(0,2))&&
                        Integer.parseInt(du.monthCal(du.getTime("YYYYMMDD"),-2).substring(0,6))<=Integer.parseInt(payMasMonth))) {
                    result_cd    = "D030";
                    result_msg   = "청구월오류["+payMasMonth+"]";
                    boolResult   = false;
                } else if ("".equals(payMasDt)) {
                    result_cd   = "D031";
                    result_msg  = "일자오류["+payMasDt+"]";
                    boolResult  = false;
                } else if (payMasDt.length()!=8) {
                    result_cd    = "D031";
                    result_msg   = "일자오류["+payMasDt+"]";
                    boolResult   = false;
                } else if ("".equals(rcpStartDt)) {
                    result_cd   = "D033";
                    result_msg  = "시작일오류["+rcpStartDt+"]";
                    boolResult  = false;
                } else if (!(rcpStartDt.length()==8&&ComCheck.isAllNumber(rcpStartDt)&&
                        ComCheck.chkDateFormat(rcpStartDt)&&"20".equals(rcpStartDt.substring(0,2))&&
                        Integer.parseInt(du.dateCal(du.getTime("YYYYMMDD"),-60))<=Integer.parseInt(rcpStartDt))) {
                    result_cd    = "D033";
                    result_msg   = "시작일오류["+rcpStartDt+"]";
                    boolResult   = false;
                } else if (!"".equals(rcpStartTm)&&!(rcpStartTm.length()==4&&
                        ComCheck.isAllNumber(rcpStartTm)&&Integer.parseInt(rcpStartTm)<=2359)) {
                    result_cd    = "D033";
                    result_msg   = "시작시각오류["+rcpStartTm+"]";
                    boolResult   = false;
                } else if (!"".equals(rcpPrtEndDt)&&!(rcpPrtEndDt.length()==8&&ComCheck.isAllNumber(rcpPrtEndDt)&&
                        ComCheck.chkDateFormat(rcpPrtEndDt.substring(0,8))&&"20".equals(rcpPrtEndDt.substring(0,2))&&
                        Integer.parseInt(du.getTime("YYYYMMDD"))<=Integer.parseInt(rcpPrtEndDt))) {
                    result_cd    = "D035";
                    result_msg   = "출력일오류["+rcpPrtEndDt+"]";
                    boolResult   = false;
                } else if (!"".equals(rcpPrtEndDt)&&!"".equals(rcpPrtEndTm)&&!(rcpPrtEndTm.length()==4&&
                        ComCheck.isAllNumber(rcpPrtEndTm)&&Integer.parseInt(rcpPrtEndTm)<=2359)) {
                    result_cd    = "D035";
                    result_msg   = "출력시각오류["+rcpPrtEndTm+"]";
                    boolResult   = false;
                } else if ("".equals(rcpEndDt)) {
                    result_cd   = "D034";
                    result_msg  = "마감일오류["+rcpEndDt+"]";
                    boolResult  = false;
                } else if (!(rcpEndDt.length()==8&&ComCheck.isAllNumber(rcpEndDt)&&
                        ComCheck.chkDateFormat(rcpEndDt)&&"20".equals(rcpEndDt.substring(0,2))&&
                        Integer.parseInt(du.getTime("YYYYMMDD"))<=Integer.parseInt(rcpEndDt))) {
                    result_cd    = "D034";
                    result_msg   = "마감일오류["+rcpEndDt+"]";
                    boolResult   = false;
                } else if (!"".equals(rcpEndTm)&&!(rcpEndTm.length()==4&&
                        ComCheck.isAllNumber(rcpEndTm)&&Integer.parseInt(rcpEndTm)<=2359)) {
                    result_cd    = "D033";
                    result_msg   = "마감시각오류["+rcpEndTm+"]";
                    boolResult   = false;
                } else if (Integer.parseInt(rcpStartDt)>Integer.parseInt(rcpEndDt)) {
                    result_cd    = "D050";
                    result_msg   = "날짜["+rcpStartDt+"]["+rcpEndDt+"]";
                    boolResult   = false;
                } else if (!"".equals(rcpPrtEndDt)&&Integer.parseInt(rcpStartDt)>Integer.parseInt(rcpPrtEndDt)) {
                    result_cd    = "D050";
                    result_msg   = "날짜["+rcpStartDt+"]["+rcpPrtEndDt+"]";
                    boolResult   = false;
                } else if (!"".equals(cusNm)) {
                    for (int iPos = 0; iPos < cusNm.length(); iPos++) {
                        String strOne = cusNm.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D070";
                            result_msg  = "고객명오류";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if (!("Y".equalsIgnoreCase(smsYn)||"N".equalsIgnoreCase(smsYn))) {
                    result_cd   = "D010";
                    result_msg  = "SMS여부오류["+smsYn+"]";
                    boolResult  = false;
                } else if ("Y".equalsIgnoreCase(smsYn)&&"".equals(mobileNo)) {
                    result_cd   = "D011";
                    result_msg  = "번호오류["+smsYn+"]["+mobileNo+"]";
                    boolResult  = false;
                } else if (!"".equals(mobileNo)) {
                    if (!(mobileNo.length()>=10&&ComCheck.isAllPhoneNo(mobileNo))) {
                        result_cd   = "D011";
                        result_msg  = "번호오류["+mobileNo+"]";
                        boolResult  = false;
                    }
                } else if (!("Y".equalsIgnoreCase(emailYn)||"N".equalsIgnoreCase(emailYn))) {
                    result_cd   = "D012";
                    result_msg  = "email여부오류["+emailYn+"]";
                    boolResult  = false;
                } else if ("Y".equalsIgnoreCase(emailYn)&&"".equals(email)) {
                    result_cd   = "D013";
                    result_msg  = "email오류["+emailYn+"]["+email+"]";
                    boolResult  = false;
                } else if (!"".equals(email)) {
                    if (!(email.length()>=10&&ComCheck.isAllEmail(email))) {
                        result_cd   = "D013";
                        result_msg  = "email오류["+email+"]";
                        boolResult  = false;
                    }
                } else if (!"".equals(content1)) {
                    for (int iPos = 0; iPos < content1.length(); iPos++) {
                        String strOne = content1.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D058";
                            result_msg  = "참조오류["+content1+"]";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if (!"".equals(content2)) {
                    for (int iPos = 0; iPos < content2.length(); iPos++) {
                        String strOne = content2.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D059";
                            result_msg  = "참조오류["+content2+"]";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if (!"".equals(content3)) {
                    for (int iPos = 0; iPos < content3.length(); iPos++) {
                        String strOne = content3.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D060";
                            result_msg  = "참조오류["+content3+"]";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if (!"".equals(content4)) {
                    for (int iPos = 0; iPos < content4.length(); iPos++) {
                        String strOne = content4.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D061";
                            result_msg  = "참조오류["+content4+"]";
                            boolResult  = false;
                            break;
                        }
                    }
                }
            }
        }

        if (boolResult) {
            if (!"".equals(chaTrNo)) {
                for (int iPos = 0; iPos < chaTrNo.length(); iPos++) {
                    String strOne = chaTrNo.substring(iPos, iPos + 1);
                    if (!ComCheck.isNumber(strOne)&&!ComCheck.isAlpha(strOne)) {
                        result_cd   = "D062";
                        result_msg  = "항목번호오류";
                        boolResult  = false;
                        break;
                    }
                }
            }else if ( ("N".equalsIgnoreCase(workField)||"A".equalsIgnoreCase(workField)) && "".equals(chaTrNo)){
                result_cd   = "D062";
                result_msg  = "항목번호오류";
                boolResult  = false;
            }
        }

        if (boolResult) {
            if ("N".equalsIgnoreCase(workField)||"A".equalsIgnoreCase(workField)||
                    ("U".equalsIgnoreCase(workField)&&!"".equals(chaTrNo))) {
                if (!"".equals(adjfiGrpCd)&&!(adjfiGrpCd.length()==5&&ComCheck.isAllNumber(adjfiGrpCd))) {
                    result_cd   = "D063";
                    result_msg  = "배분KEY오류["+adjfiGrpCd+"]";
                    boolResult  = false;
                } else if ("".equals(payItemNm)) {
                    result_cd   = "D040";
                    result_msg  = "항목명오류";
                    boolResult  = false;
                } else if (!"".equals(payItemNm)) {
                    for (int iPos = 0; iPos < payItemNm.length(); iPos++) {
                        String strOne = payItemNm.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D040";
                            result_msg  = "항목명오류";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if ("".equals(payItemAmt)) {
                    result_cd   = "D041";
                    result_msg  = "금액오류["+payItemAmt+"]";
                    boolResult  = false;
                } else if (!ComCheck.isAllNumber(payItemAmt)) {
                    result_cd   = "D041";
                    result_msg  = "금액오류["+payItemAmt+"]";
                    boolResult  = false;
                } else if (!("Y".equalsIgnoreCase(payItemYN)||"N".equalsIgnoreCase(payItemYN))) {
                    result_cd   = "D044";
                    result_msg  = "항목여부오류["+payItemYN+"]";
                    boolResult  = false;
                } else if (!("Y".equalsIgnoreCase(rcpItemYN)||"X".equalsIgnoreCase(rcpItemYN)||"N".equalsIgnoreCase(rcpItemYN))) {
                    result_cd   = "D045";
                    result_msg  = "영수증여부["+rcpItemYN+"]";
                    boolResult  = false;
                } else if (("Y".equalsIgnoreCase(rcpItemYN)||"X".equalsIgnoreCase(rcpItemYN)) && "".equals(rcpBusinessNo)){
                    result_cd   = "D064";
                    result_msg  = "영수증사업자["+rcpBusinessNo+"]";
                    boolResult  = false;
                } else if (!"".equals(rcpBusinessNo)&&!(rcpBusinessNo.length()==10&&ComCheck.isAllNumber(rcpBusinessNo))) {
                    result_cd   = "D064";
                    result_msg  = "영수증사업자["+rcpBusinessNo+"]";
                    boolResult  = false;
                } else if (("Y".equalsIgnoreCase(rcpItemYN)||"X".equalsIgnoreCase(rcpItemYN))&&
                        ("".equals(rcpPersonNo)&&"".equals(rcpPersonNo2))){
                    result_cd   = "D065";
                    result_msg  = "영수증고객["+rcpPersonNo+"]";
                    boolResult  = false;
                } else if ("X".equalsIgnoreCase(rcpItemYN)&&!"".equals(rcpPersonNo)&&
                        !(rcpPersonNo.length()==10&&ComCheck.isAllNumber(rcpPersonNo))) {
                    result_cd   = "D065";
                    result_msg  = "영수증고객["+rcpPersonNo+"]["+rcpItemYN+"]";
                    boolResult  = false;
                } else if ("X".equalsIgnoreCase(rcpItemYN)&&!"".equals(rcpPersonNo2)&&
                        !(rcpPersonNo2.length()==10&&ComCheck.isAllNumber(rcpPersonNo2))) {
                    result_cd   = "D065";
                    result_msg  = "영수증고객["+rcpPersonNo2+"]["+rcpItemYN+"]";
                    boolResult  = false;
                } else if ("Y".equalsIgnoreCase(rcpItemYN)&&!"".equals(rcpPersonNo)&&
                        !(rcpPersonNo.length()>10&&ComCheck.isAllNumber(rcpPersonNo))) {
                    result_cd   = "D065";
                    result_msg  = "영수증고객["+rcpPersonNo+"]["+rcpItemYN+"]";
                    boolResult  = false;
                } else if ("Y".equalsIgnoreCase(rcpItemYN)&&!"".equals(rcpPersonNo2)&&
                        !(rcpPersonNo2.length()>10&&ComCheck.isAllNumber(rcpPersonNo2))) {
                    result_cd   = "D065";
                    result_msg  = "영수증고객["+rcpPersonNo2+"]["+rcpItemYN+"]";
                    boolResult  = false;
                } else if ("Y".equalsIgnoreCase(rcpItemYN)&&!"".equals(rcpPersonNo)&&
                        rcpPersonNo.length()==13&&ComCheck.isAllNumber(rcpPersonNo)) {
                    result_cd   = "D065";
                    result_msg  = "영수증고객["+rcpPersonNo+"]["+rcpItemYN+"]";
                    boolResult  = false;
                } else if ("Y".equalsIgnoreCase(rcpItemYN)&&!"".equals(rcpPersonNo2)&&
                        rcpPersonNo2.length()==13&&ComCheck.isAllNumber(rcpPersonNo2)) {
                    result_cd   = "D065";
                    result_msg  = "주민번호금지["+rcpPersonNo2+"]["+rcpItemYN+"]";
                    boolResult  = false;
                } else if (!"".equals(prtPayItemNm)) {
                    for (int iPos = 0; iPos < prtPayItemNm.length(); iPos++) {
                        String strOne = prtPayItemNm.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D042";
                            result_msg  = "항목명오류";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if (!"".equals(prtContent1)) {
                    for (int iPos = 0; iPos < prtContent1.length(); iPos++) {
                        String strOne = prtContent1.substring(iPos, iPos + 1);
                        if (ComCheck.isSpecialWord(strOne)) {
                            result_cd   = "D066";
                            result_msg  = "비고오류";
                            boolResult  = false;
                            break;
                        }
                    }
                } else if (!"".equals(prtSeq)&&!ComCheck.isAllNumber(prtSeq)) {
                    result_cd   = "D067";
                    result_msg  = "순서오류["+prtSeq+"]";
                    boolResult  = false;
                }
            }
        }


        dMsg.setResult_cd(result_cd);
        dMsg.setResult_msg(ComCheck.makeSpaceData(result_msg,30));

        return boolResult;
    }
}
