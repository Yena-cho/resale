package kr.co.finger.shinhandamoa.common;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * 문자열 바이트 유틸리티
 *
 * @author wisehouse@finger.co.kr
 */
public class StringByteUtils {
    private StringByteUtils() {
        throw new RuntimeException(new IllegalAccessException());
    }

    /**
     * 문자열의 왼쪽을 바이트 길이로 #{char} 단위로 자른다.
     *
     * <pre>
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 1) = ""
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 2) = "신"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 3) = "신"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 4) = "신한"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 5) = "신한 "
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 6) = "신한 "
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 7) = "신한 다"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 8) = "신한 다"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 9) = "신한 다모"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 10) = "신한 다모"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 11) = "신한 다모아"
     *     StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 12) = "신한 다모아"
     * </pre>
     *
     * @param input
     * @param charset
     * @param maxLength
     * @return
     */
    public static String left(String input, Charset charset, int maxLength) {
        if(input == null) {
            return input;
        }

        if(input.getBytes(charset).length <= maxLength) {
            return input;
        }

        int finalLength = 0;
        for(int i=0; i<input.length(); i++) {
            if(StringUtils.left(input, i).getBytes(charset).length > maxLength) {
                break;
            }

            finalLength = i;
        }

        return StringUtils.left(input, finalLength);
    }
}
