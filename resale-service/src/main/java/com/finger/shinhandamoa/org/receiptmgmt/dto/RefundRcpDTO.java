package com.finger.shinhandamoa.org.receiptmgmt.dto;

import com.finger.shinhandamoa.data.table.model.ReceiptMaster;

/**
 * 수기수납 - 환불내역 DTO
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 19.
 */
public class RefundRcpDTO extends ReceiptMaster {
	
	/**
	 * rownum
	 */
	private int rn;
	
	/**
	 * 환불일자
	 */
	private String repayday;
	
	/**
	 * 환불PK
	 */
	private String repaymascd;
	
	/**
	 * 수납항목코드
	 */
	private String rcpdetcd;
	
	/**
	 * 청구항목코드
	 */
	private String notidetcd;
	

	/**
	 * 고객핸드폰번호
	 */
	private String cushp;
	
	/**
	 * 현금영수증발행고객정보
	 */
	private String cusoffno;
	
	/**
	 * 항목거래번호
	 */
	private String detkey;
	
	/**
	 * 항목코드
	 */
	private String payitemcd;
	
	/**
	 * 분할입금코드
	 */
	private String adjfiregkey;
	
	/**
	 * 항목명
	 */
	private String payitemname;
	
	/**
	 * 청구항목금액
	 */
	private Long payitemamt;
	
	/**
	 * 현금영수증발행여부
	 */
	private String rcpitemyn;
	
	/**
	 * 사업자번호
	 */
	private String chaoffno;
	
	/**
	 * 수납항목 상태
	 */
	private String rcpdetst;
	
	/**
	 * 현금영수증코드
	 */
	private String cashmascd;
	
	/**
	 * 청구항목 수납금액
	 */
	private Long rcppayitemamt;
	
	/**
	 * 환불금액
	 */
	private Long refundamt;
	
	/**
	 * 수납상태
	 */
	private String notimasst;

	/**
	 * @return the rn
	 */
	public int getRn() {
		return rn;
	}

	/**
	 * @param rn the rn to set
	 */
	public void setRn(int rn) {
		this.rn = rn;
	}

	/**
	 * @return the repayday
	 */
	public String getRepayday() {
		return repayday;
	}

	/**
	 * @param repayday the repayday to set
	 */
	public void setRepayday(String repayday) {
		this.repayday = repayday;
	}

	/**
	 * @return the repaymascd
	 */
	public String getRepaymascd() {
		return repaymascd;
	}

	/**
	 * @param repaymascd the repaymascd to set
	 */
	public void setRepaymascd(String repaymascd) {
		this.repaymascd = repaymascd;
	}

	/**
	 * @return the rcpdetcd
	 */
	public String getRcpdetcd() {
		return rcpdetcd;
	}

	/**
	 * @param rcpdetcd the rcpdetcd to set
	 */
	public void setRcpdetcd(String rcpdetcd) {
		this.rcpdetcd = rcpdetcd;
	}

	/**
	 * @return the notidetcd
	 */
	public String getNotidetcd() {
		return notidetcd;
	}

	/**
	 * @param notidetcd the notidetcd to set
	 */
	public void setNotidetcd(String notidetcd) {
		this.notidetcd = notidetcd;
	}

	/**
	 * @return the cushp
	 */
	public String getCushp() {
		return cushp;
	}

	/**
	 * @param cushp the cushp to set
	 */
	public void setCushp(String cushp) {
		this.cushp = cushp;
	}

	/**
	 * @return the cusoffno
	 */
	public String getCusoffno() {
		return cusoffno;
	}

	/**
	 * @param cusoffno the cusoffno to set
	 */
	public void setCusoffno(String cusoffno) {
		this.cusoffno = cusoffno;
	}

	/**
	 * @return the detkey
	 */
	public String getDetkey() {
		return detkey;
	}

	/**
	 * @param detkey the detkey to set
	 */
	public void setDetkey(String detkey) {
		this.detkey = detkey;
	}

	/**
	 * @return the payitemcd
	 */
	public String getPayitemcd() {
		return payitemcd;
	}

	/**
	 * @param payitemcd the payitemcd to set
	 */
	public void setPayitemcd(String payitemcd) {
		this.payitemcd = payitemcd;
	}

	/**
	 * @return the adjfiregkey
	 */
	public String getAdjfiregkey() {
		return adjfiregkey;
	}

