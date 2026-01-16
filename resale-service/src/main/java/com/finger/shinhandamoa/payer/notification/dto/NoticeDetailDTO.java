/**
 * 
 */
package com.finger.shinhandamoa.payer.notification.dto;

import com.finger.shinhandamoa.data.table.model.NoticeDetails;

/**
 * 청구항목 상세 DTO with 수납정보
 * @author jhjeong@finger.co.kr
 * @modified 2018. 10. 15.
 */
public class NoticeDetailDTO extends NoticeDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8510068144304709462L;

	private String rcpdetcd;
	private String rcpmascd;
	private String rcpdetst;
	private long rcpamt;
	
	public NoticeDetailDTO(NoticeDetails details) {
		super.setAdjfiregkey(details.getAdjfiregkey());
		super.setBatchseq(details.getBatchseq());
		super.setChaoffno(details.getChaoffno());
		super.setChatrty(details.getChatrty());
		super.setCusoffno(details.getCusoffno());
		super.setDetkey(details.getDetkey());
		super.setMakedt(details.getMakedt());
		super.setMaker(details.getMaker());
		super.setNotiCanYn(details.getNotiCanYn());
		super.setNotidetcd(details.getNotidetcd());
		super.setNotidetst(details.getNotidetst());
		super.setNotimascd(details.getNotimascd());
		super.setPayitemamt(details.getPayitemamt());
		super.setPayitemcd(details.getPayitemcd());
		super.setPayitemname(details.getPayitemname());
		super.setPayitemselyn(details.getPayitemselyn());
		super.setPtritemname(details.getPtritemname());
		super.setPtritemorder(details.getPtritemorder());
		super.setPtritemremark(details.getPtritemremark());
		super.setRcpitemyn(details.getRcpitemyn());
		super.setRegdt(details.getRegdt());
		super.setPickrcpyn(details.getPickrcpyn());
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
	 * @return the rcpmascd
	 */
	public String getRcpmascd() {
		return rcpmascd;
	}
	/**
	 * @param rcpmascd the rcpmascd to set
	 */
	public void setRcpmascd(String rcpmascd) {
		this.rcpmascd = rcpmascd;
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
	 * @return the rcpamt
	 */
	public long getRcpamt() {
		return rcpamt;
	}
	/**
	 * @param rcpamt the rcpamt to set
	 */
	public void setRcpamt(long rcpamt) {
		this.rcpamt = rcpamt;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (rcpamt ^ (rcpamt >>> 32));
		result = prime * result + ((rcpdetcd == null) ? 0 : rcpdetcd.hashCode());
		result = prime * result + ((rcpdetst == null) ? 0 : rcpdetst.hashCode());
		result = prime * result + ((rcpmascd == null) ? 0 : rcpmascd.hashCode());
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
		if (!(obj instanceof NoticeDetailDTO))
			return false;
		NoticeDetailDTO other = (NoticeDetailDTO) obj;
		if (rcpamt != other.rcpamt)
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
		if (rcpmascd == null) {
			if (other.rcpmascd != null)
				return false;
		} else if (!rcpmascd.equals(other.rcpmascd))
			return false;
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("[");
		builder.append("rcpdetcd=");
		builder.append(rcpdetcd);
		builder.append(", rcpmascd=");
		builder.append(rcpmascd);
		builder.append(", rcpdetst=");
		builder.append(rcpdetst);
		builder.append(", rcpamt=");
		builder.append(rcpamt);
		builder.append("]");
		return builder.toString();
	}
	
	
}
