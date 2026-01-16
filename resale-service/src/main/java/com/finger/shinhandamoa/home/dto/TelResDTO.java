package com.finger.shinhandamoa.home.dto;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public class TelResDTO {

	private int no;				
	private int bbs;			//구분값 9 : 전화상담예약
	private int num;			
	private String id;			//작성자 ID
	private String writer;		//작성자
	private String title;		//제목
	private String contents;	//내용
	private String code;		//사용자가 입력한 기관 코드		
	private String data1;		//전화번호
	private String data2;		//상담예약사유코드
	private String data3;		//상담예약사유
	public String getData4() {
		return data4;
	}
	public void setData4(String data4) {
		this.data4 = data4;
	}
	private String data4;		//상담진행상태(대기중:1, 징행중 :2, 답변완료: 3)
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getBbs() {
		return bbs;
	}
	public void setBbs(int bbs) {
		this.bbs = bbs;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getData1() {
		return data1;
	}
	public void setData1(String data1) {
		this.data1 = data1;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data2) {
		this.data2 = data2;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data3) {
		this.data3 = data3;
	}
	@Override
	public String toString() {
		return "TelResDTO [no=" + no + ", bbs=" + bbs + ", num=" + num + ", id=" + id + ", writer=" + writer
				+ ", title=" + title + ", contents=" + contents + ", data1=" + data1 + ", data2=" + data2 + ", data3="
				+ data3 + "]";
	}
	
}
