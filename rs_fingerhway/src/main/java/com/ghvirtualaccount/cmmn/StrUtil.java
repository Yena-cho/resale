package com.ghvirtualaccount.cmmn;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrUtil {

	//Object가 null 일 경우, "" 값을 리턴 한다.
	public static String nullToVoid(Object obj){
		if(obj == null){
				return "";
		}
		return (String)obj;
	}
	
	//현재 월도를 구해서 리턴 한다.
	public String getCurrentMonthStr(){
		Date d3 = new Date(); 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMM");
		return transFormat.format(d3);
	}

	//현재 월도을 기준으로 month 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다.
	public String getCalMonthStr(int month){
		Date d3 = new Date(); 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d3);
		cal.add(Calendar.MONTH, month);
		
		return transFormat.format(cal.getTime());
	}

	//현재 월도을 기준으로 month 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다. yyyy년MM월 형태로 리턴
	public String getCalMonthFullStr(int month){
		Date d3 = new Date(); 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년MM월");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d3);
		cal.add(Calendar.MONTH, month);
		
		return transFormat.format(cal.getTime());
	}
	
	//현재 날짜를 리턴 한다.
	public String getCurrentDateStr(){
		Date d3 = new Date(); 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd"); 
		return transFormat.format(d3);
	}
	
	//현재 월도을 기준으로 month 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다.
	public String getCalMonthDateStr(int month){
		Date d3 = new Date(); 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d3);
		cal.add(Calendar.MONTH, month);
		
		return transFormat.format(cal.getTime());
	}
	
	//현재 일자를 기준으로 day 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다.
	public String getCalDayDateStr(int day){
		Date d3 = new Date(); 
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d3);
		cal.add(Calendar.DATE, day);
		
		return transFormat.format(cal.getTime());
	}
	
	//콤마(,)를 제거한다.
	public String getAmtString(String amt){
		if(amt!=null&&!amt.equals("")){
			return amt.replaceAll(",", "").trim();
		} else {
			return "0";
		}
	}
	
	//현재 월을 포함한 cnt만큼 월도 목록을 만들어 리턴 한다. keyStr=yyyymm, dpStr=yyyy년mm월
	public List<Map<String, Object>> getMonthList(int cnt){
		List<Map<String, Object>> monthList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<cnt;i++){
			Map<String, Object> monthMap = new HashMap<String, Object>();
			
			monthMap.put("keyStr",getCalMonthStr((i*-1)));
			monthMap.put("dpStr",getCalMonthFullStr((i*-1)));
			monthList.add(i, monthMap);
		}
		return monthList;
	}
	
	public String strCut(String szText, int nLength) throws Exception { // 문자열 자르기
		String r_val = szText;
		int oF = 0, oL = 0, rF = 0, rL = 0;
		int nLengthPrev = 0;
		try {
			byte[] bytes = r_val.getBytes("UTF-8"); // 바이트로 보관
			// x부터 y길이만큼 잘라낸다. 한글안깨지게.
			int j = 0;
			if (nLengthPrev > 0)
				while (j < bytes.length) {
					if ((bytes[j] & 0x80) != 0) {
						oF += 2;
						rF += 3;
						if (oF + 2 > nLengthPrev) {
							break;
						}
						j += 3;
					} else {
						if (oF + 1 > nLengthPrev) {
							break;
						}
						++oF;
						++rF;
						++j;
					}
				}
			j = rF;
			while (j < bytes.length) {
				if ((bytes[j] & 0x80) != 0) {
					if (oL + 2 > nLength) {
						break;
					}
					oL += 2;
					rL += 3;
					j += 3;
				} else {
					if (oL + 1 > nLength) {
						break;
					}
					++oL;
					++rL;
					++j;
				}
			}
			r_val = new String(bytes, rF, rL, "UTF-8"); // charset 옵션
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return r_val;
	}

}
