package kr.co.finger.damoa.model.rcp;

import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 입금(이체) 처리시에 청구 상세 정보를 가지는 객체...
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class NotiDet implements Serializable,Cloneable {
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
    private long paid;
    private String notmascd;

    public NotiDet(String adjfiregkey, String payitemamt) {
        this.adjfiregkey = adjfiregkey;
        this.payitemamt = payitemamt;
    }
    public NotiDet(String adjfiregkey, String payitemamt,String notmascd,String notidetcd) {
        this.adjfiregkey = adjfiregkey;
        this.payitemamt = payitemamt;
        this.notmascd = notmascd;
        this.notidetcd = notidetcd;
    }

    public NotiDet() {
    }

    public String getNotmascd() {
        return notmascd;
    }

    public void setNotmascd(String notmascd) {
        this.notmascd = notmascd;
    }

    public long getPaid() {
        return paid;
    }

    public void setPaid(long paid) {
        this.paid = paid;
    }

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
        setNotmascd(Maps.getValue(map, "NOTIMASCD"));
    }

    public void simpleSetupFrom(Map<String, Object> map) {
        setAdjfiregkey(Maps.getValue(map,"ADJFIREGKEY"));
        setPayitemamt(Maps.getValue(map,"PAYITEMAMT"));
        setNotmascd(Maps.getValue(map, "NOTIMASCD"));
        setNotidetcd(Maps.getValue(map, "NOTIDETCD"));

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
        param.put("PAYITEMAMT", amount);  //항목금액
        param.put("RCPITEMYN", rcpitemyn);  //현금영수증발행여부
        param.put("CHAOFFNO", chaoffno);  //사업자번호
        param.put("CUSOFFNO", cusoffno);  //현금영수증인증번호
        param.put("PTRITEMNAME",ptritemname );  //출력명
        param.put("PTRITEMREMARK", ptritemremark);  //비고
        param.put("PTRITEMORDER", ptritemorder);  //출력순서
        param.put("RCPDETST", findState(getSt()));  //상태
        param.put("MAKEDT", date);  //수정일
        param.put("MAKER","DamoaShinhnaAgent" );  //수정자
        param.put("REGDT",date );  //등록일
        param.put("CHATRTY", chatrty);  //기관형태

        return param;
    }


    private String findState(String st) {
        return "PA02".equals(st) ? st : "PA03";
    }

    /**
     * 이미 수납된 데이터를 처리함.
     * @param rcpInfos
     */
    public void aggregate(Map<String, String> rcpInfos) {
        String key = Damoas.makeKey(notmascd, notidetcd);
        if (rcpInfos.containsKey(key)) {
            paid = Damoas.toLong(rcpInfos.get(key));
        } else {
            paid = 0;
        }
    }

    public long getAmountToPay() {
        return Damoas.toLong(getPayitemamt()) - paid;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("payitemcd", payitemcd)
                .append("amount", amount)
                .append("st", st)
                .toString();
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
