package com.damoa.msg;

import com.damoa.comm.ClientInfo;
import com.damoa.util.StringUtil;

import java.nio.charset.Charset;

/**
 * 납부결과 DATA VO
 * 
 * >>> 수신전용
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 6.
 */
public class AegisRMsg {
	public String DrecordType  	= "";
	public String serviceId    	= "";
	public String workField    	= "";
	public String seccessYN    	= "";
	public String resultCd     	= "";
	public String resultMsg    	= "";
	public String trNo         	= "";
	public String vaNo         	= "";
	public String payMasMonth  	= "";
	public String payMasDt     	= "";
	public String rcpDt			= "";
	public String rcpUsrName   	= "";
	public String bnkCd     	= "";
	public String bchCd     	= "";
	public String mdGubn     	= "";
	public String trnDay	    = "";
	public String rcpAmt     	= "";
	public String vaNoBnkCd 	= "";
	public String content1 		= "";
	public String content2 		= "";
	public String content3 		= "";
	public String content4 		= "";
	public String svecd	 		= "";
	public String chaTrNo      	= "";
	public String adjfiGrpCd   	= "";
	public String payItemNm    	= "";
	public String payItemAmt   	= "";

	public AegisRMsg(){}

	public void setMsg(byte[] rcvByte) {
		this.clear();

		if("DAMOA".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
			DrecordType  = StringUtil.bytesToString(rcvByte,   0,  1);     //
			serviceId    = StringUtil.bytesToString(rcvByte,   1,  8);     //    가맹점코드
			workField    = StringUtil.bytesToString(rcvByte,   9,  1);     //    처리내역
			seccessYN    = StringUtil.bytesToString(rcvByte,  10,  1);    //    처리결과
			resultCd     = StringUtil.bytesToString(rcvByte,  11,  4);    //    처리결과코드
			resultMsg    = StringUtil.bytesToString(rcvByte,  15, 30);   //    처리결과메시지
			trNo         = StringUtil.bytesToString(rcvByte,  45, 30).toUpperCase();//     청구거래번호
			vaNo         = StringUtil.bytesToString(rcvByte,  75, 20);   //    가상계좌번호
			payMasMonth  = StringUtil.bytesToString(rcvByte,  95,  6);    //    청구월
			payMasDt     = StringUtil.bytesToString(rcvByte, 101,  8);   //    청구일자
			rcpDt        = StringUtil.bytesToString(rcvByte, 109, 14);  //    수납일시
			rcpUsrName   = StringUtil.bytesToString(rcvByte, 123, 20);  //    수납계좌명
			bnkCd        = StringUtil.bytesToString(rcvByte, 143,  3);   //    처리일자
			bchCd        = StringUtil.bytesToString(rcvByte, 146,  4);   //    처리시간
			mdGubn       = StringUtil.bytesToString(rcvByte, 150,  1);   //    매체구분
			trnDay       = StringUtil.bytesToString(rcvByte, 151, 14);  //    전송일시
			rcpAmt       = StringUtil.bytesToString(rcvByte, 165, 10);  //    수납금액
			content1     = StringUtil.bytesToString(rcvByte, 175, 30);  //    참조1
			content2     = StringUtil.bytesToString(rcvByte, 205, 30);  //    참조2
			content3     = StringUtil.bytesToString(rcvByte, 235, 30);  //    참조3
			content4     = StringUtil.bytesToString(rcvByte, 265, 30);  //    참조4
			svecd        = StringUtil.bytesToString(rcvByte, 295,  3);   //    수납구분
			chaTrNo      = StringUtil.bytesToString(rcvByte, 420, 30).toUpperCase();  //    항목거래번호
			adjfiGrpCd   = StringUtil.bytesToString(rcvByte, 450,  5);   //    신한은행발급배분키
			payItemNm    = StringUtil.bytesToString(rcvByte, 455, 50);  //    항목명
			payItemAmt   = StringUtil.bytesToString(rcvByte, 505, 10);  //    항목금액

		}else if("THEBILL".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
			DrecordType  = StringUtil.bytesToString(rcvByte,   0,  1).trim();
			serviceId    = StringUtil.bytesToString(rcvByte,   1,  8).trim();     // 가맹점코드
			workField    = StringUtil.bytesToString(rcvByte,   9,  1).trim();     // 처리내역
			seccessYN    = StringUtil.bytesToString(rcvByte,  10,  1).trim();    // 처리결과
			resultCd     = StringUtil.bytesToString(rcvByte,  11,  4).trim();    // 처리결과
			resultMsg    = StringUtil.bytesToString(rcvByte,  15, 30).trim();   // 처리결과
			trNo         = StringUtil.bytesToString(rcvByte,  45, 30).trim().toUpperCase();    // 청구거래번호
			vaNo         = StringUtil.bytesToString(rcvByte,  75, 20).trim();   // 가상계좌번호
			payMasMonth  = StringUtil.bytesToString(rcvByte,  95,  6).trim();    // 청구월
			payMasDt     = StringUtil.bytesToString(rcvByte, 101,  8).trim();   // 청구일자
			rcpDt		 = StringUtil.bytesToString(rcvByte, 109, 14).trim();  // 수납일시
			rcpUsrName   = StringUtil.bytesToString(rcvByte, 123, 20).trim();  // 수납계좌명
			bnkCd     	 = StringUtil.bytesToString(rcvByte, 143,  3).trim();   // 처리일자
			bchCd     	 = StringUtil.bytesToString(rcvByte, 146,  4).trim();   // 처리시간
			mdGubn     	 = StringUtil.bytesToString(rcvByte, 150,  1).trim();   // 매체구분
			trnDay	     = StringUtil.bytesToString(rcvByte, 151, 14).trim();  // 전송일시
			rcpAmt     	 = StringUtil.bytesToString(rcvByte, 165, 10).trim();  // 수납금액
			vaNoBnkCd 	 = StringUtil.bytesToString(rcvByte, 175,  3).trim();   // 가상계좌은행
			svecd	 	 = StringUtil.bytesToString(rcvByte, 178,  3).trim();   // 수납구분(VAS,OCD)
                           
			chaTrNo      = StringUtil.bytesToString(rcvByte, 420, 30).trim();  // 항목거래번호
			adjfiGrpCd   = StringUtil.bytesToString(rcvByte, 450,  5).trim();   // 신한은행발급배분키
			payItemNm    = StringUtil.bytesToString(rcvByte, 455, 50).trim();  // 항목명
			payItemAmt   = StringUtil.bytesToString(rcvByte, 505, 10).trim();  // 항목금액
		}
	}


