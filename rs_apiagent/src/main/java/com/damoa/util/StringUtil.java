package com.damoa.util;

import java.nio.charset.Charset;

import com.damoa.comm.ClientInfo;

public class StringUtil {

    public StringUtil()    {
    } // end constructor

    /**
     * 문자열 뒤집기
     * @param param String
     * @return String 뒤집은 문자열
     */
    public static String reverseStr(String param) {
        if (param == null)
            return "";
        else {
            String text = "";
            for (int i=0; i<param.length(); i++) {
                text = param.substring(i, i+1) + text;
            } // end for
            return text;
        }

    } // end reverseStr

    /**
     * String이나 Object가 null이면 빈 String "" 반환.
     * @param param String 또는 Object
     * @return String 빈문자열이나 트림된 결과값.
     */
    public static String null2void(String param) {
        try {
			if (param == null)
			    return "";
			else
			    return param.trim();
		} catch (Exception e) {
			return "";
		}
    } // end null2void

    /**
     * String이나 Object가 null이면 정수 0을 반환하거나 정수형으로 변환한 값 반환
     * @param param String 또는 Object
     * @return int 정수 0이나 정수형으로 변환한 값.
     */
    public static int null2zero(String param) {
        if (param == null)
            return 0;
        if (param.trim().equals("")) {
        	return 0;
        }
        else {
        	try {
				return Integer.parseInt(param);
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
				return 0;
			}
        }
    } // end null2zero

    /**
     * 입력값이 비었는지를 판단
     * @param param 입력 Object
     * @return boolean 참/거짓
     */
    public static boolean isEmpty(Object param) {
        if ((param == null) || param.equals(""))
            return true;
        else
            return false;
    } // end isEmpty


    /**
     * 숫자자리수 고정 : 9를 009로 하구 싶을때. 월, 날짜에서 넘길때 9일등을 09로 넘길때. StringUtil.setDigit("9", 3) -> 099
     * @param input 원 문자열
     * @param length 자리수
     * @return String 자리수 조정된 스트링
     */
    public static String setDigit(String input, int length) {
        if (isEmpty(input)) return "";
        int odd = length - input.length();
        if (odd > 0) {
            for (int i = 0; i < odd; i++) {
                input = "0" + input;
            } // end for
            return input;
        } else {
            return input;
        }
    } // end setDigit


    /**
     * String 왼쪽 정렬 패딩. 원하는 자리수로 문자열을 정렬시키고 나머지는 필러로 채움. 원래문자 길이가 자리수보다 초과하면 자름. 필러가 ""이면 " "으로 세팅.
     * @param source 원래 문자열
     * @param padding 필러
     * @param length 자리수
     * @return String 패딩된 문자열
     */
    public static String lPad(String source, String padding, int length) {
        if (source == null) source = "";
        if (padding.equals("")) padding = " ";
        int tmpLength = length;
        if(source.getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)).length < tmpLength) tmpLength = source.getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)).length;
        String tmpStr = new String(unicodeCut(source.getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)), tmpLength),Charset.forName(ClientInfo.CHAR_ENCODING));
        StringBuffer res = new StringBuffer();
        res.append(tmpStr);
        int gap = length - tmpStr.getBytes(Charset.forName(ClientInfo.CHAR_ENCODING)).length;
        for (int i = 0; i < gap; i++){
            res.append(padding);
        } // end for
        return res.toString();
    } // end lPad

    /**
     * Byte 자를때 유니코드 중간에서 잘라야 하는 경우 쓰레기값 제거해서 반환.
     * @param inputB 원래 문자열바이트
     * @param length 자리수
     * @return byte[] 자르고 난 문자열바이트
     */
    public static byte[] unicodeCut(byte[] inputB, int length) {
        boolean boo_middle_cut = false;
        int unicodeSize = 0;
        for (int i=0;i<length;i++) {
            if (inputB[i] < 0x00) unicodeSize++;
        } // end for
        if (unicodeSize%2 == 1) boo_middle_cut = true;

        // 유니코드 가운데서 잘림.
        if (boo_middle_cut) return new String(inputB, 0, length-1,Charset.forName(ClientInfo.CHAR_ENCODING)).getBytes(Charset.forName(ClientInfo.CHAR_ENCODING));
            // 유니코드가 아님. 그냥 자를수 있음.
        else return new String(inputB, 0, length,Charset.forName(ClientInfo.CHAR_ENCODING)).getBytes(Charset.forName(ClientInfo.CHAR_ENCODING));

    } // end unicodeCut

    /**
     * http, https, ftp, mms의 자동 링크 조건 판단하여 변환.
     * @param input
     * @return String 자동링크 생성.
     */
    public String wwwLinkCon(String input) {
        if (input.length() < 7) return input;

        if (((input.length() > 3) && ("http".equals((input.substring(0,4))))) || ((input.length() > 4) && ("https".equals((input.substring(0,5))))) || ((input.length() > 2) && ("ftp".equals((input.substring(0,3))))) || ((input.length() > 2) && ("mms".equals((input.substring(0,3)))))) return "<a href=\"" + input + "\" target=\"_blank\">" + input + "</a>";
        else return input;
    } // end wwwLinkCon

    /**
     * byte에서 특정문자열을 변환한다.
     * @param bytes The bytes to be decoded into characters
     * @param offset The index of the first byte to decode
     * @param length The number of bytes to decode
     * @return
     */
    public static String bytesToString(byte[] bytes, int offset, int length) {
    	String checkString = "";
    	int byteLength = bytes.length;
    	if (bytes == null || byteLength <= 0) {
    		return "";
    	}
    	
		try {
			checkString = new String(bytes, offset, length, Charset.forName(ClientInfo.CHAR_ENCODING)).trim();
		} catch (Exception e) {
			//e.printStackTrace();
			if (byteLength <= offset) {
				checkString = "";
	    	} else if (byteLength >= offset && (byteLength-offset) <= length) {
	    		checkString = new String(bytes, offset, (byteLength-offset), Charset.forName(ClientInfo.CHAR_ENCODING)).trim();
	    	}
		}
    	return checkString;
    }
}
