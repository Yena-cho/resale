package kr.co.finger.shinhandamoa.data.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.VaSettleMas;
import kr.co.finger.shinhandamoa.data.table.model.VaSettleMasKey;
import kr.co.finger.shinhandamoa.data.table.model.Xadjgroup;
import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString
public class VaSettleMasVO extends VaSettleMasKey implements Serializable {

    private String fgcd;
    private String chaname;
    private Date settleDate;
    private String settleStatus;
    private Long settleAmount;
    private String settleAccno;
    private String adjfiregkey;
    private String fingerFeePayty;
    private Date regDate;
    private String regName;
    private Date modDate;
    private String modName;
    private String settleBankcd;

    public static VaSettleMas create(Xchalist xchalist, Xadjgroup xadjgroup, long rcpamt, String settleDateString) throws ParseException {
        VaSettleMas vaSettleMas = new VaSettleMas();
        vaSettleMas.setRcpDate(DateUtils.parseDate(settleDateString, "yyyyMMdd"));
        vaSettleMas.setChacd(xchalist.getChacd());
        vaSettleMas.setFgcd(xchalist.getFgcd());
        vaSettleMas.setChaname(xchalist.getChaname());
//        vaSettleMas.setSettleDate();
        // W, S, F
        vaSettleMas.setSettleStatus("W");
        vaSettleMas.setSettleAmount(rcpamt);
        vaSettleMas.setSettleAccno(xadjgroup.getRealAccno());
        vaSettleMas.setAdjfiregkey(xadjgroup.getAdjfiregkey());
        vaSettleMas.setFingerFeePayty(xchalist.getFingerFeePayty());
        vaSettleMas.setRegDate(new Date());
        vaSettleMas.setRegName("scheduler");
        vaSettleMas.setModDate(new Date());
        vaSettleMas.setModName("scheduler");
        vaSettleMas.setSettleBankcd(xadjgroup.getBankcd());
        vaSettleMas.setSettleSeq(xchalist.getChacd()+""+settleDateString+""+xadjgroup.getAdjfiregkey());

        return vaSettleMas;
    }
}