	/**
	 * @param adjfiregkey the adjfiregkey to set
	 */
	public void setAdjfiregkey(String adjfiregkey) {
		this.adjfiregkey = adjfiregkey;
	}

	/**
	 * @return the payitemname
	 */
	public String getPayitemname() {
		return payitemname;
	}

	/**
	 * @param payitemname the payitemname to set
	 */
	public void setPayitemname(String payitemname) {
		this.payitemname = payitemname;
	}

	/**
	 * @return the payitemamt
	 */
	public Long getPayitemamt() {
		return payitemamt;
	}

	/**
	 * @param payitemamt the payitemamt to set
	 */
	public void setPayitemamt(Long payitemamt) {
		this.payitemamt = payitemamt;
	}

	/**
	 * @return the rcpitemyn
	 */
	public String getRcpitemyn() {
		return rcpitemyn;
	}

	/**
	 * @param rcpitemyn the rcpitemyn to set
	 */
	public void setRcpitemyn(String rcpitemyn) {
		this.rcpitemyn = rcpitemyn;
	}

	/**
	 * @return the chaoffno
	 */
	public String getChaoffno() {
		return chaoffno;
	}

	/**
	 * @param chaoffno the chaoffno to set
	 */
	public void setChaoffno(String chaoffno) {
		this.chaoffno = chaoffno;
	}

	/**
	 * @return the rcpdetst
	 */
	public String getRcpdetst() {
		return rcpdetst;
	}

	/**
	 * @param rcpdetst the rcpdetst to set
	 */
	public void setRcpdetst(String rcpdetst) {
		this.rcpdetst = rcpdetst;
	}

	/**
	 * @return the cashmascd
	 */
	public String getCashmascd() {
		return cashmascd;
	}

	/**
	 * @param cashmascd the cashmascd to set
	 */
	public void setCashmascd(String cashmascd) {
		this.cashmascd = cashmascd;
	}

	/**
	 * @return the rcppayitemamt
	 */
	public Long getRcppayitemamt() {
		return rcppayitemamt;
	}

	/**
	 * @param rcppayitemamt the rcppayitemamt to set
	 */
	public void setRcppayitemamt(Long rcppayitemamt) {
		this.rcppayitemamt = rcppayitemamt;
	}

	/**
	 * @return the refundamt
	 */
	public Long getRefundamt() {
		return refundamt;
	}

	/**
	 * @param refundamt the refundamt to set
	 */
	public void setRefundamt(Long refundamt) {
		this.refundamt = refundamt;
	}

	/**
	 * @return the notimasst
	 */
	public String getNotimasst() {
		return notimasst;
	}

