package com.damoa.msg;

import com.damoa.comm.ClientInfo;
import com.damoa.util.StringUtil;

public class AegisDMsg {

	/**
	 * Record구분
	 */
	public String recordType 	= "";
	/**
	 * 가맹점코드(기관코드)
	 */
	public String serviceId 	= "";
	/**
	 * 처리내역   (신규 : 'N', 추가 : 'A', 수정 : 'U', 삭제 : 'D')
	 */
	public String workField 	= "";
	/**
	 * 처리결과
	 */
	public String successYN 	= "";
	/**
	 * 결과코드
	 */
	public String resultCd 		= "";
	/**
	 * 결과메시지
	 */
	public String resultMsg 	= "";
	/**
	 * 청구거래번호 (유일해야함)
	 */
	public String trNo 			= "";
	/**
	 * 가상계좌번호
	 */
	public String vaNo 			= "";
	/**
	 * 청구월 (YYYYMM)
	 */
	public String payMasMonth 	= "";
	/**
	 * 청구일자 (YYYYMMDD)
	 */
	public String payMasDt 		= "";
	/**
	 * 납부시작일 (YYYYMMDD)
	 */
	public String rcpStartDt 	= "";
	/**
	 * 납부시작시간(사용안함)
	 */
	public String rcpStartTm 	= "";
	/**
	 * 납부마감일(YYYYMMDD)
	 */
	public String rcpEndDt 		= "";
	/**
	 * 납부마감시간(사용안함)
	 */
	public String rcpEndTm 		= "";
	/**
	 * 출력납부마감일(YYYYMMDD)
	 */
	public String rcpPrtEndDt 	= "";
	/**
	 * 출력납부마감시간(사용안함)
	 */
	public String rcpPrtEndTm 	= "";
	/**
	 * 고객명
	 */
	public String cusNm 		= "";
	/**
	 * SMS전송여부(사용안함) 'N'값 셋팅
	 */
	public String smsYn 		= "";
	/**
	 * 고객휴대폰번호
	 */
	public String mobileNo 		= "";
	/**
	 * 이메일전송여부(사용안함) 'N'값 셋팅
	 */
	public String emailYn 		= "";
	/**
	 * 고객이메일주소(사용안함) 'N'값 셋팅
	 */
	public String email 		= "";
	/**
	 * 참조1
	 */
	public String content1 		= "";
	/**
	 * 참조2
	 */
	public String content2 		= "";
	/**
	 * 참조3
	 */
	public String content3 		= "";
	/**
	 * 참조4
	 */
	public String content4 		= "";
	/**
	 * 전문번호 (연동테이블PK)
	 */
	public String regSeq        = "";

	/**
	 * 항목거래번호
	 */
	public String chaTrNo       = "";
	/**
	 * 분할입금KEY
	 */
	public String adjfiGrpCd    = "";
	/**
	 * 기관그룹코드
	 */
	public String agsGrpCd    	= "";
	/**
	 * 은행코드
	 */
	public String fiCd    		= "";
	/**
	 * 항목명
	 */
	public String payItemNm     = "";
	/**
	 * 항목금액
	 */
	public String payItemAmt    = "";
	/**
	 * 항목필수여부(Y : 필수, N : 필수아님)
	 */
	public String payItemYN     = "";
	/**
	 * 현금영수증발행여부(Y : 소득공제용, X : 지출증빙용, N : 비대상)
	 */
	public String rcpItemYN     = "";
	/**
	 * 현금영수증발행사업자정보
	 */
	public String rcpBusinessNo = "";
	/**
	 * 현금영수증발행고객정보 (핸드폰 '-' 제외)
	 */
	public String rcpPersonNo   = "";
	/**
	 * 출력항목명
	 */
	public String prtPayItemNm  = "";
	/**
	 * 출력비고내용
	 */
	public String prtContent1   = "";
	/**
	 * 출력순서
	 */
	public String prtSeq        = "";
	
	/**
	 * 공백문자
	 */
	public final String SPACE      = " ";
	/**
	 * 전문종료 구분자 CR
	 */
	public final String STRING_CR   = "\r";
	/**
	 * 전문종료 구분자 LF
	 */
	public final String STRING_LF   = "\n";
	
	public AegisDMsg(){}

