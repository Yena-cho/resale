/*
 * 작성된 날짜: 2004. 3. 8.
 *
 *
 */
package damoa.comm.util;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.Vector;

import damoa.Constants;

/**
 * @author Mir
 *
 * <br>문자열 조작을 위한 클래스
 */
public class StringUtil {

    public StringUtil()    {
    } // end constructor
    
    /**
     * byte에서 특정문자열을 변환한다.
     * @param bytes The bytes to be decoded into characters
     * @param offset The index of the first byte to decode
     * @param length The number of bytes to decode
     * @return
     */
    public static String bytesToString(byte[] bytes, int offset, int length) {
    	String checkString = "";
    	
    	if (bytes == null || bytes.length <= 0) {
    		return ComCheck.lPad("", " ", length);
    	}
    	
		try {
			checkString = new String(bytes, offset, length, Constants.MY_CHARSET).trim();
		} catch (Exception e) {
			//e.printStackTrace();
			if (bytes.length <= offset) {
				checkString = "";
	    	} else if (bytes.length < length) {
	    		checkString = "";
	    	} else if (bytes.length > offset && bytes.length < length) {
	    		checkString = new String(bytes, offset, bytes.length, Constants.MY_CHARSET).trim();
	    	}
			
			checkString = ComCheck.lPad(checkString, " ", length);
		}
    	return checkString;
    }

    /**
     * String이나 Object가 null이면 빈 String "" 반환.
     * @param param String 또는 Object
     * @return String 빈문자열이나 트림된 결과값.
     */
    public String null2void(String param) {
        if (param == null)
            return "";
            //if (param.trim().equals(""))
            //return "";
        else
            //return trim(param.trim());
            return param;
    } // end null2void
    
    /**
     * 문자열 좌우 트림
     * @param s 원 문자열
     * @return String 트림된 스트링
     */
    public String trim(String s) {
        if (s == null)
            return "";
        int st = 0;
        char val[] = s.toCharArray();
        int count = val.length;
        int len;
        for(len = count; st < len && (val[st] <= ' ' || val[st] == '\u3000'); st++);
        for(; st < len && (val[len - 1] <= ' ' || val[len - 1] == '\u3000'); len--);
        return st <= 0 && len >= count ? s : s.substring(st, len);
    } // end trim

    /**
     * 입력값이 비었는지를 판단
     * @param param 입력 Object
     * @return boolean 참/거짓
     */
    public boolean isEmpty(Object param) {
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
    public String setDigit(String input, int length) {
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

} // end class StringUtil
