package kr.co.finger.shinhandamoa.data.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.VaSettleDet;
import kr.co.finger.shinhandamoa.data.table.model.VaSettleMas;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class VaSettleDetVO implements Serializable {

    private Date rcpDate;
    private String fgcd;
    private String chacd;
    private String chaname;
    private Date settleDate;
    private String settleStatus;
    private Long settleAmount;
    private String settleAccno;
    private String adjfiregkey;
    private String fingerFeePayty;
    private Date regDate;
    private String regName;
    private String resultMsg;
    private String settleBankcd;

    public static VaSettleDet create(VaSettleMas vaSettleMas) {
        VaSettleDet vaSettleDet = new VaSettleDet();

        vaSettleDet.setRcpDate(vaSettleMas.getRcpDate());
        vaSettleDet.setChacd(vaSettleMas.getChacd());
        vaSettleDet.setFgcd(vaSettleMas.getFgcd());
        vaSettleDet.setChaname(vaSettleMas.getChaname());
        vaSettleMas.setSettleDate(vaSettleMas.getSettleDate());
        vaSettleDet.setSettleStatus(vaSettleMas.getSettleStatus());
        vaSettleDet.setSettleAmount(vaSettleMas.getSettleAmount());
        vaSettleDet.setSettleAccno(vaSettleMas.getSettleAccno());
        vaSettleDet.setAdjfiregkey(vaSettleMas.getAdjfiregkey());
        vaSettleDet.setFingerFeePayty(vaSettleMas.getFingerFeePayty());
        vaSettleDet.setRegDate(new Date());
        vaSettleDet.setRegName("scheduler");
        vaSettleDet.setSettleBankcd(vaSettleMas.getSettleBankcd());
        vaSettleDet.setResultMsg("가상계좌 정산 등록");
        vaSettleDet.setSettleSeq(vaSettleMas.getSettleSeq());

        return vaSettleDet;
    }
}