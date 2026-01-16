package com.ghvirtualaccount.vo;

public class TbVirtualAcntVO {

	private String	virtualAcntNum;	//가상계좌번호 
	private String	payDueDt;		//납무마감일
	private String	useYn;     	//사용여부
	
	public String getVirtualAcntNum() {
		return virtualAcntNum;
	}
	public void setVirtualAcntNum(String virtualAcntNum) {
		this.virtualAcntNum = virtualAcntNum;
	}
	public String getPayDuDt() {
		return payDueDt;
	}
	public void setPayDuDt(String payDuDt) {
		this.payDueDt = payDuDt;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

}