	public void setMsg(byte[] rcvByte) {
		
		this.clear();

		if("DAMOA".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
			recordType 	  = StringUtil.bytesToString(rcvByte,  0,   1).trim();
			serviceId 	  = StringUtil.bytesToString(rcvByte,  1,   8).trim();
			workField 	  = StringUtil.bytesToString(rcvByte,  9,   1).trim();
			successYN     = StringUtil.bytesToString(rcvByte,  10,  1);   //   처리내역(필수)
			resultCd      = StringUtil.bytesToString(rcvByte,  11,  4);   //   결과코드
			resultMsg     = StringUtil.bytesToString(rcvByte,  15, 30);   //   결과메시지
			trNo          = StringUtil.bytesToString(rcvByte,  45, 30).toUpperCase();    // 청구거래번호 (전문번호)
			vaNo          = StringUtil.bytesToString(rcvByte,  75, 20);    //  가상계좌번호
			payMasMonth   = StringUtil.bytesToString(rcvByte,  95,  6);    //  청구월
			payMasDt      = StringUtil.bytesToString(rcvByte, 101,  8);    //  청구일자
			rcpStartDt    = StringUtil.bytesToString(rcvByte, 109,  8);    //  수납시작일
			rcpStartTm    = StringUtil.bytesToString(rcvByte, 117,  4);    //  수납시작시각
			rcpEndDt      = StringUtil.bytesToString(rcvByte, 121,  8);    //  수납종료일
			rcpEndTm      = StringUtil.bytesToString(rcvByte, 129,  4);    //  수납종료시각
			rcpPrtEndDt   = StringUtil.bytesToString(rcvByte, 133,  8);    // 출력납부마감일
			rcpPrtEndTm   = StringUtil.bytesToString(rcvByte, 141,  4);    // 출력납부마감시각
			cusNm         = StringUtil.bytesToString(rcvByte, 145, 30);    //  고객명
			smsYn         = StringUtil.bytesToString(rcvByte, 175,  1);    //  sms전송여부
			mobileNo      = StringUtil.bytesToString(rcvByte, 176, 12);    //  휴대번호
			emailYn       = StringUtil.bytesToString(rcvByte, 188,  1);    // email전송여부
			email         = StringUtil.bytesToString(rcvByte, 189, 50);    //  email
			content1      = StringUtil.bytesToString(rcvByte, 239, 30);    //  참조1
			content2      = StringUtil.bytesToString(rcvByte, 269, 30);    //  참조2
			content3      = StringUtil.bytesToString(rcvByte, 299, 30);    //  참조3
			content4      = StringUtil.bytesToString(rcvByte, 329, 30);    //  참조4
			regSeq        = StringUtil.bytesToString(rcvByte, 359,  9);    //  테이블등록순번
			chaTrNo       = StringUtil.bytesToString(rcvByte, 420, 30).toUpperCase();    // 항목거래번호
			adjfiGrpCd    = StringUtil.bytesToString(rcvByte, 450,  5);    //  신한은행발급배분키
			payItemNm     = StringUtil.bytesToString(rcvByte, 455, 50);    // 항목명
			payItemAmt    = StringUtil.bytesToString(rcvByte, 505, 10);    // 항목금액
			payItemYN     = StringUtil.bytesToString(rcvByte, 515,  1);    //  항목필수여부
			rcpItemYN     = StringUtil.bytesToString(rcvByte, 516,  1);    //  현금영수증발행여부
			rcpBusinessNo = StringUtil.bytesToString(rcvByte, 517, 10);    // 현금영수증발행사업자번호
			prtPayItemNm  = StringUtil.bytesToString(rcvByte, 540, 50);    // 출력항목명
			prtContent1   = StringUtil.bytesToString(rcvByte, 590, 50);    // 출력비고내용
			prtSeq        = StringUtil.bytesToString(rcvByte, 640,  2);    //  출력순서
			rcpPersonNo   = StringUtil.bytesToString(rcvByte, 642, 20);    // 현금영수증발행고객정보
		}else if("THEBILL".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
			recordType 	  = StringUtil.bytesToString(rcvByte,  0,   1).trim();
			serviceId 	  = StringUtil.bytesToString(rcvByte,  1,   8).trim();
			workField 	  = StringUtil.bytesToString(rcvByte,  9,   1).trim();
			successYN 	  = StringUtil.bytesToString(rcvByte,  10,  1).trim();
			resultCd 	  = StringUtil.bytesToString(rcvByte,  11,  4).trim();
			resultMsg 	  = StringUtil.bytesToString(rcvByte,  15, 30).trim();
			regSeq 		  = StringUtil.bytesToString(rcvByte,  45,  9).trim().toUpperCase();
			trNo 		  = StringUtil.bytesToString(rcvByte,  54, 30).trim();
			vaNo 		  = StringUtil.bytesToString(rcvByte,  84, 20).trim();    // 청구월
			payMasMonth	  = StringUtil.bytesToString(rcvByte, 104,  6).trim();    // 청구월
			payMasDt 	  = StringUtil.bytesToString(rcvByte, 110,  8).trim();    // 청구일자
			rcpStartDt 	  = StringUtil.bytesToString(rcvByte, 118,  8).trim();    // 수납시작일
			rcpStartTm 	  = StringUtil.bytesToString(rcvByte, 126,  4).trim();    // 수납시작시각
			rcpEndDt 	  = StringUtil.bytesToString(rcvByte, 130,  8).trim();    // 수납종료일
			rcpEndTm 	  = StringUtil.bytesToString(rcvByte, 138,  4).trim();    // 수납종료시각
			rcpPrtEndDt   = StringUtil.bytesToString(rcvByte, 142,  8).trim();    // 출력납부마감일
			rcpPrtEndTm   = StringUtil.bytesToString(rcvByte, 150,  4).trim();    // 출력납부마감시각
			cusNm 		  = StringUtil.bytesToString(rcvByte, 154, 30).trim();    // 고객명
			smsYn 		  = StringUtil.bytesToString(rcvByte, 184,  1).trim();    // sms전송여부
			mobileNo 	  = StringUtil.bytesToString(rcvByte, 185, 12).trim();    // 휴대번호
			emailYn 	  = StringUtil.bytesToString(rcvByte, 197,  1).trim();    // email전송여부
			email 		  = StringUtil.bytesToString(rcvByte, 198, 50).trim();    // email
			content1 	  = StringUtil.bytesToString(rcvByte, 248, 30).trim();    // 참조1
			content2 	  = StringUtil.bytesToString(rcvByte, 278, 30).trim();    // 참조2
			content3 	  = StringUtil.bytesToString(rcvByte, 308, 30).trim();    // 참조3
			content4 	  = StringUtil.bytesToString(rcvByte, 338, 30).trim();    // 참조4
			chaTrNo       = StringUtil.bytesToString(rcvByte, 420, 30).trim().toUpperCase();
			adjfiGrpCd    = StringUtil.bytesToString(rcvByte, 450,  5).trim();    // 신한은행발급배분키
			payItemNm     = StringUtil.bytesToString(rcvByte, 455, 50).trim();    // 항목명
			payItemAmt    = StringUtil.bytesToString(rcvByte, 505, 10).trim();    // 항목금액
			payItemYN     = StringUtil.bytesToString(rcvByte, 515,  1).trim();    // 항목필수여부
			rcpItemYN     = StringUtil.bytesToString(rcvByte, 516,  1).trim();    // 현금영수증발행여부
			rcpBusinessNo = StringUtil.bytesToString(rcvByte, 517, 10).trim();    // 현금영수증발행사업자번호
			rcpPersonNo   = StringUtil.bytesToString(rcvByte, 527, 20).trim();    // 현금영수증발행고객정보
			prtPayItemNm  = StringUtil.bytesToString(rcvByte, 547, 50).trim();    // 출력항목명
			prtContent1   = StringUtil.bytesToString(rcvByte, 597, 50).trim();    // 출력비고내용
			prtSeq        = StringUtil.bytesToString(rcvByte, 647,  2).trim();    // 출력순서
		}
	}

