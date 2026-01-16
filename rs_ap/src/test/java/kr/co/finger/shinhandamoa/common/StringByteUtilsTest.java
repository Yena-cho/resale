package kr.co.finger.shinhandamoa.common;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class StringByteUtilsTest {

    @Test
    public void left() {
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 1), "");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 2), "신");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 3), "신");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 4), "신한");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 5), "신한 ");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 6), "신한 ");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 7), "신한 다");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 8), "신한 다");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 9), "신한 다모");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 10), "신한 다모");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 11), "신한 다모아");
        assertEquals(StringByteUtils.left("신한 다모아", Charset.forName("EUC-KR"), 12), "신한 다모아");
    }
}