	/**
	 * @param notimasst the notimasst to set
	 */
	public void setNotimasst(String notimasst) {
		this.notimasst = notimasst;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adjfiregkey == null) ? 0 : adjfiregkey.hashCode());
		result = prime * result + ((cashmascd == null) ? 0 : cashmascd.hashCode());
		result = prime * result + ((chaoffno == null) ? 0 : chaoffno.hashCode());
		result = prime * result + ((cushp == null) ? 0 : cushp.hashCode());
		result = prime * result + ((cusoffno == null) ? 0 : cusoffno.hashCode());
		result = prime * result + ((detkey == null) ? 0 : detkey.hashCode());
		result = prime * result + ((notidetcd == null) ? 0 : notidetcd.hashCode());
		result = prime * result + ((payitemamt == null) ? 0 : payitemamt.hashCode());
		result = prime * result + ((payitemcd == null) ? 0 : payitemcd.hashCode());
		result = prime * result + ((payitemname == null) ? 0 : payitemname.hashCode());
		result = prime * result + ((rcpdetcd == null) ? 0 : rcpdetcd.hashCode());
		result = prime * result + ((rcpdetst == null) ? 0 : rcpdetst.hashCode());
		result = prime * result + ((rcpitemyn == null) ? 0 : rcpitemyn.hashCode());
		result = prime * result + ((rcppayitemamt == null) ? 0 : rcppayitemamt.hashCode());
		result = prime * result + ((refundamt == null) ? 0 : refundamt.hashCode());
		result = prime * result + ((repayday == null) ? 0 : repayday.hashCode());
		result = prime * result + ((repaymascd == null) ? 0 : repaymascd.hashCode());
		result = prime * result + rn;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof RefundRcpDTO))
			return false;
		RefundRcpDTO other = (RefundRcpDTO) obj;
		if (adjfiregkey == null) {
			if (other.adjfiregkey != null)
				return false;
		} else if (!adjfiregkey.equals(other.adjfiregkey))
			return false;
		if (cashmascd == null) {
			if (other.cashmascd != null)
				return false;
		} else if (!cashmascd.equals(other.cashmascd))
			return false;
		if (chaoffno == null) {
			if (other.chaoffno != null)
				return false;
		} else if (!chaoffno.equals(other.chaoffno))
			return false;
		if (cushp == null) {
			if (other.cushp != null)
				return false;
		} else if (!cushp.equals(other.cushp))
			return false;
		if (cusoffno == null) {
			if (other.cusoffno != null)
				return false;
		} else if (!cusoffno.equals(other.cusoffno))
			return false;
		if (detkey == null) {
			if (other.detkey != null)
				return false;
		} else if (!detkey.equals(other.detkey))
			return false;
		if (notidetcd == null) {
			if (other.notidetcd != null)
				return false;
		} else if (!notidetcd.equals(other.notidetcd))
			return false;
		if (payitemamt == null) {
			if (other.payitemamt != null)
				return false;
		} else if (!payitemamt.equals(other.payitemamt))
			return false;
		if (payitemcd == null) {
			if (other.payitemcd != null)
				return false;
		} else if (!payitemcd.equals(other.payitemcd))
			return false;
		if (payitemname == null) {
			if (other.payitemname != null)
				return false;
		} else if (!payitemname.equals(other.payitemname))
			return false;
		if (rcpdetcd == null) {
			if (other.rcpdetcd != null)
				return false;
		} else if (!rcpdetcd.equals(other.rcpdetcd))
			return false;
		if (rcpdetst == null) {
			if (other.rcpdetst != null)
				return false;
		} else if (!rcpdetst.equals(other.rcpdetst))
			return false;
		if (rcpitemyn == null) {
			if (other.rcpitemyn != null)
				return false;
		} else if (!rcpitemyn.equals(other.rcpitemyn))
			return false;
		if (rcppayitemamt == null) {
			if (other.rcppayitemamt != null)
				return false;
		} else if (!rcppayitemamt.equals(other.rcppayitemamt))
			return false;
		if (refundamt == null) {
			if (other.refundamt != null)
				return false;
		} else if (!refundamt.equals(other.refundamt))
			return false;
		if (repayday == null) {
			if (other.repayday != null)
				return false;
		} else if (!repayday.equals(other.repayday))
			return false;
		if (repaymascd == null) {
			if (other.repaymascd != null)
				return false;
		} else if (!repaymascd.equals(other.repaymascd))
			return false;
		if (notimasst == null) {
			if (other.notimasst != null)
				return false;
		} else if (!notimasst.equals(other.notimasst))
			return false;
		if (rn != other.rn)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RefundRcpDTO [rn=");
		builder.append(rn);
		builder.append(", repayday=");
		builder.append(repayday);
		builder.append(", repaymascd=");
		builder.append(repaymascd);
		builder.append(", rcpdetcd=");
		builder.append(rcpdetcd);
		builder.append(", notidetcd=");
		builder.append(notidetcd);
		builder.append(", cushp=");
		builder.append(cushp);
		builder.append(", cusoffno=");
		builder.append(cusoffno);
		builder.append(", detkey=");
		builder.append(detkey);
		builder.append(", payitemcd=");
		builder.append(payitemcd);
		builder.append(", adjfiregkey=");
		builder.append(adjfiregkey);
		builder.append(", payitemname=");
		builder.append(payitemname);
		builder.append(", payitemamt=");
		builder.append(payitemamt);
		builder.append(", rcpitemyn=");
		builder.append(rcpitemyn);
		builder.append(", chaoffno=");
		builder.append(chaoffno);
		builder.append(", rcpdetst=");
		builder.append(rcpdetst);
		builder.append(", cashmascd=");
		builder.append(cashmascd);
		builder.append(", rcppayitemamt=");
		builder.append(rcppayitemamt);
		builder.append(", refundamt=");
		builder.append(refundamt);
		builder.append(", notimasst=");
		builder.append(notimasst);
		builder.append("]");
		return builder.toString();
	}
}