	public void clear() {
		DrecordType = "";
		serviceId   = "";
		workField   = "";
		seccessYN   = "";
		resultCd    = "";
		resultMsg   = "";
		trNo        = "";
		vaNo        = "";
		payMasMonth = "";
		payMasDt    = "";
		rcpDt		= "";
		rcpUsrName  = "";
		bnkCd     	= "";
		bchCd     	= "";
		mdGubn     	= "";
		trnDay	    = "";
		rcpAmt     	= "";
		vaNoBnkCd 	= "";
		content1 	= "";
		content2 	= "";
		content3 	= "";
		content4 	= "";
		svecd	 	= "";
		chaTrNo     = "";
		adjfiGrpCd  = "";
		payItemNm   = "";
		payItemAmt  = "";
	}

	@Override
	public String toString() {
		return "AegisRMsg [DrecordType=" + DrecordType + ", serviceId=" + serviceId + ", workField=" + workField
				+ ", seccessYN=" + seccessYN + ", resultCd=" + resultCd + ", resultMsg=" + resultMsg + ", trNo=" + trNo
				+ ", vaNo=" + vaNo + ", payMasMonth=" + payMasMonth + ", payMasDt=" + payMasDt + ", rcpDt=" + rcpDt
				+ ", rcpUsrName=" + rcpUsrName + ", bnkCd=" + bnkCd + ", bchCd=" + bchCd + ", mdGubn=" + mdGubn
				+ ", trnDay=" + trnDay + ", rcpAmt=" + rcpAmt + ", vaNoBnkCd=" + vaNoBnkCd + ", content1=" + content1
				+ ", content2=" + content2 + ", content3=" + content3 + ", content4=" + content4 + ", svecd=" + svecd
				+ ", chaTrNo=" + chaTrNo + ", adjfiGrpCd=" + adjfiGrpCd + ", payItemNm=" + payItemNm + ", payItemAmt="
				+ payItemAmt + "]";
	}
}
