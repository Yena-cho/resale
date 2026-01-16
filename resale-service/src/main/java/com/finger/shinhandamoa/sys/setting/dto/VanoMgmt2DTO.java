package com.finger.shinhandamoa.sys.setting.dto;

/**
 * @author  정빛나
 * @date    2019. 9. 30.
 * @desc    최초생성
 * @version 
 * 
 */

/*
* 관리자페이지 > 가상계좌관리 > 기관별가상계좌보유현황 메뉴를 위한 DTO
*/
public class VanoMgmt2DTO {
	private int rn;
	private String chacd;
	private String regdt;
	private String chaname;
	private String chatrty;
	private String cusname;
	private String vano;
	private String useyn;
	private String lastrcpdate;
	private String totalcnt;
	private String miscnt;
	private String fgcd;
	private String assigndt;
	private String vastate;

	public String getVastate() {
		return vastate;
	}

	public void setVastate(String vastate) {
		this.vastate = vastate;
	}

	public String getAssigndt() {
		return assigndt;
	}

	public void setAssigndt(String assigndt) {
		this.assigndt = assigndt;
	}

	public String getFgcd() {
		return fgcd;
	}

	public void setFgcd(String fgcd) {
		this.fgcd = fgcd;
	}

	public String getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(String totalcnt) {
		this.totalcnt = totalcnt;
	}

	public String getMiscnt() {
		return miscnt;
	}

	public void setMiscnt(String miscnt) {
		this.miscnt = miscnt;
	}

	public String getChaname() {
		return chaname;
	}

	public void setChaname(String chaname) {
		this.chaname = chaname;
	}

	public int getRn() {
		return rn;
	}

	public void setRn(int rn) {
		this.rn = rn;
	}

	public String getChacd() {
		return chacd;
	}

	public void setChacd(String chacd) {
		this.chacd = chacd;
	}

	public String getRegdt() {
		return regdt;
	}

	public void setRegdt(String regdt) {
		this.regdt = regdt;
	}

	public String getChatrty() {
		return chatrty;
	}

	public void setChatrty(String chatrty) {
		this.chatrty = chatrty;
	}

	public String getCusname() {
		return cusname;
	}

	public void setCusname(String cusname) {
		this.cusname = cusname;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public String getUseyn() {
		return useyn;
	}

	public void setUseyn(String useyn) {
		this.useyn = useyn;
	}

	public String getLastrcpdate() {
		return lastrcpdate;
	}

	public void setLastrcpdate(String lastrcpdate) {
		this.lastrcpdate = lastrcpdate;
	}
}