	public String makeMsg(){

		StringBuffer sb = new StringBuffer();

		if("DAMOA".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
			sb.append(StringUtil.lPad(recordType,     " ",    1));    // Record구분(필수)
			sb.append(StringUtil.lPad(serviceId,      " ",    8));    // 가맹점코드(필수)
			sb.append(StringUtil.lPad(workField,      " ",    1));    // 처리내역(필수)
			sb.append(StringUtil.lPad(successYN,      " ",    1));    // 처리결과
			sb.append(StringUtil.lPad(resultCd,       " ",    4));    // 결과코드
			sb.append(StringUtil.lPad(resultMsg,      " ",   30));    // 결과메시지
			sb.append(StringUtil.lPad(trNo,           " ",   30));    // 청구거래번호(필수)
			sb.append(StringUtil.lPad(vaNo,           " ",   20));    // 가상계좌번호(필수)
			sb.append(StringUtil.lPad(payMasMonth,    " ",    6));    // 청구월(필수)
			sb.append(StringUtil.lPad(payMasDt,       " ",    8));    // 청구일자(필수)
			sb.append(StringUtil.lPad(rcpStartDt,     " ",    8));    // 납부시작일(필수)
			sb.append(StringUtil.lPad(rcpStartTm,     " ",    4));    // 납부시작시간
			sb.append(StringUtil.lPad(rcpEndDt,       " ",    8));    // 납부마감일(필수)
			sb.append(StringUtil.lPad(rcpEndTm,       " ",    4));    // 납부마감시간
			sb.append(StringUtil.lPad(rcpPrtEndDt,    " ",    8));    // 출력납부마감일
			sb.append(StringUtil.lPad(rcpPrtEndTm,    " ",    4));    // 출력납부마감시간
			sb.append(StringUtil.lPad(cusNm,          " ",   30));    // 고객명
			sb.append(StringUtil.lPad(smsYn,          " ",    1));    // SMS전송여부(필수)
			sb.append(StringUtil.lPad(mobileNo,       " ",   12));    // 고객휴대폰번호
			sb.append(StringUtil.lPad(emailYn,        " ",    1));    // 이메일전송여부(필수)
			sb.append(StringUtil.lPad(email,          " ",   50));    // 고객이메일주소
			sb.append(StringUtil.lPad(content1,       " ",   30));    // 참조1
			sb.append(StringUtil.lPad(content2,       " ",   30));    // 참조2
			sb.append(StringUtil.lPad(content3,       " ",   30));    // 참조3
			sb.append(StringUtil.lPad(content4,       " ",   30));    // 참조4
			sb.append(StringUtil.lPad(regSeq,         " ",    9));    // 전문번호
			sb.append(StringUtil.lPad(SPACE,          " ",   52));    // space
			sb.append(StringUtil.lPad(chaTrNo,        " ",   30));    // 항목거래번호(필수)
			sb.append(StringUtil.lPad(adjfiGrpCd,     " ",    5));    // 신한은행발급배분KEY
			sb.append(StringUtil.lPad(payItemNm,      " ",   50));    // 항목명(필수)
			sb.append(StringUtil.lPad(payItemAmt,     " ",   10));    // 항목금액(필수)
			sb.append(StringUtil.lPad(payItemYN,      " ",    1));    // 항목필수여부(필수)
			sb.append(StringUtil.lPad(rcpItemYN,      " ",    1));    // 현금영수증발행여부(필수)
			sb.append(StringUtil.lPad(rcpBusinessNo,  " ",   10));    // 현금영수증발행사업자정보
			sb.append(StringUtil.lPad(SPACE,          " ",   13));    // 현금영수증발행고객정보
			sb.append(StringUtil.lPad(prtPayItemNm,   " ",   50));    // 출력항목명
			sb.append(StringUtil.lPad(prtContent1,    " ",   50));    // 출력비고내용
			sb.append(StringUtil.lPad(prtSeq,         " ",    2));    // 출력순서
			sb.append(StringUtil.lPad(rcpPersonNo,    " ",   20));    // 현금영수증발행고객정보
			sb.append(StringUtil.lPad(SPACE,          " ",   36));

		}else if("THEBILL".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
			sb.append(StringUtil.lPad(recordType,     " ",   1));
			sb.append(StringUtil.lPad(serviceId,      " ",   8));
			sb.append(StringUtil.lPad(workField,      " ",   1));
			sb.append(StringUtil.lPad(successYN,      " ",   1));
			sb.append(StringUtil.lPad(resultCd,       " ",   4));
			sb.append(StringUtil.lPad(resultMsg,      " ",  30));
			sb.append(StringUtil.lPad(regSeq,         " ",   9));
			sb.append(StringUtil.lPad(trNo,           " ",  30));
			sb.append(StringUtil.lPad(vaNo,           " ",  20));
			sb.append(StringUtil.lPad(payMasMonth,    " ",   6));
			sb.append(StringUtil.lPad(payMasDt,       " ",   8));
			sb.append(StringUtil.lPad(rcpStartDt,     " ",   8));
			sb.append(StringUtil.lPad(rcpStartTm,     " ",   4));
			sb.append(StringUtil.lPad(rcpEndDt,       " ",   8));
			sb.append(StringUtil.lPad(rcpEndTm,       " ",   4));
			sb.append(StringUtil.lPad(rcpPrtEndDt,    " ",   8));
			sb.append(StringUtil.lPad(rcpPrtEndTm,    " ",   4));
			sb.append(StringUtil.lPad(cusNm,          " ",  30));
			sb.append(StringUtil.lPad(smsYn,          " ",   1));
			sb.append(StringUtil.lPad(mobileNo,       " ",  12));
			sb.append(StringUtil.lPad(emailYn,        " ",   1));
			sb.append(StringUtil.lPad(email,          " ",  50));
			sb.append(StringUtil.lPad(content1,       " ",  30));
			sb.append(StringUtil.lPad(content2,       " ",  30));
			sb.append(StringUtil.lPad(content3,       " ",  30));
			sb.append(StringUtil.lPad(content4,       " ",  30));
			sb.append(StringUtil.lPad("",             " ",  52));
			sb.append(StringUtil.lPad(chaTrNo,        " ",  30));
			sb.append(StringUtil.lPad(adjfiGrpCd,     " ",   5));
			sb.append(StringUtil.lPad(payItemNm,      " ",  50));
			sb.append(StringUtil.lPad(payItemAmt,     " ",  10));
			sb.append(StringUtil.lPad(payItemYN,      " ",   1));
			sb.append(StringUtil.lPad(rcpItemYN,      " ",   1));
			sb.append(StringUtil.lPad(rcpBusinessNo,  " ",  10));
			sb.append(StringUtil.lPad(rcpPersonNo,    " ",  20));
			sb.append(StringUtil.lPad(prtPayItemNm,   " ",  50));
			sb.append(StringUtil.lPad(prtContent1,    " ",  50));
			sb.append(StringUtil.lPad(prtSeq,         " ",   2));
			sb.append(StringUtil.lPad("",             " ",  49));
		}
		
		// 전송종료문자열
		sb.append(STRING_CR).append(STRING_LF);

		return sb.toString();

	}

