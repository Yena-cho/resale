package kr.co.finger.shinhandamoa.data.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xrcpdet;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class XrcpdetVO implements Serializable {
    private String rcpdetcd;
    private String rcpmascd;
    private String notidetcd;
    private String detkey;
    private String payitemcd;
    private String adjfiregkey;
    private String payitemname;
    private Long payitemamt;
    private String rcpitemyn;
    private String chaoffno;
    private String cusoffno;
    private String ptritemname;
    private String ptritemremark;
    private String ptritemorder;
    private String rcpdetst;
    private Date makedt;
    private String maker;
    private Date regdt;
    private String chatrty;
    private String cashmascd;


    public XrcpdetVO() {

    }

    public XrcpdetVO(XrcpmasVO xrcpmasVO) {

    }

    public static XrcpdetVO convert(Xrcpdet xrcpdet) {
        XrcpdetVO xrcpdetVO = new XrcpdetVO();
        xrcpdetVO.setRcpdetcd(xrcpdet.getRcpdetcd());
        xrcpdetVO.setRcpmascd(xrcpdet.getRcpmascd());
        xrcpdetVO.setNotidetcd(xrcpdet.getNotidetcd());
        xrcpdetVO.setDetkey(xrcpdet.getDetkey());
        xrcpdetVO.setPayitemcd(xrcpdet.getPayitemcd());
        xrcpdetVO.setAdjfiregkey(xrcpdet.getAdjfiregkey());
        xrcpdetVO.setPayitemname(xrcpdet.getPayitemname());
        xrcpdetVO.setPayitemamt(xrcpdet.getPayitemamt());
        xrcpdetVO.setRcpitemyn(xrcpdet.getRcpitemyn());
        xrcpdetVO.setChaoffno(xrcpdet.getChaoffno());
        xrcpdetVO.setCusoffno(xrcpdet.getCusoffno());
        xrcpdetVO.setPtritemname(xrcpdet.getPtritemname());
        xrcpdetVO.setPtritemremark(xrcpdet.getPtritemremark());
        xrcpdetVO.setPtritemorder(xrcpdet.getPtritemorder());
        xrcpdetVO.setRcpdetst(xrcpdet.getRcpdetst());
        xrcpdetVO.setMakedt(xrcpdet.getMakedt());
        xrcpdetVO.setMaker(xrcpdet.getMaker());
        xrcpdetVO.setRegdt(xrcpdet.getRegdt());
        xrcpdetVO.setChatrty(xrcpdet.getChatrty());
        xrcpdetVO.setCashmascd(xrcpdet.getCashmascd());



        return xrcpdetVO;
    }
}