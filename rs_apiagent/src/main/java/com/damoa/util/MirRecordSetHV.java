/*
 * 작성된 날짜: 2004. 4. 6.
 *
 *
 */
package com.damoa.util;

import java.util.Vector;

/**
 * @author Mir
 *
 * <br>반복하는 데이터 집합용 클래스. HashVector이 모인 Vector임. 필드명은 대문자가 기본.<br>
 *<pre>
 *MirRecord mir = new MirRecord();
 *MirRecordSetHV mirREC = new MirRecordSetHV();
 *HashVector hv = new HashVector();
 *
 *for (condition) {
 *    hv.put(fieldName1, fieldValue1);
 *    hv.put(fieldName2, fieldValue2);
 *    hv.put(fieldName3, fieldValue3);
 *     .
 *     .
 *  mirREC.add(hv);
 *} // end for
 *
 *mir.setRec("REC", mirREC);
 *HashVector뿐만 아니라 object도 넣을 수 있다.
 *</pre>
 *<pre>
 *MirRecord mir = new MirRecord();
 *MirRecordSetHV mirREC = new MirRecordSetHV();
 *
 *for (condition) {
 *    mirREC.more();
 *    mirREC.set(fieldName1, fieldValue1);
 *    mirREC.set(fieldName2, fieldValue2);
 *    mirREC.set(fieldName3, fieldValue3);
 *     .
 *     .
 *} // end for
 *
 *mir.setRec("REC", mirREC);
 *HashVector뿐만 가능한 세팅법. more()부터 써주고 해당 레코드셋(인덱스)의 값을 세팅해야 함.
 *</pre>
 */
@SuppressWarnings("rawtypes")
public class MirRecordSetHV extends Vector {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8604783716408110517L;
	public int size = 0;
    public int index = 0;
    public int nowIndex = 0;
    public HashVector hv = null;

    public MirRecordSetHV() {
        super();
    } // end constructor

    /**
     * 다음반복부 유무
     * @return boolean 있으면 true, 없으면 false.
     */
    public boolean isnext() {
        if (index<size) return true;
        else return false;
    } // end isnext

    /**
     * 다음반복부로 이동.
     */
    public void next() {
        index = index + 1;
    } // end next

    /**
     * 첫반복부로 이동.
     */
    public void first() {
        index = 0;
    } // end first

    /**
     * 마지막반복부로 이동.
     */
    public void last() {
        index = size - 1;
    } // end last

    /**
     * 원하는 인덱스 위치로 이동.
     * @param idx 이동하고자 하는 인덱스. 0부터 size-1까지 가능. 0보다 작으면 first(), size보다 크거나 같으면 last()와 동일.
     */
    public void go(int idx) {
        if (idx<0) index = 0;
        else if (idx<size) index = idx;
        else index = size - 1;
    } // end last

    /**
     * 반복부 하나 추가.(추가로 값 세팅하거나 할때)
     */
    @SuppressWarnings("unchecked")
	public void more() {
        size++;
        if (size > 1) index++;
        hv = null;
        hv = new HashVector();
        add(hv);
    } // end more

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(String).
     */
    @SuppressWarnings("unchecked")
	public void set(String fieldNm, String value) {
        if (hv == null) hv = new HashVector();
        hv.put(fieldNm.toUpperCase(), value);
        set(index, hv);
    } // end set

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(Object).
     */
    @SuppressWarnings("unchecked")
	public void set(String fieldNm, Object value) {
        if (hv == null) hv = new HashVector();
        hv.put(fieldNm.toUpperCase(), value);
        set(index, hv);
    } // end set

    /**
     * 데이터 얻기
     * @param fieldNm 필드이름
     * @return String 필드값
     */
    public String getFieldString(String fieldNm) {
        return (String)((HashVector)get(index)).get(fieldNm.toUpperCase());
    } // end getFieldString

    /**
     * 데이터 얻기. recordSet으로 데이터를 저장했을때 저장 위치의 인덱스로 값을 추출. 1부터 시작.
     * @param idx 필드위치
     * @return String 필드값
     */
    public String getFieldString(int idx) {
        return (String)((HashVector)get(index)).get((String)((HashVector)get(index)).getKey(idx-1));
    } // end getFieldString

    /**
     * 데이터 얻기
     * @param fieldNm 필드이름
     * @return String 필드값
     */
    public Object get(String fieldNm) {
        return ((HashVector)get(index)).get(fieldNm.toUpperCase());
    } // end get

    /**
     * 현재 레코드셋 포인터의 HashVector 반환
     * @return String 필드값
     */
    public HashVector getHV() {
        return (HashVector)get(index);
    } // end getHV        

} // end class MirRecordSetHV
