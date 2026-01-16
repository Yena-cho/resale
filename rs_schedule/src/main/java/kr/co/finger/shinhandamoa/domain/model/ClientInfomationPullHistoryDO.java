package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.damoa.scheduler.ThreadSession;
import kr.co.finger.damoa.scheduler.ThreadSessionHolder;
import kr.co.finger.shinhandamoa.data.table.model.DamoaClientInfoPullHist;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 기관 정보 연동 이력 DO
 *
 * @author wisehouse@finger.co.kr
 */
public class ClientInfomationPullHistoryDO {
    /**
     * 수신 완료
     */
    public static final String STATUS_CD_PULL = "C10001";
    /**
     * 작업 대기
     */
    public static final String STATUS_CD_STANDBY = "C10002";
    /**
     * 작업 중
     */
    public static final String STATUS_CD_WIP = "C10003";
    /**
     * 작업 성공
     */
    public static final String STATUS_CD_SUCCESS = "C10004";
    /**
     * 작업 실패
     */
    public static final String STATUS_CD_FAIL = "C10005";
    /**
     * 신규
     */
    public static final String TYPE_CD_CREATE = "C20001";
    /**
     * 변경
     */
    public static final String TYPE_CD_MODIFY = "C20002";
    /**
     * 해지
     */
    public static final String TYPE_CD_TERMINATE = "C20003";
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInfomationPullHistoryDO.class);
    private DamoaClientInfoPullHist damoaClientInfoPullHist;

    private ClientInfomationPullHistoryDO() {
        this(new DamoaClientInfoPullHist());
    }

    public ClientInfomationPullHistoryDO(DamoaClientInfoPullHist damoaClientInfoPullHist) {
        this.damoaClientInfoPullHist = damoaClientInfoPullHist;
    }

    public static ClientInfomationPullHistoryDO newInstance(ClientInfomationPullHistoryExample example, ClientDO clientDO) {
        final String user = ThreadSessionHolder.getInstance().getSession("damoa-domain").getAttribute(ThreadSession.SESSION_ATTRIBUTE_KEY_OWNER);

        final String typeCd;
        if (clientDO == null) {
            typeCd = TYPE_CD_CREATE;
        } else if (StringUtils.equalsAny(example.getClientStatus(), "1", "2")) {
            typeCd = TYPE_CD_MODIFY;
        } else {
            typeCd = TYPE_CD_TERMINATE;
        }

        final String statusCd;
        if (StringUtils.equals(typeCd, TYPE_CD_CREATE)) {
            statusCd = STATUS_CD_STANDBY;
        } else {
            statusCd = STATUS_CD_PULL;
        }

        final ClientInfomationPullHistoryDO instance = new ClientInfomationPullHistoryDO();
        instance.setPullDt(example.getPullDt());
        instance.setClientId(example.getClientId());
        instance.setClientName(example.getClientName());
        instance.setClientDeleteYn(example.getClientDeleteYn());
        instance.setRelayClientId(example.getRelayClientId());
        instance.setClientCreateDt(example.getClientCreateDt());
        instance.setClientIdentityNo(example.getClientIdentityNo());
        instance.setClientStatus(example.getClientStatus());
        instance.setAgency1Code(example.getAgencyCode01());
        instance.setAgency1Name(example.getAgencyName01());
        instance.setAgency1Status(example.getAgencyStatus01());
        instance.setAgency2Code(example.getAgencyCode02());
        instance.setAgency2Name(example.getAgencyName02());
        instance.setAgency2Status(example.getAgencyStatus02());
        instance.setAgency3Code(example.getAgencyCode03());
        instance.setAgency3Name(example.getAgencyName03());
        instance.setAgency3Status(example.getAgencyStatus03());
        instance.setAgency4Code(example.getAgencyCode04());
        instance.setAgency4Name(example.getAgencyName04());
        instance.setAgency4Status(example.getAgencyStatus04());
        instance.setAgency5Code(example.getAgencyCode05());
        instance.setAgency5Name(example.getAgencyName05());
        instance.setAgency5Status(example.getAgencyStatus05());
        instance.setAgency6Code(example.getAgencyCode06());
        instance.setAgency6Name(example.getAgencyName06());
        instance.setAgency6Status(example.getAgencyStatus06());
        instance.setAgency7Code(example.getAgencyCode07());
        instance.setAgency7Name(example.getAgencyName07());
        instance.setAgency7Status(example.getAgencyStatus07());
        instance.setAgency8Code(example.getAgencyCode08());
        instance.setAgency8Name(example.getAgencyName08());
        instance.setAgency8Status(example.getAgencyStatus08());
        instance.setAgency9Code(example.getAgencyCode09());
        instance.setAgency9Name(example.getAgencyName09());
        instance.setAgency9Status(example.getAgencyStatus09());
        instance.setAgency10Code(example.getAgencyCode10());
        instance.setAgency10Name(example.getAgencyName10());
        instance.setAgency10Status(example.getAgencyStatus10());
        instance.setPaymentFeeAmount(example.getPaymentFeeAmount());
        instance.setFingerFeeRate(example.getFingerFeeRate());
        instance.setBranchCode(example.getBranchCode());
        instance.setStatusCd(statusCd);
        instance.setTypeCd(typeCd);
        instance.setCreateUser(user);
        instance.setCreateDate(new Date());
        instance.setModifyUser(user);
        instance.setModifyDate(new Date());
        
        if(clientDO != null) {
            instance.setAsisClientName(clientDO.getName());
            instance.setAsisClientIdentityNo(clientDO.getIdentityNo());
            switch (clientDO.getStatus()) {
                case "ST01":
                case "ST03":
                case "ST04":
                case "ST05":
                case "ST06":
                case "ST07":
                    instance.setAsisClientStatus("1");
                    break;

                case "ST02":
                default:
                    instance.setAsisClientStatus("3");
                    break;
            }

            if(clientDO.agencyEnabled("00000")) {
                instance.setAsisAgency1Code("00000");
                instance.setAsisAgency1Name(clientDO.getAgencyName("00000"));
            }

            if(clientDO.agencyEnabled("00001")) {
                instance.setAsisAgency2Code("00001");
                instance.setAsisAgency2Name(clientDO.getAgencyName("00001"));
            }

            if(clientDO.agencyEnabled("00002")) {
                instance.setAsisAgency3Code("00002");
                instance.setAsisAgency3Name(clientDO.getAgencyName("00002"));
            }

            if(clientDO.agencyEnabled("00003")) {
                instance.setAsisAgency4Code("00003");
                instance.setAsisAgency4Name(clientDO.getAgencyName("00003"));
            }

            if(clientDO.agencyEnabled("00004")) {
                instance.setAsisAgency5Code("00004");
                instance.setAsisAgency5Name(clientDO.getAgencyName("00004"));
            }

            if(clientDO.agencyEnabled("00005")) {
                instance.setAsisAgency6Code("00005");
                instance.setAsisAgency6Name(clientDO.getAgencyName("00005"));
            }

            if(clientDO.agencyEnabled("00006")) {
                instance.setAsisAgency7Code("00006");
                instance.setAsisAgency7Name(clientDO.getAgencyName("00006"));
            }

            if(clientDO.agencyEnabled("00007")) {
                instance.setAsisAgency8Code("00007");
                instance.setAsisAgency8Name(clientDO.getAgencyName("00007"));
            }

            if(clientDO.agencyEnabled("00008")) {
                instance.setAsisAgency9Code("00008");
                instance.setAsisAgency9Name(clientDO.getAgencyName("00008"));
            }

            if(clientDO.agencyEnabled("00009")) {
                instance.setAsisAgency10Code("00009");
                instance.setAsisAgency10Name(clientDO.getAgencyName("00009"));
            }

            instance.setAsisPaymentFeeAmount(clientDO.getReceiptFee());
            if(clientDO.getReceiptFee() == 0) {
                instance.setAsisFingerFeeRate(0);
            } else {
                instance.setAsisFingerFeeRate(Double.valueOf(100 - Math.floor(100 * clientDO.getReceiptBankFee() / clientDO.getReceiptFee())).intValue());
            }
            instance.setAsisBranchCode(clientDO.getBranchCode());
        }

        return instance;
    }

    private void setAsisClientIdentityNo(String clientIdentityNo) {
        damoaClientInfoPullHist.setAsisClientIdNo(clientIdentityNo);
    }

    private void setAsisBranchCode(String branchCode) {
        damoaClientInfoPullHist.setAsisBrchCode(branchCode);
    }

    private void setAsisFingerFeeRate(int fingerFeeRate) {
        damoaClientInfoPullHist.setAsisFingerFeeRate(fingerFeeRate);
    }

    private void setAsisPaymentFeeAmount(long receiptFee) {
        damoaClientInfoPullHist.setAsisPaymentFeeAmt((int) receiptFee);
    }

    public DamoaClientInfoPullHist getDamoaClientInfoPullHist() {
        return damoaClientInfoPullHist;
    }

    private void setAsisAgency1Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency1Code(agencyCode01);
    }

    private void setAsisAgency1Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency1Name(agencyName01);
    }

    private void setAsisAgency2Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency2Code(agencyCode01);
    }

    private void setAsisAgency2Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency2Name(agencyName01);
    }

    private void setAsisAgency3Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency3Code(agencyCode01);
    }

    private void setAsisAgency3Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency3Name(agencyName01);
    }

    private void setAsisAgency4Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency4Code(agencyCode01);
    }

    private void setAsisAgency4Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency4Name(agencyName01);
    }

    private void setAsisAgency5Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency5Code(agencyCode01);
    }

    private void setAsisAgency5Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency5Name(agencyName01);
    }

    private void setAsisAgency6Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency6Code(agencyCode01);
    }

    private void setAsisAgency6Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency6Name(agencyName01);
    }

    private void setAsisAgency7Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency7Code(agencyCode01);
    }

    private void setAsisAgency7Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency7Name(agencyName01);
    }

    private void setAsisAgency8Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency8Code(agencyCode01);
    }

    private void setAsisAgency8Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency8Name(agencyName01);
    }

    private void setAsisAgency9Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency9Code(agencyCode01);
    }

    private void setAsisAgency9Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency9Name(agencyName01);
    }

    private void setAsisAgency10Code(String agencyCode01) {
        damoaClientInfoPullHist.setAsisAgency10Code(agencyCode01);
    }

    private void setAsisAgency10Name(String agencyName01) {
        damoaClientInfoPullHist.setAsisAgency10Name(agencyName01);
    }

    private void setAsisClientStatus(String clientStatus) {
        damoaClientInfoPullHist.setAsisClientStatus(clientStatus);
    }

    private void setAsisClientName(String clientName) {
        this.damoaClientInfoPullHist.setAsisClientName(clientName);
    }

    private void setAgency1Code(String agency1Code) {
        damoaClientInfoPullHist.setAgency1Code(agency1Code);
    }

    private void setAgency1Name(String agency1Name) {
        damoaClientInfoPullHist.setAgency1Name(agency1Name);
    }

    private void setAgency1Status(String agency1Status) {
        damoaClientInfoPullHist.setAgency1Status(agency1Status);
    }

    private void setAgency2Code(String agency2Code) {
        damoaClientInfoPullHist.setAgency2Code(agency2Code);
    }

    private void setAgency2Name(String agency2Name) {
        damoaClientInfoPullHist.setAgency2Name(agency2Name);
    }

    private void setAgency2Status(String agency2Status) {
        damoaClientInfoPullHist.setAgency2Status(agency2Status);
    }

    private void setAgency3Code(String agency3Code) {
        damoaClientInfoPullHist.setAgency3Code(agency3Code);
    }

    private void setAgency3Name(String agency3Name) {
        damoaClientInfoPullHist.setAgency3Name(agency3Name);
    }

    private void setAgency3Status(String agency3Status) {
        damoaClientInfoPullHist.setAgency3Status(agency3Status);
    }

    private void setAgency4Code(String agency4Code) {
        damoaClientInfoPullHist.setAgency4Code(agency4Code);
    }

    private void setAgency4Name(String agency4Name) {
        damoaClientInfoPullHist.setAgency4Name(agency4Name);
    }

    private void setAgency4Status(String agency4Status) {
        damoaClientInfoPullHist.setAgency4Status(agency4Status);
    }

    private void setAgency5Code(String agency5Code) {
        damoaClientInfoPullHist.setAgency5Code(agency5Code);
    }

    private void setAgency5Name(String agency5Name) {
        damoaClientInfoPullHist.setAgency5Name(agency5Name);
    }

    private void setAgency5Status(String agency5Status) {
        damoaClientInfoPullHist.setAgency5Status(agency5Status);
    }

    private void setAgency6Code(String agency6Code) {
        damoaClientInfoPullHist.setAgency6Code(agency6Code);
    }

    private void setAgency6Name(String agency6Name) {
        damoaClientInfoPullHist.setAgency6Name(agency6Name);
    }

    private void setAgency6Status(String agency6Status) {
        damoaClientInfoPullHist.setAgency6Status(agency6Status);
    }

    private void setAgency7Code(String agency7Code) {
        damoaClientInfoPullHist.setAgency7Code(agency7Code);
    }

    private void setAgency7Name(String agency7Name) {
        damoaClientInfoPullHist.setAgency7Name(agency7Name);
    }

    private void setAgency7Status(String agency7Status) {
        damoaClientInfoPullHist.setAgency7Status(agency7Status);
    }

    private void setAgency8Code(String agency8Code) {
        damoaClientInfoPullHist.setAgency8Code(agency8Code);
    }

    private void setAgency8Name(String agency8Name) {
        damoaClientInfoPullHist.setAgency8Name(agency8Name);
    }

    private void setAgency8Status(String agency8Status) {
        damoaClientInfoPullHist.setAgency8Status(agency8Status);
    }

    private void setAgency9Code(String agency9Code) {
        damoaClientInfoPullHist.setAgency9Code(agency9Code);
    }

    private void setAgency9Name(String agency9Name) {
        damoaClientInfoPullHist.setAgency9Name(agency9Name);
    }

    private void setAgency9Status(String agency9Status) {
        damoaClientInfoPullHist.setAgency9Status(agency9Status);
    }

    private void setFingerFeeRate(int fingerFeeRate) {
        damoaClientInfoPullHist.setFingerFeeRate(fingerFeeRate);
    }

    private void setBranchCode(String brchCode) {
        damoaClientInfoPullHist.setBrchCode(brchCode);
    }


    public String getClientName() {
        return damoaClientInfoPullHist.getClientName();
    }

    private void setClientName(String clientName) {
        damoaClientInfoPullHist.setClientName(clientName);
    }

    public String getClientDeleteYn() {
        return damoaClientInfoPullHist.getClientDeleteYn();
    }

    private void setClientDeleteYn(String clientDeleteYn) {
        damoaClientInfoPullHist.setClientDeleteYn(clientDeleteYn);
    }

    public String getRelayClientId() {
        return damoaClientInfoPullHist.getRelayClientId();
    }

    private void setRelayClientId(String relayClientId) {
        damoaClientInfoPullHist.setRelayClientId(relayClientId);
    }

    public String getClientCreateDt() {
        return damoaClientInfoPullHist.getRgstDt();
    }

    private void setClientCreateDt(String rgstDt) {
        damoaClientInfoPullHist.setRgstDt(rgstDt);
    }

    public String getClientIdentityNo() {
        return damoaClientInfoPullHist.getClientIdNo();
    }

    private void setClientIdentityNo(String clientIdNo) {
        damoaClientInfoPullHist.setClientIdNo(clientIdNo);
    }

    public String getClientStatus() {
        return damoaClientInfoPullHist.getClientStatus();
    }

    private void setClientStatus(String clientStatus) {
        damoaClientInfoPullHist.setClientStatus(clientStatus);
    }

    public String getAgency01Code() {
        return damoaClientInfoPullHist.getAgency1Code();
    }

    public String getAgency01Name() {
        return damoaClientInfoPullHist.getAgency1Name();
    }

    public String getAgency01Status() {
        return damoaClientInfoPullHist.getAgency1Status();
    }

    public String getAgency02Code() {
        return damoaClientInfoPullHist.getAgency2Code();
    }

    public String getAgency02Name() {
        return damoaClientInfoPullHist.getAgency2Name();
    }

    public String getAgency02Status() {
        return damoaClientInfoPullHist.getAgency2Status();
    }

    public String getAgency03Code() {
        return damoaClientInfoPullHist.getAgency3Code();
    }

    public String getAgency03Name() {
        return damoaClientInfoPullHist.getAgency3Name();
    }

    public String getAgency03Status() {
        return damoaClientInfoPullHist.getAgency3Status();
    }

    public String getAgency04Code() {
        return damoaClientInfoPullHist.getAgency4Code();
    }

    public String getAgency04Name() {
        return damoaClientInfoPullHist.getAgency4Name();
    }

    public String getAgency04Status() {
        return damoaClientInfoPullHist.getAgency4Status();
    }

    public String getAgency05Code() {
        return damoaClientInfoPullHist.getAgency5Code();
    }

    public String getAgency05Name() {
        return damoaClientInfoPullHist.getAgency5Name();
    }

    public String getAgency05Status() {
        return damoaClientInfoPullHist.getAgency5Status();
    }

    public String getAgency06Code() {
        return damoaClientInfoPullHist.getAgency6Code();
    }

    public String getAgency06Name() {
        return damoaClientInfoPullHist.getAgency6Name();
    }

    public String getAgency06Status() {
        return damoaClientInfoPullHist.getAgency6Status();
    }

    public String getAgency07Code() {
        return damoaClientInfoPullHist.getAgency7Code();
    }

    public String getAgency07Name() {
        return damoaClientInfoPullHist.getAgency7Name();
    }

    public String getAgency07Status() {
        return damoaClientInfoPullHist.getAgency7Status();
    }

    public String getAgency08Code() {
        return damoaClientInfoPullHist.getAgency8Code();
    }

    public String getAgency08Name() {
        return damoaClientInfoPullHist.getAgency8Name();
    }

    public String getAgency08Status() {
        return damoaClientInfoPullHist.getAgency8Status();
    }

    public String getAgency09Code() {
        return damoaClientInfoPullHist.getAgency9Code();
    }

    public String getAgency09Name() {
        return damoaClientInfoPullHist.getAgency9Name();
    }

    public String getAgency09Status() {
        return damoaClientInfoPullHist.getAgency9Status();
    }

    public String getAgency10Code() {
        return damoaClientInfoPullHist.getAgency10Code();
    }

    private void setAgency10Code(String agency10Code) {
        damoaClientInfoPullHist.setAgency10Code(agency10Code);
    }

    public String getAgency10Name() {
        return damoaClientInfoPullHist.getAgency10Name();
    }

    private void setAgency10Name(String agency10Name) {
        damoaClientInfoPullHist.setAgency10Name(agency10Name);
    }

    public String getAgency10Status() {
        return damoaClientInfoPullHist.getAgency10Status();
    }

    private void setAgency10Status(String agency10Status) {
        damoaClientInfoPullHist.setAgency10Status(agency10Status);
    }

    public Integer getPaymentFeeAmount() {
        return damoaClientInfoPullHist.getPaymentFeeAmt();
    }

    private void setPaymentFeeAmount(int paymentFeeAmt) {
        damoaClientInfoPullHist.setPaymentFeeAmt(paymentFeeAmt);
    }

    public Integer getFingerFeeRate() {
        return damoaClientInfoPullHist.getFingerFeeRate();
    }

    public void setFingerFeeRate(Integer fingerFeeRate) {
        damoaClientInfoPullHist.setFingerFeeRate(fingerFeeRate);
    }

    public String getBranchCode() {
        return damoaClientInfoPullHist.getBrchCode();
    }

    public void setBrchCode(String brchCode) {
        damoaClientInfoPullHist.setBrchCode(brchCode);
    }

    public String getStatusCd() {
        return damoaClientInfoPullHist.getStatusCd();
    }

    private void setStatusCd(String statusCd) {
        damoaClientInfoPullHist.setStatusCd(statusCd);
    }

    public Date getCreateDate() {
        return damoaClientInfoPullHist.getCreateDate();
    }

    private void setCreateDate(Date createDate) {
        damoaClientInfoPullHist.setCreateDate(createDate);
    }

    public String getCreateUser() {
        return damoaClientInfoPullHist.getCreateUser();
    }

    private void setCreateUser(String createUser) {
        damoaClientInfoPullHist.setCreateUser(createUser);
    }

    public Date getModifyDate() {
        return damoaClientInfoPullHist.getModifyDate();
    }

    private void setModifyDate(Date modifyDate) {
        damoaClientInfoPullHist.setModifyDate(modifyDate);
    }

    public String getModifyUser() {
        return damoaClientInfoPullHist.getModifyUser();
    }

    private void setModifyUser(String modifyUser) {
        damoaClientInfoPullHist.setModifyUser(modifyUser);
    }

    public String getPullDt() {
        return damoaClientInfoPullHist.getPullDt();
    }

    private void setPullDt(String pullDt) {
        damoaClientInfoPullHist.setPullDt(pullDt);
    }

    public String getClientId() {
        return damoaClientInfoPullHist.getClientId();
    }

    private void setClientId(String clientId) {
        damoaClientInfoPullHist.setClientId(clientId);
    }

    public String getTypeCd() {
        return damoaClientInfoPullHist.getTypeCd();
    }

    private void setTypeCd(String typeCd) {
        damoaClientInfoPullHist.setTypeCd(typeCd);
    }

    public void switchStatusToStandby() {
        LOGGER.info("기관[{}] 상태를 준비로 변경", getClientId());
        setStatusCd(STATUS_CD_STANDBY);
    }

    public void switchStatusToSuccess() {
        LOGGER.info("기관[{}] 상태를 성공으로 변경", getClientId());
        setStatusCd(STATUS_CD_SUCCESS);
    }

    public void switchStatusToFail() {
        LOGGER.info("기관[{}] 상태를 실패로 변경", getClientId());
        setStatusCd(STATUS_CD_FAIL);
    }

    public int getAsisPaymentFeeAmount() {
        if(damoaClientInfoPullHist.getAsisPaymentFeeAmt() == null) {
            return 0;
        }

        return damoaClientInfoPullHist.getAsisPaymentFeeAmt();
    }
    
    public int getAsisFingerFeeRate() {
        if(damoaClientInfoPullHist.getAsisFingerFeeRate() == null) {
            return 0;
        }
        
        return damoaClientInfoPullHist.getAsisFingerFeeRate();
    }

    public String getAsisBranchCode() {
        return damoaClientInfoPullHist.getAsisBrchCode();
    }
}
