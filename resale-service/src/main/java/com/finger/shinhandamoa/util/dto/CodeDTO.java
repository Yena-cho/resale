package com.finger.shinhandamoa.util.dto;

/**
 * @author  by puki
 * @date    2018. 4. 13.
 * @desc    코드 DTO
 * @version  
 * 
 */
public class CodeDTO {

	private String code;		// 공통코드
	private String codeName;	// 공통코드명
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	@Override
	public String toString() {
		return "CodeDTO [code=" + code + ", codeName=" + codeName + "]";
	}
	
	
}
