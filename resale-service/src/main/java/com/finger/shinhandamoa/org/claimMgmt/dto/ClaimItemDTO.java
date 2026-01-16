package com.finger.shinhandamoa.org.claimMgmt.dto;

import java.util.Date;
import java.util.List;

/**
 * @author by puki
 * @date 2018. 4. 13.
 * @desc 최초생성
 */
public class ClaimItemDTO {

    private String rn;
    private String payItemCd;        // 청구항목코드
    private String chaCd;            // 가맹점코드
    private String adjfiRegKey;        // 분할입금계좌
    private String payItemName;        // 청구항목명
    private String payItemSelYN;    // 청구항목필수여부
    private String rcpItemYN;        // 현금영수증대상여부
    private String chaOffNo;        // 사업자번호
    private String ptrItemName;        // 고지서출력대체항목명
    private String ptrItemOrder;    // 출력순서
    private String payItemSt;        // 청구항목상태 등록:ST01, 삭제:ST02
    private Date makeDt;            // 조작일시
    private String maker;            // 조작자
    private Date regDt;            // 등록일시
    private String cmsAcctNo;        // 자동이체정산계좌번호
    private String cmsBnkCd;        // 자동이체정산은행
    private String makeIp;            //
    private String disabledYN;        // 항목사용여부
    private String grpadjName;        // 분할입금명
    private String adjaccyn;

    private List<String> itemList;
    private List<String> idxList;

    private int curPage;

    private String payItemAmt;
    private String ptrItemRemark;

    private String xRow;

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getPayItemCd() {
        return payItemCd;
    }

    public void setPayItemCd(String payItemCd) {
        this.payItemCd = payItemCd;
    }

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getAdjfiRegKey() {
        return adjfiRegKey;
    }

    public void setAdjfiRegKey(String adjfiRegKey) {
        this.adjfiRegKey = adjfiRegKey;
    }

    public String getPayItemName() {
        return payItemName;
    }

    public void setPayItemName(String payItemName) {
        this.payItemName = payItemName;
    }

    public String getPayItemSelYN() {
        return payItemSelYN;
    }

    public void setPayItemSelYN(String payItemSelYN) {
        this.payItemSelYN = payItemSelYN;
    }

    public String getRcpItemYN() {
        return rcpItemYN;
    }

    public void setRcpItemYN(String rcpItemYN) {
        this.rcpItemYN = rcpItemYN;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getPtrItemName() {
        return ptrItemName;
    }

    public void setPtrItemName(String ptrItemName) {
        this.ptrItemName = ptrItemName;
    }

    public String getPtrItemOrder() {
        return ptrItemOrder;
    }

    public void setPtrItemOrder(String ptrItemOrder) {
        this.ptrItemOrder = ptrItemOrder;
    }

    public String getPayItemSt() {
        return payItemSt;
    }

    public void setPayItemSt(String payItemSt) {
        this.payItemSt = payItemSt;
    }

    public Date getMakeDt() {
        return makeDt;
    }

    public void setMakeDt(Date makeDt) {
        this.makeDt = makeDt;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    public String getCmsAcctNo() {
        return cmsAcctNo;
    }

    public void setCmsAcctNo(String cmsAcctNo) {
        this.cmsAcctNo = cmsAcctNo;
    }

    public String getCmsBnkCd() {
        return cmsBnkCd;
    }

    public void setCmsBnkCd(String cmsBnkCd) {
        this.cmsBnkCd = cmsBnkCd;
    }

    public String getMakeIp() {
        return makeIp;
    }

    public void setMakeIp(String makeIp) {
        this.makeIp = makeIp;
    }

    public String getDisabledYN() {
        return disabledYN;
    }

    public void setDisabledYN(String disabledYN) {
        this.disabledYN = disabledYN;
    }

    public String getGrpadjName() {
        return grpadjName;
    }

    public void setGrpadjName(String grpadjName) {
        this.grpadjName = grpadjName;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }

    public List<String> getIdxList() {
        return idxList;
    }

    public void setIdxList(List<String> idxList) {
        this.idxList = idxList;
    }

    public String getAdjaccyn() {
        return adjaccyn;
    }

    public void setAdjaccyn(String adjaccyn) {
        this.adjaccyn = adjaccyn;
    }

    public String getPayItemAmt() {
        return payItemAmt;
    }

    public void setPayItemAmt(String payItemAmt) {
        this.payItemAmt = payItemAmt;
    }

    public String getPtrItemRemark() {
        return ptrItemRemark;
    }

    public void setPtrItemRemark(String ptrItemRemark) {
        this.ptrItemRemark = ptrItemRemark;
    }

    public String getxRow() {
        return xRow;
    }

    public void setxRow(String xRow) {
        this.xRow = xRow;
    }
}
