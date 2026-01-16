package com.damoa.msg;

import com.damoa.util.StringUtil;

/**
 * 전문Header VO
 * 청구 / 납부 공통사용
 * 
 * >>> 송/수신 용
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 6.
 */
public class AegisHMsg {
	public String recordType 	= "";
	public String serviceId 	= "";
	public String successYN 	= "";
	public String resultCd 		= "";
	public String resultMsg 	= "";
	public String rowCnt 		= "";
	
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

	public AegisHMsg(){}

	public void setMsg(byte[] rcvByte) {
		this.clear();
		recordType = StringUtil.bytesToString(rcvByte,  0,  1);  // Record구분(필수)
		serviceId  = StringUtil.bytesToString(rcvByte,  1,  8);  // 가맹점코드(필수)
		// space
		successYN  = StringUtil.bytesToString(rcvByte, 10,  1);  // 처리결과
		resultCd   = StringUtil.bytesToString(rcvByte, 11,  4);  // 결과코드
		resultMsg  = StringUtil.bytesToString(rcvByte, 15, 30);  // 결과메세지
		rowCnt     = StringUtil.bytesToString(rcvByte, 45,  6);  // 처리건수(필수) { 청구신청건수, 납부결과건수 }
		// space
	}

	public String makeMsg() {
		StringBuffer sb = new StringBuffer();

		sb.append(StringUtil.lPad(recordType, " ",   1));
		sb.append(StringUtil.lPad(serviceId,  " ",   8));
		sb.append( SPACE );
		sb.append(StringUtil.lPad(successYN,  " ",   1));
		sb.append(StringUtil.lPad(resultCd,   " ",   4));
		sb.append(StringUtil.lPad(resultMsg,  " ",  30));
		sb.append(StringUtil.lPad(rowCnt,     " ",   6));
		sb.append(StringUtil.lPad(SPACE,      " ", 647));
		// 전송종료문자열
		sb.append(STRING_CR).append(STRING_LF);
		return sb.toString();
    }
	
	public void clear(){
		recordType 	= "";
		serviceId 	= "";
		successYN 	= "";
		resultCd 	= "";
		resultMsg 	= "";
		rowCnt 		= "";

	}

	@Override
	public String toString() {
		return "AegisHMsg [recordType=" + recordType + ", serviceId=" + serviceId + ", successYN=" + successYN
				+ ", resultCd=" + resultCd + ", resultMsg=" + resultMsg + ", rowCnt=" + rowCnt + "]";
	}
	
}
