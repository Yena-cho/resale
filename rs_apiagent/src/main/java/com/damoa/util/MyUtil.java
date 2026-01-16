package com.damoa.util;

import java.nio.charset.Charset;

public class MyUtil {
    public static String newString(byte[] rcvByte,int offset,int length) {
    	StringUtil su = new StringUtil();
        try {
        	if (rcvByte.length <= 0) return su.lPad("", " ", length); 
        	return new String(rcvByte,offset,length,Charset.forName("EUC-KR")).trim();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(String.format("rcvByte=%d,offset=%d,length=%d", rcvByte.length, offset, length));
			return su.lPad("", " ", length);
		}
    }
}
