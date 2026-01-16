package com.finger.shinhandamoa.main.dto;

import java.util.Date;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public class FaqDTO {

	private int rn;
	private int faqNo;
	private String title;
	private String gubunCd;
	private String content;
	private Date regDt;
	private String regEmp;
	
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public int getFaqNo() {
		return faqNo;
	}
	public void setFaqNo(int faqNo) {
		this.faqNo = faqNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGubunCd() {
		return gubunCd;
	}
	public void setGubunCd(String gubunCd) {
		this.gubunCd = gubunCd;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getRegEmp() {
		return regEmp;
	}
	public void setRegEmp(String regEmp) {
		this.regEmp = regEmp;
	}
	@Override
	public String toString() {
		return "FaqDTO [rn=" + rn + ", faqNo=" + faqNo + ", title=" + title + ", gubunCd=" + gubunCd + ", content="
				+ content + ", regDt=" + regDt + ", regEmp=" + regEmp + "]";
	}
	
}
