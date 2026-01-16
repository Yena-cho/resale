package com.damoa.msg;

import com.damoa.util.StringUtil;

/**
 * 종료전문 VO
 * 
 * >>> 송신전용
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 6.
 */
public class AegisTMsg {
	public String recordType 	= "";
	public String serviceId 	= "";
	public String successYN 	= "";
	public String resultCd 		= "";
	public String resultMsg 	= "";
	public String rowCnt	 	= "";
	public String successCnt 	= "";
	
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

	public AegisTMsg(){}

	public void setMsg(byte[] rcvByte) {
		this.clear();
		recordType = StringUtil.bytesToString(rcvByte,  0, 1);  // Record구분(필수)
		serviceId  = StringUtil.bytesToString(rcvByte,  1, 8);  // 가맹점코드(필수)
		successYN  = StringUtil.bytesToString(rcvByte, 10, 1);  // 처리결과
		resultCd   = StringUtil.bytesToString(rcvByte, 11, 4);  // 결과코드
		resultMsg  = StringUtil.bytesToString(rcvByte, 15,30);  // 결과메세지
		rowCnt     = StringUtil.bytesToString(rcvByte, 45, 6);  // 청구신청건수(필수)
		successCnt = StringUtil.bytesToString(rcvByte, 51, 6);  // 청구성공건수
	}

	public String makeMsg() {
		StringBuffer sb = new StringBuffer();

		sb.append(StringUtil.lPad(recordType, " ",   1) );    // TAILER RECORD 
		sb.append(StringUtil.lPad(serviceId,  " ",   8) );    // 가맹점코드
		sb.append( SPACE );                                   // SPACE
		sb.append(StringUtil.lPad(successYN,  " ",   1) );    // Y : 정상 N : 오류
		sb.append(StringUtil.lPad(resultCd,   " ",   4) );    // T***
		sb.append(StringUtil.lPad(resultMsg,  " ",  30) );    // 결과코드의 메시지
		sb.append(StringUtil.lPad(rowCnt,     " ",   6) );    // 총신청건수(DATA부분건수)
		sb.append(StringUtil.lPad(successCnt, " ",   6) );    // 총성공건수
		sb.append(StringUtil.lPad(SPACE,      " ", 641) );    // SPACE
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
		successCnt 	= "";
	}

	@Override
	public String toString() {
		return "AegisTMsg [recordType=" + recordType + ", serviceId=" + serviceId + ", successYN=" + successYN
				+ ", resultCd=" + resultCd + ", resultMsg=" + resultMsg + ", rowCnt=" + rowCnt + ", successCnt="
				+ successCnt + "]";
	}
}
