/*
 * 작성된 날짜: 2004. 3. 15.
 *
 *
 */
package com.damoa.util;

import java.util.Hashtable;

/**
 * @author Mir
 *
 * <br>반복없는 데이터 집합용 클래스<br>Hashtable을 이용함. 필드명은 대문자로 세팅된다.
 * <br>인스턴스 생성시 RECSIZE를 0으로 세팅한다(MirRecordSet이 없을수도 있으므로).
 */
public class MirRecord {
    @SuppressWarnings("rawtypes")
	private Hashtable ht;
    private String mirRECnm;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public MirRecord()    {
        ht = new Hashtable();
        ht.put("RECSIZE", "0");
    } // end constructor

    /**
     * toString
     * @return String 필드값(string)
     */
    public String toString() {
        return ht.toString();
    } // end toString

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(string)
     */
    @SuppressWarnings("unchecked")
	public void set(String fieldNm, String value) {
        ht.put(fieldNm.toUpperCase(), value);
    } // end set

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(object)
     */
    @SuppressWarnings("unchecked")
	public void set(String fieldNm, Object value) {
        ht.put(fieldNm.toUpperCase(), value);
    } // end set

    /**
     * 데이터 얻기
     * @param fieldNm 필드이름
     * @return String 필드값
     */
    public String getFieldString(String fieldNm) {
        return (String)ht.get(fieldNm.toUpperCase());
    } // end getFieldString

    /**
     * 데이터 얻기
     * @param fieldNm 필드이름
     * @return Object 필드값
     */
    public Object get(String fieldNm) {
        return ht.get(fieldNm.toUpperCase());
    } // end get

    /**
     * 데이터개수 얻기
     * @return int 데이터개수
     */
    public int size() {
//    	return ht.size();
        return Integer.parseInt((String)ht.get("RECSIZE"));
    } // end size

    /**
     * 데이터 지우기
     * @param fieldNm 필드이름
     */
    public void del(String fieldNm) {
        if (ht.get(fieldNm.toUpperCase()) != null) ht.remove(fieldNm.toUpperCase());
    } // end del

    /**
     * 반복부 데이터(MirRecordSet) 세팅. RECSIZE라는 필드명으로 반복부 개수가 세팅된다.
     * @param fieldNm 필드이름
     * @param mirREC 필드값(MirRecordSet)
     */
    @SuppressWarnings("unchecked")
	public void setRec(String fieldNm, MirRecordSet mirREC) {
        mirRECnm = fieldNm.toUpperCase();
        mirREC.size = mirREC.size();
        ht.put("RECSIZE", String.valueOf(mirREC.size()));
        ht.put(fieldNm.toUpperCase(), mirREC);
    } // end setRec

    /**
     * 데이터 얻기
     * @return MirRecordSet 필드값
     */
    public MirRecordSet getRec() {
        return (MirRecordSet)ht.get(mirRECnm);
    } // end getRec

    /**
     * 반복부 데이터(MirRecordSetHV) 세팅. RECSIZE라는 필드명으로 반복부 개수가 세팅된다.
     * @param fieldNm 필드이름
     * @param mirREC 필드값(MirRecordSetHV)
     */
    @SuppressWarnings("unchecked")
	public void setRec(String fieldNm, MirRecordSetHV mirREC) {
        mirRECnm = fieldNm.toUpperCase();
        mirREC.size = mirREC.size();
        ht.put("RECSIZE", String.valueOf(mirREC.size()));
        ht.put(fieldNm.toUpperCase(), mirREC);
    } // end setRec

    /**
     * 데이터 얻기
     * @return MirRecordSetHV 필드값
     */
    public MirRecordSetHV getRecHV() {
        return (MirRecordSetHV)ht.get(mirRECnm);
    } // end getRec

} // end class mirRecord
