package kr.co.finger.shinhandamoa.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class XlsxEventParserTest {
    @Test
    public void test() {
        assertEquals(1, new XlsxEventParser().getColumnNo("A"));
        assertEquals(27, new XlsxEventParser().getColumnNo("AA"));
        assertEquals(52, new XlsxEventParser().getColumnNo("AZ"));
        System.out.println(new XlsxEventParser().getColumnNo("AZ"));
    }
}