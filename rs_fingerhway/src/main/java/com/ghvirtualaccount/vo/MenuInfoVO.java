package com.ghvirtualaccount.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuInfoVO {

	private String	menuId;			//메뉴ID    
	private String	menuNm;			//메뉴명    
	private String	menuLvl;		//메뉴레벨  
	private String	menuUrl;		//메뉴경로  
	private String	ind;			//구분
	private String	defaultYn;		//기본메뉴여부
	private List<MenuInfoVO> subMenu = new ArrayList<MenuInfoVO>();
	
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
	public String getMenuLvl() {
		return menuLvl;
	}
	public void setMenuLvl(String menuLvl) {
		this.menuLvl = menuLvl;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public List<MenuInfoVO> getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(List<MenuInfoVO> subMenu) {
		this.subMenu = subMenu;
	}
	public String getInd() {
		return ind;
	}
	public void setInd(String ind) {
		this.ind = ind;
	}
	public String getDefaultYn() {
		return defaultYn;
	}
	public void setDefaultYn(String defaultYn) {
		this.defaultYn = defaultYn;
	}

}
