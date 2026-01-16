package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.biz.CompanyStatusLookupAPI;
import kr.co.finger.damoa.scheduler.ThreadSession;
import kr.co.finger.damoa.scheduler.ThreadSessionHolder;
import kr.co.finger.shinhandamoa.data.table.model.Xadjgroup;
import kr.co.finger.shinhandamoa.data.table.model.XadjgroupKey;
import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 기관 도메인
 *
 * @author wisehouse@finger.co.kr
 */
public class ClientDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDO.class);

    /**
     * 승인 / 해피콜 대기
     */
    public static final String CLIENT_STATUS_WAIT = "ST01";

    /**
     * 해지
     */
    public static final String CLIENT_STATUS_TERMINATE = "ST02";

    /**
     * 정지
     */
    public static final String CLIENT_STATUS_HOLD = "ST04";

    /**
     * 승인 대기
     */
    public static final String CLIENT_STATUS_EVAL_WAIT = "ST05";

    /**
     * 정상
     */
    public static final String CLIENT_STATUS_OK = "ST06";

    /**
     * 승인 거부
     */
    public static final String CLIENT_STATUS_EVAL_REJECT = "ST07";

    private Xchalist xchalist;

    private List<Xadjgroup> xadjgroupList;

    private List<Xadjgroup> createXadjgroupList = new ArrayList<>();

    private List<Xadjgroup> modifyXadjgroupList = new ArrayList<>();

    private List<Xadjgroup> deleteXadjgroupList = new ArrayList<>();

    public ClientDO(Xchalist xchalist, List<Xadjgroup> xadjgroupList) {
        this.xchalist = xchalist;
        this.xadjgroupList = xadjgroupList;
    }

    private ClientDO() {
        this(new Xchalist(), new ArrayList<>());
        this.xchalist.setRcpdtdupyn("Y");
        this.xchalist.setUsesmsyn("N");
        this.xchalist.setNotsmsyn("M");
        this.xchalist.setRcpsmsyn("N");
        this.xchalist.setRcpsmsty("M");
        this.xchalist.setUsemailyn("N");
        this.xchalist.setAmtchkty("Y");
        this.xchalist.setCusnamety("U");
        this.xchalist.setNotaxyn("N");
        this.xchalist.setChatrty("01");
        this.xchalist.setCusgubnyn1("Y");
        this.xchalist.setCusgubnyn2("Y");
        this.xchalist.setCusgubnyn3("Y");
        this.xchalist.setCusgubnyn4("Y");
        this.xchalist.setChasvcyn("N");
        this.xchalist.setBillimgty("A");
        this.xchalist.setUsepgyn("N");
        this.xchalist.setChkcash("N");
        this.xchalist.setChkoff("N");
        this.xchalist.setChkcms("N");
        this.xchalist.setAtyn("N");
        this.xchalist.setNotatyn("N");
        this.xchalist.setRcpatyn("N");
    }

    private void disableCashReceipt() {
        this.xchalist.setRcpreqyn("N");
        this.xchalist.setRcpreqty("M");
    }

    public static ClientDO newInstance(String clientId, String clientName, String deleteYn, String createDt, String clientIdentityNo, String branchCode) {
        final ClientDO clientDO = new ClientDO();
        clientDO.setId(clientId);
        clientDO.setName(clientName);
        clientDO.setIdentityNo(clientIdentityNo);
        clientDO.setBranchCode(branchCode);
        clientDO.setStatus(CLIENT_STATUS_EVAL_WAIT);
        clientDO.setBankRegisterDt(createDt);
        clientDO.disableAgency();
        clientDO.disableCashReceipt();

        return clientDO;
    }

    private void setBranchCode(String branchCode) {
        xchalist.setAgtcd(branchCode);
    }

    private void setBankRegisterDt(String bankRegisterDt) {
        this.xchalist.setBankRegiDt(bankRegisterDt);
    }

    public String getId() {
        return xchalist.getChacd();
    }

    private void setId(String clientId) {
        this.xchalist.setChacd(clientId);
    }

    public long getReceiptFee() {
        return xchalist.getRcpcntfee();
    }

    public void setReceiptFee(int receiptFee) {
        this.xchalist.setRcpcntfee(receiptFee);
    }

    public long getReceiptBankFee() {
        return xchalist.getRcpbnkfee();
    }

    public void setReceiptBankFee(int receiptBankFee) {
        this.xchalist.setRcpbnkfee(receiptBankFee);
    }

    public long getSmsFee() {
        return 20;
    }

    public long getLmsFee() {
        return 40;
    }

    public long getAtFee() { return 20; }

    public long getNoticeFee() {
        return xchalist.getNotcntfee();
    }

    public long getNoticeBaseCharge() {
        return xchalist.getNotminfee();
    }

    public long getNoticeBaseCount() {
        return xchalist.getNotminlimit();
    }

    public void delete() {
        // TODO 삭제하는 것과 해지하는 것의 차이 확인 필요. 현재는 기관을 삭제하는 프로세스 정의 없음
    }

    public Xchalist getXchalist() {
        return xchalist;
    }

    private void setStatus(String clientStatus) {
        this.xchalist.setChast(clientStatus);
    }

    private void setIdentityNo(String clientIdentityNo) {
        this.xchalist.setChaoffno(clientIdentityNo);
    }

    private void setName(String clientName) {
        this.xchalist.setChaname(clientName);
    }

    public void clearAgencyList() {
        this.xadjgroupList.clear();
    }

    public synchronized void createAgency(String agencyCode, String agencyName) {
        LOGGER.debug("대리점 추가 [{}, {}]", xchalist.getChacd(), agencyCode);
        if(ListUtils.select(xadjgroupList, xadjgroup -> StringUtils.equals(xadjgroup.getAdjfiregkey(), agencyCode)).size() > 0) {
            LOGGER.warn("대리점 코드 중복 [{}, {}]", xchalist.getChacd(), agencyCode);
            return;
        }
        
        final Xadjgroup xadjgroup = new Xadjgroup();
        xadjgroup.setChacd(xchalist.getChacd());
        xadjgroup.setAdjfiregkey(agencyCode);
        xadjgroup.setGrpadjname(agencyName);
        xadjgroup.setMakedt(new Date());
        xadjgroup.setMaker(ThreadSessionHolder.getInstance().getSession("damoa-domain").getAttribute(ThreadSession.SESSION_ATTRIBUTE_KEY_OWNER));
        xadjgroup.setRegdt(new Date());
        
        createXadjgroupList.add(xadjgroup);
        
        xadjgroupList.add(xadjgroup);
        Collections.sort(xadjgroupList, Comparator.comparing(XadjgroupKey::getAdjfiregkey));
        if(xadjgroupList.size() > 1) {
            enableAgency();
        } else {
            disableAgency();
        }
    }

    private void disableAgency() {
        this.xchalist.setAdjaccyn("N");
    }
    
    private void enableAgency() {
        this.xchalist.setAdjaccyn("Y");
    }

    public void deleteAgency(String agencyCode, String agencyName) {
        LOGGER.debug("대리점 삭제 [{}, {}]", xchalist.getChacd(), agencyCode);
        List<Xadjgroup> select = ListUtils.select(xadjgroupList, xadjgroup -> StringUtils.equals(xadjgroup.getAdjfiregkey(), agencyCode));
        if(select.size() != 1) {
            LOGGER.warn("삭제할 대리점 코드 없음 [{}, {}]", xchalist.getChacd(), agencyCode);
            return;
        }
        
        deleteXadjgroupList.addAll(select);
        
        xadjgroupList.removeAll(select);

        if(xadjgroupList.size() > 1) {
            enableAgency();
        } else {
            disableAgency();
        }
    }

    private void setTerminateDt(String terminateDt) {
        this.xchalist.setChaCloseDt(terminateDt);
    }

    public void terminate() {
        setStatus(CLIENT_STATUS_TERMINATE);
        setTerminateDt(DateUtils.format(new Date(), "yyyyMMdd"));
    }

    public void hold() {
        setStatus(CLIENT_STATUS_HOLD);
    }

    public List<Xadjgroup> getCreateXadjgroupList() {
        return this.createXadjgroupList;
    }

    public List<Xadjgroup> getDeleteXadjgroupList() {
        return this.deleteXadjgroupList;
    }

    public boolean deleted() {
        return false;
    }

    public void lookupStatusOnNTS(CompanyStatusLookupAPI companyStatusLookupAPI) {
        LOGGER.debug("국세청 휴폐업 조회");
        
        try {
            LOGGER.debug("국세청 휴폐업 조회 선딜 10초");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            
        }
        
        final CompanyStatusLookupAPI.IFX1002 result = companyStatusLookupAPI.lookup(this.xchalist.getChaoffno());
        final String code = result.getTaxGbn();
        
        switch (code) {
            case CompanyStatusLookupAPI.CODE_ERROR:
                LOGGER.warn("조회 실패");
                break;
            case CompanyStatusLookupAPI.CODE_REST:
                LOGGER.debug("휴업");
                this.xchalist.setChaCloseSt("31");
                this.xchalist.setChaCloseVarReson("휴업 사업자");
                this.xchalist.setChaCloseChk("Y");
                break;
            case CompanyStatusLookupAPI.CODE_CLOSE:
                LOGGER.debug("폐업");
                this.xchalist.setChaCloseSt("32");
                this.xchalist.setChaCloseVarReson("폐업 사업자");
                this.xchalist.setChaCloseChk("Y");
                break;
            default:
                LOGGER.debug("정상");
                this.xchalist.setChaCloseSt("11");
                this.xchalist.setChaCloseVarReson("폐업 사업자");
                this.xchalist.setChaCloseChk("N");
                break;
        }
        
        this.xchalist.setChaCloseVarDt(DateUtils.format(new Date(), "yyyyMMdd"));
    }

    public String getName() {
        return xchalist.getChaname();
    }

    public String getStatus() {
        return xchalist.getChast();
    }

    public String getBranchCode() {
        return xchalist.getAgtcd();
    }

    public boolean agencyEnabled(String agencyCode) {
        for (Xadjgroup each : xadjgroupList) {
            if (StringUtils.equals(each.getAdjfiregkey(), agencyCode)) {
                return true;
            }
        }
        return false;
    }
    
    public String getAgencyName(String agencyCode) {
        for (Xadjgroup each : xadjgroupList) {
            if (StringUtils.equals(each.getAdjfiregkey(), agencyCode)) {
                return each.getGrpadjname();
            }
        }
        return StringUtils.EMPTY;
    }

    public void modifyName(String name) {
        xchalist.setChaname(name);
    }

    public void modifyBranchCode(String branchCode) {
        setBranchCode(branchCode);
    }
    
    public String getIdentityNo() {
        return xchalist.getChaoffno();
    }

    public boolean getInvoiceFree() {
        return StringUtils.equals(xchalist.getPrnFreeYn(), "Y");
    }

    public List<Xadjgroup> getXadjgroupList() {
        return xadjgroupList;
    }

    public void setXadjgroupList(List<Xadjgroup> xadjgroupList) {
        this.xadjgroupList = xadjgroupList;
    }
}
