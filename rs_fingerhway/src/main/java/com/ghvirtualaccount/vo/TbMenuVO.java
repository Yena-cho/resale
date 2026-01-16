package com.ghvirtualaccount.vo;

public class TbMenuVO {

	private String	menuId;			//메뉴ID    
	private String	menuNm;			//메뉴명    
	private String	upperMenuId;	//상위메뉴ID
	private String	menuLvl;		//메뉴레벨  
	private int		dispSeq;		//표시순서  
	private String	ind;			//구분      
	private String	menuUrl;		//메뉴경로  
	private String	useYn;			//사용여부  
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getUpperMenuId() {
		return upperMenuId;
	}
	public void setUpperMenuId(String upperMenuId) {
		this.upperMenuId = upperMenuId;
	}
	public String getMenuLvl() {
		return menuLvl;
	}
	public void setMenuLvl(String menuLvl) {
		this.menuLvl = menuLvl;
	}
	public int getDispSeq() {
		return dispSeq;
	}
	public void setDispSeq(int dispSeq) {
		this.dispSeq = dispSeq;
	}
	public String getInd() {
		return ind;
	}
	public void setInd(String ind) {
		this.ind = ind;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
}
