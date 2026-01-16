package com.finger.shinhandamoa.excel.handler;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Column implements Serializable {

	private static final long serialVersionUID = 3847433327921595513L;

	public int idx;
	public String code;
	public String value;
	
	public Column() {
		this.idx = -1;
	}
	
	public Column(String[] arr) {
		if (arr != null && arr.length == 2) {
			this.code = arr[0].toUpperCase();
			this.value = arr[1].toUpperCase();
		} else {
			this.code = StringUtils.EMPTY;
			this.value = StringUtils.EMPTY;
		}
	}
	
	public Column(String str) {
		this.code = str.toUpperCase();
		this.value = str.toUpperCase();
	}
	
	public boolean equals() {
		return (StringUtils.equalsIgnoreCase(code, value));
	}
	
	public boolean isPayment() {
		return !(StringUtils.equalsIgnoreCase(code, value));
	}

}
