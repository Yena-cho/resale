/*
 * Created on 2004. 9. 2.
 *
 */
package com.damoa.util;

import java.util.Vector;
/**
 * @author Mir
 *
 * <br>반복없는 데이터 집합용 클래스<br>MirRecord와 거의 흡사하나
 * <br>recordSet과 관계된 사항들이 없고, 벡터를 이용하며 세팅순서대로 들어간다(key, value 벡터 두개).
 * <br>필드명은 대문자로 세팅된다.
 */
public class HashVector {
	private Vector<Object> vtKey;
	private Vector<Object> vtValue;

	public HashVector()    {
        vtKey = new Vector<Object>();
        vtValue = new Vector<Object>();
    } // end constructor

    /**
     * toString
     * @return String 필드값(string)
     */
    public String toString() {
        return vtKey.toString()+System.getProperty("line.separator")+vtValue.toString();
    } // end toString

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(string)
     */
    public void set(String fieldNm, String value) {
        mapKeyValue(fieldNm.toUpperCase(), value);
    } // end set

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(string)
     */
    public void put(String fieldNm, String value) {
        mapKeyValue(fieldNm.toUpperCase(), value);
    } // end put

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(object)
     */
    public void set(String fieldNm, Object value) {
        mapKeyValue(fieldNm.toUpperCase(), value);
    } // end set

    /**
     * 데이터 세팅
     * @param fieldNm 필드이름
     * @param value 필드값(object)
     */
    public void put(String fieldNm, Object value) {
        mapKeyValue(fieldNm.toUpperCase(), value);
    } // end set

    /**
     * 데이터 세팅
     * @param index 값인덱스
     * @param value 필드값(string)
     */
    public void set(int index, String value) {
        vtValue.set(index, value);
    } // end set

    /**
     * 데이터 세팅
     * @param index 값인덱스
     * @param value 필드값(string)
     */
    public void put(int index, String value) {
        vtValue.set(index, value);
    } // end put

    /**
     * 데이터 세팅
     * @param index 값인덱스
     * @param value 필드값(object)
     */
    public void set(int index, Object value) {
        vtValue.set(index, value);
    } // end set

    /**
     * 데이터 세팅
     * @param index 값인덱스
     * @param value 필드값(object)
     */
    public void put(int index, Object value) {
        vtValue.set(index, value);
    } // end set

    /**
     * 데이터 세팅. 추가개념이고 key명은 "field"+index로 자동 세팅.
     * @param value 필드값(string)
     */
    public void set(String value) {
        vtKey.add("field"+(vtKey.size()+1));
        vtValue.add(value);
    } // end set

    /**
     * 데이터 세팅. 추가개념이고 key명은 "field"+index로 자동 세팅.
     * @param value 필드값(string)
     */
    public void put(String value) {
        vtKey.add("field"+(vtKey.size()+1));
        vtValue.add(value);
    } // end put

    /**
     * 데이터 세팅. 추가개념이고 key명은 "field"+index로 자동 세팅.
     * @param value 필드값(object)
     */
    public void set(Object value) {
        vtKey.add("field"+(vtKey.size()+1));
        vtValue.add(value);
    } // end set

    /**
     * 데이터 세팅. 추가개념이고 key명은 "field"+index로 자동 세팅.
     * @param value 필드값(object)
     */
    public void put(Object value) {
        vtKey.add("field"+(vtKey.size()+1));
        vtValue.add(value);
    } // end set

    /**
     * 데이터 얻기
     * @param fieldNm 필드이름
     * @return String 필드값
     */
    public String getFieldString(String fieldNm) {
        if (vtKey.contains(fieldNm.toUpperCase())) return (String)vtValue.get(vtKey.indexOf(fieldNm.toUpperCase()));
        else return null;
    } // end getFieldString

    /**
     * 데이터 얻기
     * @param fieldNm 필드이름
     * @return Object 필드값
     */
    public Object get(String fieldNm) {
        if (vtKey.contains(fieldNm.toUpperCase())) return (Object)vtValue.get(vtKey.indexOf(fieldNm.toUpperCase()));
        else return null;
    } // end get

    /**
     * 데이터 얻기
     * @param index 값인덱스
     * @return String 필드값
     */
    public String getFieldString(int index) {
        return (String)vtValue.get(index);
    } // end getFieldString

    /**
     * 데이터 얻기
     * @param index 값인덱스
     * @return Object 필드값
     */
    public Object get(int index) {
        return (Object)vtValue.get(index);
    } // end get

    /**
     * 데이터 얻기
     * @param index 값인덱스
     * @return String 필드값
     */
    public String getKeyString(int index) {
        return (String)vtKey.get(index);
    } // end getFieldString

    /**
     * 데이터 얻기
     * @param index 값인덱스
     * @return Object 필드값
     */
    public Object getKey(int index) {
        return (Object)vtKey.get(index);
    } // end get

    /**
     * 데이터개수 얻기
     * @return int 데이터개수
     */
    public int size() {
        return vtKey.size();
    } // end size

    /**
     * 데이터 지우기
     * @param fieldNm 필드이름
     */
    public void del(String fieldNm) {
        if (vtKey.contains(fieldNm.toUpperCase())) {
            vtValue.remove(vtKey.indexOf(fieldNm.toUpperCase()));
            vtKey.remove(fieldNm.toUpperCase());
        }
    } // end del

    /**
     * key 벡터 반환
     * @return Vector key vector 반환
     */
    public Vector<Object> getKey() {
        return vtKey;
    } // end getKey

    /**
     * 값 세팅.
     * @param key 필드이름
     * @param value 값
     */
    private void mapKeyValue(String key, String value) {
        if (vtKey.contains(key)) {
            vtValue.set(vtKey.indexOf(key), value);
        } else {
            vtKey.add(key);
            vtValue.add(value);
        }
    } // end mapKeyValue

    /**
     * 값 세팅.
     * @param key 필드이름
     * @param value 값
     */
    private void mapKeyValue(Object key, Object value) {
        if (vtKey.contains(key)) {
            vtValue.set(vtKey.indexOf(key), value);
        } else {
            vtKey.add(key);
            vtValue.add(value);
        }
    } // end mapKeyValue

} // end class HashVector
