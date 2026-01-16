package com.finger.shinhandamoa.org.receiptmgmt.dto;

import java.util.Date;
import java.util.Map;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;

public class NotiDTO {

	private String notidetcd;
    private String adjfiregkey;
    private String payitemamt;
    private String payitemcd;
    private String payitemname;
    private String rcpitemyn;
    private String detkey;
    private String chaoffno;
    private String ptritemname;
    private String ptritemremark;
    private String ptritemorder;
    private long amount;
    private String st;
    private String vano;
    private String notimascd;
    private String rcpmascd;
    private String chacd;
    private int rcpAmt;
    private String noTaxYn;
    private String amtChkTy;
    private String rcpReqTy;
    private String chatrty;
    private String cusOffNo;
    private String cusType;
    private String confirm;
    
    public String getNotidetcd() {
        return notidetcd;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setNotidetcd(String notidetcd) {
        this.notidetcd = notidetcd;
    }

    public String getAdjfiregkey() {
        return adjfiregkey;
    }

    public void setAdjfiregkey(String adjfiregkey) {
        this.adjfiregkey = adjfiregkey;
    }

    public String getPayitemamt() {
        return payitemamt;
    }

    public void setPayitemamt(String payitemamt) {
        this.payitemamt = payitemamt;
    }

    public String getPayitemcd() {
        return payitemcd;
    }

    public void setPayitemcd(String payitemcd) {
        this.payitemcd = payitemcd;
    }

    public String getPayitemname() {
        return payitemname;
    }

    public void setPayitemname(String payitemname) {
        this.payitemname = payitemname;
    }

    public String getRcpitemyn() {
        return rcpitemyn;
    }

    public void setRcpitemyn(String rcpitemyn) {
        this.rcpitemyn = rcpitemyn;
    }

    public String getDetkey() {
        return detkey;
    }

    public void setDetkey(String detkey) {
        this.detkey = detkey;
    }

    public String getChaoffno() {
        return chaoffno;
    }

    public void setChaoffno(String chaoffno) {
        this.chaoffno = chaoffno;
    }

    public String getPtritemname() {
        return ptritemname;
    }

    public void setPtritemname(String ptritemname) {
        this.ptritemname = ptritemname;
    }

    public String getPtritemremark() {
        return ptritemremark;
    }

    public void setPtritemremark(String ptritemremark) {
        this.ptritemremark = ptritemremark;
    }

    public String getPtritemorder() {
        return ptritemorder;
    }

    public void setPtritemorder(String ptritemorder) {
        this.ptritemorder = ptritemorder;
    }

    public void setupFrom(Map<String, Object> map) {
        setNotidetcd(Maps.getValue(map,"NOTIDETCD"));
        setAdjfiregkey(Maps.getValue(map,"ADJFIREGKEY"));
        setPayitemamt(Maps.getValue(map,"PAYITEMAMT"));
        setPayitemcd(Maps.getValue(map,"PAYITEMCD"));
        setPayitemname(Maps.getValue(map,"PAYITEMNAME"));
        setRcpitemyn(Maps.getValue(map,"RCPITEMYN"));
        setDetkey(Maps.getValue(map,"DETKEY"));
        setChaoffno(Maps.getValue(map,"CHAOFFNO"));
        setPtritemname(Maps.getValue(map,"PTRITEMNAME"));
        setPtritemremark(Maps.getValue(map,"PTRITEMREMARK"));
        setPtritemorder(Maps.getValue(map,"PTRITEMORDER"));
    }

    public void simpleSetupFrom(Map<String, Object> map) {
        setAdjfiregkey(Maps.getValue(map,"ADJFIREGKEY"));
        setPayitemamt(Maps.getValue(map,"PAYITEMAMT"));

    }

    public Map<String, Object> toInsertRcpDetailMap(String rcpMasCd, Date now,String cusoffno,String chatrty) {
        Map<String, Object> param = Maps.hashmap();
        String date = DateUtils.toDTType(now);
        param.put("RCPMASCD",rcpMasCd);  //수납코드
        param.put("NOTIDETCD", notidetcd);  //청구항목코드
        param.put("DETKEY", detkey);  //
        param.put("PAYITEMCD",payitemcd );  //항목코드
        param.put("ADJFIREGKEY", adjfiregkey);  //분할입금코드
        param.put("PAYITEMNAME", payitemname);  //항목명
        param.put("PAYITEMAMT", payitemamt);  //항목금액
        param.put("RCPITEMYN", rcpitemyn);  //현금영수증발행여부
        param.put("CHAOFFNO", chaoffno);  //사업자번호
        param.put("CUSOFFNO", cusoffno);  //현금영수증인증번호
        param.put("PTRITEMNAME",ptritemname );  //출력명
        param.put("PTRITEMREMARK", ptritemremark);  //비고
        param.put("PTRITEMORDER", ptritemorder);  //출력순서
        param.put("RCPDETST", getSt());  //상태
        param.put("MAKEDT", date);  //수정일
        param.put("MAKER","DamoaShinhnaAgent" );  //수정자
        param.put("REGDT",date );  //등록일
        param.put("CHATRTY", chatrty);  //기관형태

        return param;
    }

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public String getNotimascd() {
		return notimascd;
	}

	public void setNotimascd(String notimascd) {
		this.notimascd = notimascd;
	}

	public String getRcpmascd() {
		return rcpmascd;
	}

	public void setRcpmascd(String rcpmascd) {
		this.rcpmascd = rcpmascd;
	}

	public String getChacd() {
		return chacd;
	}

	public void setChacd(String chacd) {
		this.chacd = chacd;
	}

	public int getRcpAmt() {
		return rcpAmt;
	}

	public void setRcpAmt(int rcpAmt) {
		this.rcpAmt = rcpAmt;
	}

	public String getNoTaxYn() {
		return noTaxYn;
	}

	public void setNoTaxYn(String noTaxYn) {
		this.noTaxYn = noTaxYn;
	}

	public String getAmtChkTy() {
		return amtChkTy;
	}

	public void setAmtChkTy(String amtChkTy) {
		this.amtChkTy = amtChkTy;
	}

	public String getRcpReqTy() {
		return rcpReqTy;
	}

	public void setRcpReqTy(String rcpReqTy) {
		this.rcpReqTy = rcpReqTy;
	}

	public String getChatrty() {
		return chatrty;
	}

	public void setChatrty(String chatrty) {
		this.chatrty = chatrty;
	}

	public String getCusOffNo() {
		return cusOffNo;
	}

	public void setCusOffNo(String cusOffNo) {
		this.cusOffNo = cusOffNo;
	}

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
}
