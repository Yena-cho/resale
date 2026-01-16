package com.ghvirtualaccount.vo;

public class TbReceiptLastDownDtVO {
	
	private String	procDttm;		//처리일시    
	private String	inqStartDt;		//조회시작일자      
	private String	inqEndDt;		//조회종료일자
	
	public String getProcDttm() {
		return procDttm;
	}
	public void setProcDttm(String procDttm) {
		this.procDttm = procDttm;
	}
	public String getInqStartDt() {
		return inqStartDt;
	}
	public void setInqStartDt(String inqStartDt) {
		this.inqStartDt = inqStartDt;
	}
	public String getInqEndDt() {
		return inqEndDt;
	}
	public void setInqEndDt(String inqEndDt) {
		this.inqEndDt = inqEndDt;
	}
}
