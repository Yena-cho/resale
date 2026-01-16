package com.damoa.msg;

import com.damoa.util.StringUtil;

/**
 * 시작전문 VO
 * 
 * >>> 송신전용
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 6.
 */
public class AegisStartMsg {
	public String procDate = "";
	public String procSeq = "";
	public String cust_id = "";
	public String dataField = "";
	public String workField = "";
	
	public String startYN = "";      // 처리결과  
	public String startCd = "";      // 결과코드  
	public String startMsg = "";     // 결과메세지 
	
	/**
	 * 전문시작문자
	 */
	public final String START_RECODE = "S";
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
	
	public void setMsg(byte[] rcvByte) {
		startYN  = StringUtil.bytesToString(rcvByte, 10,  1);  // 처리결과
		startCd  = StringUtil.bytesToString(rcvByte, 11,  4);  // 결과코드
		startMsg = StringUtil.bytesToString(rcvByte, 15, 30);  // 결과메세지
	}
	
	public String makeMsg() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(START_RECODE);
		sb.append(StringUtil.lPad(cust_id,   " ",   8));  
		sb.append( SPACE );                               
		sb.append(StringUtil.lPad(startYN,   " ",   1));  
		sb.append(StringUtil.lPad(startCd,   " ",   4));  
		sb.append(StringUtil.lPad(startMsg,  " ",  30));  
		sb.append(StringUtil.lPad(dataField, " ",   3));  
		sb.append(StringUtil.lPad(workField, " ",   1));  
		sb.append(StringUtil.lPad(procDate,  "0",   8));  
		sb.append(StringUtil.lPad(procSeq,   "0",   6));  
		sb.append(StringUtil.lPad(SPACE,     " ", 635));  
	
		// 전송종료문자열
		sb.append(STRING_CR).append(STRING_LF);
		return sb.toString();
    }
	
	@Override
	public String toString() {
		return "AegisStartMsg [procDate=" + procDate + ", procSeq=" + procSeq + ", cust_id=" + cust_id + ", dataField="
				+ dataField + ", workField=" + workField + ", startYN=" + startYN + ", startCd=" + startCd
				+ ", startMsg=" + startMsg + "]";
	}
}