	public void clear(){
		recordType 	= "";
		serviceId 	= "";
		workField 	= "";
		successYN 	= "";
		resultCd 	= "";
		resultMsg 	= "";
		trNo 		= "";
		vaNo 		= "";
		payMasMonth = "";
		payMasDt 	= "";
		rcpStartDt 	= "";
		rcpStartTm 	= "";
		rcpEndDt 	= "";
		rcpEndTm 	= "";
		rcpPrtEndDt = "";
		rcpPrtEndTm = "";
		cusNm 		= "";
		smsYn 		= "";
		mobileNo 	= "";
		emailYn 	= "";
		email 		= "";
		content1 	= "";
		content2 	= "";
		content3 	= "";
		content4 	= "";
		regSeq		= "";

		chaTrNo       = "";
		adjfiGrpCd    = "";
		payItemNm     = "";
		payItemAmt    = "";
		payItemYN     = "";
		rcpItemYN     = "";
		rcpBusinessNo = "";
		rcpPersonNo   = "";
		prtPayItemNm  = "";
		prtContent1   = "";
		prtSeq        = "";
	}

	@Override
	public String toString() {
		return "AegisDMsg [recordType=" + recordType + ", serviceId=" + serviceId + ", workField=" + workField
				+ ", successYN=" + successYN + ", resultCd=" + resultCd + ", resultMsg=" + resultMsg + ", trNo=" + trNo
				+ ", vaNo=" + vaNo + ", payMasMonth=" + payMasMonth + ", payMasDt=" + payMasDt + ", rcpStartDt="
				+ rcpStartDt + ", rcpStartTm=" + rcpStartTm + ", rcpEndDt=" + rcpEndDt + ", rcpEndTm=" + rcpEndTm
				+ ", rcpPrtEndDt=" + rcpPrtEndDt + ", rcpPrtEndTm=" + rcpPrtEndTm + ", cusNm=" + cusNm + ", smsYn="
				+ smsYn + ", mobileNo=" + mobileNo + ", emailYn=" + emailYn + ", email=" + email + ", content1="
				+ content1 + ", content2=" + content2 + ", content3=" + content3 + ", content4=" + content4
				+ ", regSeq=" + regSeq + ", chaTrNo=" + chaTrNo + ", adjfiGrpCd=" + adjfiGrpCd + ", agsGrpCd="
				+ agsGrpCd + ", fiCd=" + fiCd + ", payItemNm=" + payItemNm + ", payItemAmt=" + payItemAmt
				+ ", payItemYN=" + payItemYN + ", rcpItemYN=" + rcpItemYN + ", rcpBusinessNo=" + rcpBusinessNo
				+ ", rcpPersonNo=" + rcpPersonNo + ", prtPayItemNm=" + prtPayItemNm + ", prtContent1=" + prtContent1
				+ ", prtSeq=" + prtSeq + "]";
	}
	
}
