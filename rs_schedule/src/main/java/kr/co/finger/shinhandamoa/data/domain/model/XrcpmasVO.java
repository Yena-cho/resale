package kr.co.finger.shinhandamoa.data.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xrcpmas;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class XrcpmasVO implements Serializable {

    private String rcpmascd;
    private String notimascd;
    private String svecd;
    private String chacd;
    private String vano;
    private String maskey;
    private String masmonth;
    private String masday;
    private String cuskey;
    private String cusgubn1;
    private String cusgubn2;
    private String cusgubn3;
    private String cusgubn4;
    private String cusname;
    private String cushp;
    private String cusmail;
    private String smsyn;
    private String mailyn;
    private String cusoffno;
    private String payday;
    private String paytime;
    private String bnkmsgno;
    private String ficd;
    private String bnkcd;
    private String bchcd;
    private String rcpusrname;
    private Long rcpamt;
    private String occgubn;
    private String mdgubn;
    private String sucday;
    private String suctime;
    private String canday;
    private String cantime;
    private String trnday;
    private String trntime;
    private int trncnt;
    private String trnst;
    private String packetno;
    private String rcpmasst;
    private Date makedt;
    private String maker;
    private Date regdt;
    private String chatrty;
    private String cashday;
    private String displayyn;
    private String dealseqno;

    public static XrcpmasVO convert(Xrcpmas xrcpmas) {
        XrcpmasVO xrcpmasVO = new XrcpmasVO();
        xrcpmasVO.setRcpmascd(xrcpmas.getRcpmascd());
        xrcpmasVO.setNotimascd(xrcpmas.getNotimascd());
        xrcpmasVO.setSvecd(xrcpmas.getSvecd());
        xrcpmasVO.setChacd(xrcpmas.getChacd());
        xrcpmasVO.setVano(xrcpmas.getVano());
        xrcpmasVO.setMaskey(xrcpmas.getMaskey());
        xrcpmasVO.setMasmonth(xrcpmas.getMasmonth());
        xrcpmasVO.setMasday(xrcpmas.getMasday());
        xrcpmasVO.setCuskey(xrcpmas.getCuskey());
        xrcpmasVO.setCusgubn1(xrcpmas.getCusgubn1());
        xrcpmasVO.setCusgubn2(xrcpmas.getCusgubn2());
        xrcpmasVO.setCusgubn3(xrcpmas.getCusgubn3());
        xrcpmasVO.setCusgubn4(xrcpmas.getCusgubn4());
        xrcpmasVO.setCusname(xrcpmas.getCusname());
        xrcpmasVO.setCushp(xrcpmas.getCushp());
        xrcpmasVO.setCusmail(xrcpmas.getCusmail());
        xrcpmasVO.setSmsyn(xrcpmas.getSmsyn());
        xrcpmasVO.setMailyn(xrcpmas.getMailyn());
        xrcpmasVO.setCusoffno(xrcpmas.getCusoffno());
        xrcpmasVO.setPayday(xrcpmas.getPayday());
        xrcpmasVO.setPaytime(xrcpmas.getPaytime());
        xrcpmasVO.setBnkmsgno(xrcpmas.getBnkmsgno());
        xrcpmasVO.setFicd(xrcpmas.getFicd());
        xrcpmasVO.setBnkcd(xrcpmas.getBnkcd());
        xrcpmasVO.setBchcd(xrcpmas.getBchcd());
        xrcpmasVO.setRcpusrname(xrcpmas.getRcpusrname());
        xrcpmasVO.setRcpamt(xrcpmas.getRcpamt());
        xrcpmasVO.setOccgubn(xrcpmas.getOccgubn());
        xrcpmasVO.setMdgubn(xrcpmas.getMdgubn());
        xrcpmasVO.setSucday(xrcpmas.getSucday());
        xrcpmasVO.setSuctime(xrcpmas.getSuctime());
        xrcpmasVO.setCanday(xrcpmas.getCanday());
        xrcpmasVO.setCantime(xrcpmas.getCantime());
        xrcpmasVO.setTrnday(xrcpmas.getTrnday());
        xrcpmasVO.setTrntime(xrcpmas.getTrntime());
        xrcpmasVO.setTrncnt(xrcpmas.getTrncnt());
        xrcpmasVO.setTrnst(xrcpmas.getTrnst());
        xrcpmasVO.setPacketno(xrcpmas.getPacketno());
        xrcpmasVO.setRcpmasst(xrcpmas.getRcpmasst());
        xrcpmasVO.setMakedt(xrcpmas.getMakedt());
        xrcpmasVO.setMaker(xrcpmas.getMaker());
        xrcpmasVO.setRegdt(xrcpmas.getRegdt());
        xrcpmasVO.setChatrty(xrcpmas.getChatrty());
        xrcpmasVO.setCashday(xrcpmas.getCashday());
        xrcpmasVO.setDisplayyn(xrcpmas.getDisplayyn());
        xrcpmasVO.setDealseqno(xrcpmas.getDealseqno());

        return xrcpmasVO;
    }

    public XrcpdetVO toDet() {
        XrcpdetVO xrcpdetVO = new XrcpdetVO();
        xrcpdetVO.setPayitemamt(this.getRcpamt());


        return xrcpdetVO;
    }
}