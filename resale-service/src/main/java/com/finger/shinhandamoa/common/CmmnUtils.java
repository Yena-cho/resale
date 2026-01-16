package com.finger.shinhandamoa.common;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * 공통 유틸
 * 
 * @author jhjeong@finger.co.kr
 * @author denny91@finger.co.kr
 *
 */
public class CmmnUtils {

    /**
     * Object type 변수가 비어있는지 체크
     * 
     * @param obj 
     * @return Boolean : true / false
     */
    public static boolean empty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List<?>) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map<?, ?>) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }
 
    /**
     * Object type 변수가 비어있지 않은지 체크
     * 
     * @param obj
     * @return Boolean : true / false
     */
    public static boolean notEmpty(Object obj) {
        return !empty(obj);
    }

    /**
     * 파일에 대한 mime-type을 반환
     * @param fileExt
     * @return
     */
    public static String getMimeType(String fileExt) {
    	
    	if (fileExt.equalsIgnoreCase("gif")) return "image/gif";
    	if (fileExt.equalsIgnoreCase("jpg")) return "image/jpg";
    	if (fileExt.equalsIgnoreCase("jpeg")) return "image/jpeg";
    	if (fileExt.equalsIgnoreCase("png")) return "image/png";
    	if (fileExt.equalsIgnoreCase("tiff")) return "image/tiff";
    	if (fileExt.equalsIgnoreCase("pdf")) return "application/pdf";
    	if (fileExt.equalsIgnoreCase("xls")) return "application/vnd.ms-excel";
    	if (fileExt.equalsIgnoreCase("xlsx")) return "application/vnd.ms-excel";
    	
    	return null;
    }
    
    public static String getMimeType(java.io.File file) {
    
    	if (file == null) return "";
    	
    	int pos = file.getName().lastIndexOf( "." );
		String fileExt = file.getName().substring( pos + 1 );
    	
    	if (fileExt.equalsIgnoreCase("gif")) return "image/gif";
    	if (fileExt.equalsIgnoreCase("jpg")) return "image/jpg";
    	if (fileExt.equalsIgnoreCase("jpeg")) return "image/jpeg";
    	if (fileExt.equalsIgnoreCase("png")) return "image/png";
    	if (fileExt.equalsIgnoreCase("tiff")) return "image/tiff";
    	if (fileExt.equalsIgnoreCase("pdf")) return "application/pdf";
    	if (fileExt.equalsIgnoreCase("xls")) return "application/vnd.ms-excel";
    	if (fileExt.equalsIgnoreCase("xlsx")) return "application/vnd.ms-excel";
    	
    	return null;
    }

	/*
	 * Clob 를 String 으로 변경
	 */
	public static String clobToString(Clob clob) throws IOException, SQLException {

		if (clob == null) {
			return "";
		}

		StringBuffer strOut = new StringBuffer();

		String str = "";

		BufferedReader br = new BufferedReader(clob.getCharacterStream());

		while ((str = br.readLine()) != null) {
			strOut.append(str);
		}
		return strOut.toString();
	}

	/**
	 * 날짜 포멧 유효성 체크
	 *
	 * @return
	 */
	public static boolean validateDateFormat(final String date) {
		if(StringUtils.defaultString(date).length() != 6){
			return false;
		}
		String formatStrings = "yyyyMM";

		SimpleDateFormat dateFormatParser = new SimpleDateFormat(formatStrings, Locale.KOREA);
		dateFormatParser.setLenient(false);
		try {
			dateFormatParser.parse(date);
			return true;
		} catch (Exception Ex) {
			return false;
		}
	}

	/**
	 * 날짜 포멧 유효성 체크 8자리
	 *
	 * @return
	 */
	public static boolean validateDateFormat2(final String date) {
		if(StringUtils.defaultString(date).length() != 8){
			return false;
		}
		String formatStrings = "yyyyMMdd";

		SimpleDateFormat dateFormatParser = new SimpleDateFormat(formatStrings, Locale.KOREA);
		dateFormatParser.setLenient(false);
		try {
			dateFormatParser.parse(date);
			return true;
		} catch (Exception Ex) {
			return false;
		}
	}
}
