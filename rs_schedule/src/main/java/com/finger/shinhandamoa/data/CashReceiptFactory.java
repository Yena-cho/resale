package com.finger.shinhandamoa.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

import com.finger.shinhandamoa.data.model.CashReceiptMaster;

/**
 * KIS 전문을 CashReceiptMaster 객체로 변환한다.
 * @author jhjeong@finger.co.kr
 * @modified 2018. 10. 24.
 */
public class CashReceiptFactory {
	
	final SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
	
	public static CashReceiptMaster parse(final String message) {
		
		if (message == null) return null;
		if (message.length() < 200) return null;
		
		CashReceiptMaster data = new CashReceiptMaster();
		data.setClientTypeCode(getString(message, 2, 1)); // 고객 구분 (0: 일반, 1:사업자), 전문은 거래자구분
		data.setTxTypeCode(getString(message, 3, 1)); // 거래 구분 (0: 승인거래, 1: 취소거래)
		data.setDataNo(getString(message, 4, 9)); // 데이터 일련번호
		data.setMediaTypeCode(getString(message, 13, 1)); // 처리구분 (1: 주민번호, 2:핸드폰번호, 3:카드번호, 4:사업자번호)
		data.setHostIdentityNo(getString(message, 14, 12)); // 사업자등록번호
		data.setClientIdentityNo(getString(message, 26, 20)); // 고객 식별번호
		data.setClientNo(getString(message, 46, 20)); // 고객 번호
		data.setTxDate(getString(message, 74, 8)); // 거래일자 (매출일자)
		data.setPosEntry(getString(message, 82, 2)); // Swipe:01, Key-in:02
		data.setTxNo(getString(message, 93, 9)); // 승인번호 (KIS가 부여 : 현금 승인번호)
		data.setCancelTxNo(getString(message, 102, 9)); // 취소 승인번호 (현금 원승인번호)
		data.setTxAmount(NumberUtils.toLong(getString(message, 111, 12))); // 거래금액
		data.setTip(NumberUtils.toLong(getString(message, 123, 12))); // 봉사료
		data.setTax(NumberUtils.toLong(getString(message, 135, 12))); // 부가세액
		data.setCancelTxDate(getString(message, 147, 8)); // 원거래일자 (취소 시)
		data.setCurrencyCode(getString(message, 155, 3)); // 통화코드 (410:원화, 840:미화)
		data.setResponseCode(getString(message, 158, 4)); // 응답코드
		data.setUserDefine(getString(message, 162, 38)); // 사용자 정의 (요청사이용영역)
		data.setCreateDate(new Date()); // 등록일시
		data.setCreateUser("DamoaCashAgent"); // 등록자
		return data;
	}
	
	static protected final String getString(String source, int start, int length) {
		
		if (source == null) return null;
		if (source.length() <= start) return null;
		if (source.length() < length) return null;
		
		int endIndex = start + length;
		
		return source.substring(start, endIndex).trim();
	}
}
