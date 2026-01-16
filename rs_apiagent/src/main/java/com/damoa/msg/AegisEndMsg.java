package com.damoa.msg;

import com.damoa.util.StringUtil;

/**
 * 종료전문
 * 
 * >>> 수신 only
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 6.
 */
public class AegisEndMsg {
	public String endYN = "";
	public String endCd = "";
	public String endMsg = "";
		
	public void setMsg(byte[] rcvByte) {
		endYN 	= StringUtil.bytesToString(rcvByte, 10,  1).trim();    // 처리결과
		endCd 	= StringUtil.bytesToString(rcvByte, 11,  4).trim();    // 결과코드
		endMsg 	= StringUtil.bytesToString(rcvByte, 15, 30).trim();    // 결과메세지
	}

	@Override
	public String toString() {
		return "AegisEndMsg [endYN=" + endYN + ", endCd=" + endCd + ", endMsg=" + endMsg + "]";
	}
}
