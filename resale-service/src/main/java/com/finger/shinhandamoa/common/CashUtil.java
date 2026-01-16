package com.finger.shinhandamoa.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.org.receiptmgmt.dto.NotiDTO;

import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.commons.Maps;

public class CashUtil {

	private List<NotiDTO> notiDets = new ArrayList<>();
	
	public String findAmount() {
        long amount = 0;
        for (NotiDTO notiDet : getNotiDets()) {
            amount += notiDet.getAmount();
        }
        return amount + "";
    }
	
	public String findNoTaxYn(Map<String, Object> map) {
        return Maps.getValue(map, "NOTAXYN");
    }
	
	public String tax(String amount, String noTaxYn) {
        if ("Y".equalsIgnoreCase(noTaxYn)) {
            Long _amount = Damoas.toLong(amount);
            return (_amount * 0.1) + "";
        } else {
            return "0";
        }
    }
	
	public List<NotiDTO> getNotiDets() {
        return notiDets;
    }
	
	/**
	  * 숫자에 천단위마다 콤마 넣기
	  * @param long
	  * @return String
	  * */
	 public static String moneyFormat(long num) {
	  DecimalFormat df = new DecimalFormat("#,###,###,###");
	  return df.format(num);
	 }
}
