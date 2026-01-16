package com.finger.shinhandamoa.bank.service;

import com.finger.shinhandamoa.bank.dao.BankMgmtDAO;
import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.common.CompanyStatusLookupAPI;
import com.finger.shinhandamoa.common.CompanyStatusLookupAPI.IFX1002;
import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.data.table.model.ChaExample;
import com.finger.shinhandamoa.data.table.model.KftcCode;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 홍길동
 * @date 2018. 3. 30.
 * @desc 최초생성
 * @version
 */

/**
 * @author NAMGUNWOO
 *
 */
@Service
public class BankMgmtServiceImpl implements BankMgmtService {

    private static final Logger logger = LoggerFactory.getLogger(BankMgmtServiceImpl.class);

    @Autowired
    private BankMgmtDAO bankMgmtDAO;
    
    @Autowired
    private ChaMapper chaMapper;

    @Override
    public int getBranchListTotalCount(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getBranchListTotalCount(map);
    }

    @Override
    public List<BankReg01DTO> getBranchListAll(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getBranchListAll(map);
    }

    @Override
    public int getCollectorListTotalCount(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getCollectorListTotalCount(map);
    }

    @Override
    public List<BankReg01DTO> getCollectorListAll(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getCollectorListAll(map);
    }

    @Override
    public int getNewChaListTotalCount(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getNewChaListTotalCount(map);
    }

    @Override
    public List<BankReg01DTO> getNewChaListAll(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getNewChaListAll(map);
    }

    @Override
    public BankReg01DTO selectChaListInfo(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.selectChaListInfo(map);
    }

    @Override
    public SysChaMgmtDTO selectChaListDetail(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.selectChaListDetail(map);
    }

    @Override
    public List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getAgencyList(map);
    }

    @Override
    public void updateChaSt(HashMap<String, Object> map) throws Exception {
        bankMgmtDAO.updateChaSt(map);
    }

    @Override
    public BankReg01DTO getBankFeeListTotalCount(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getBankFeeListTotalCount(map);
    }

    @Override
    public List<BankReg01DTO> getBankFeeListAll(HashMap<String, Object> map) throws Exception {
        return bankMgmtDAO.getBankFeeListAll(map);
    }

    @Override
    public void updateXChaList(BankReg01DTO dto) throws Exception {
        logger.info("updateXChaList.dto.toString() : {}", dto.toString());

        String errCode = "0000";
        CompanyStatusLookupAPI api = new CompanyStatusLookupAPI("211.111.216.19", 8080);
        String chkChacd = dto.getChaCd();
        String chkChast = dto.getChast();

        //일괄승인처리시에만 휴폐업처리하는지 검증하여 폐업상태이면 오류 처리?
        if ("B".equals(dto.getJobType()) && "ST01".equals(chkChast)) { // 확인 dto추가 jobType

            logger.debug("updateXChaList.CompanyStatusLookupAPI.chkChacd : {}", chkChacd);
            final CompanyStatusLookupAPI.IFX1002 result = api.lookup(chkChacd);
            logger.trace("updateXChaList.CompanyStatusLookupAPI.Result : {}", result);
        }
        if (dto.getJobType().equals("N")) {
            final ChaExample example = new ChaExample();
            example.createCriteria().andChacdEqualTo(dto.getChaCd());
            final List<Cha> cha = chaMapper.selectByExample(example);
            for (Cha chaWithBLOBs : cha) {
                chaWithBLOBs.setChatrty(dto.getChatrty());
                chaWithBLOBs.setAmtchkty(dto.getAmtChkTy());
                chaWithBLOBs.setChatype(dto.getChaType());
                chaWithBLOBs.setChastatus(dto.getChaStatus());
                chaWithBLOBs.setOwner(dto.getOwner());
                chaWithBLOBs.setOwnertel(dto.getOwnerTel());
                chaWithBLOBs.setChazipcode(dto.getChaZipCode());
                chaWithBLOBs.setChaaddress1(dto.getChaAddress1());
                chaWithBLOBs.setChaaddress2(dto.getChaAddress2());
                chaWithBLOBs.setChrname(dto.getChrName());
                chaWithBLOBs.setChrmail(dto.getChrMail());
                chaWithBLOBs.setChrtelno(dto.getChrTelNo());
                chaWithBLOBs.setChrhp(dto.getChrHp());
                chaWithBLOBs.setNotaxyn(dto.getNoTaxYn());
                chaWithBLOBs.setPartialPayment(dto.getPartialPayment());
                chaWithBLOBs.setRcpreqyn(dto.getRcpReqYn());
                chaWithBLOBs.setRcpreqty(dto.getRcpReqTy());
                chaWithBLOBs.setRcpreqsvety(dto.getRcpReqSveTy());
                chaWithBLOBs.setMnlrcpreqty(dto.getMnlRcpReqTy());
                chaWithBLOBs.setNotminlimit((int) dto.getNotMinLimit());
                chaWithBLOBs.setNotminfee((int) dto.getNotMinFee());
                chaWithBLOBs.setNotcntfee((int) dto.getNotCntFee());
                chaWithBLOBs.setRemark(dto.getRemark());
                chaWithBLOBs.setCusnamety(dto.getCusNameTy());
                chaWithBLOBs.setMandRcpYn(StringUtils.defaultString(dto.getMandRcpYn()));
                chaWithBLOBs.setBillcont1(dto.getConcurrentBlockYn());
            }
            chaMapper.updateByPrimaryKey(cha.get(0));
        } else {
            bankMgmtDAO.updateXChaList(dto);
        }
    }

    @Override
    public void updateChaListInfo(HashMap<String, Object> map) throws Exception {
        bankMgmtDAO.updateChaListInfo(map);
    }

    @Override
    public IFX1002 chkChaConfirmManual(BankReg01DTO dto) throws Exception {
        String errCode = "0000";
        CompanyStatusLookupAPI api = new CompanyStatusLookupAPI("211.111.216.19", 8080);
        String chkChaOffNo = dto.getChaOffNo();

        final CompanyStatusLookupAPI.IFX1002 result = api.lookup(chkChaOffNo);
//        logger.info("updateXChaList.CompanyStatusLookupAPI.chkChaOffNo : {}", chkChaOffNo);
//        logger.info("updateXChaList.CompanyStatusLookupAPI.Result : {}", result.toString());
//        logger.info("updateXChaList.CompanyStatusLookupAPI.Result.getCode : {}", result.getCode());
//        logger.info("updateXChaList.CompanyStatusLookupAPI.Result.getUpdateDate : {}", result.getUpdateDate());
//        logger.info("updateXChaList.CompanyStatusLookupAPI.Result.getMessage : {}", result.getMessage());
//        logger.info("updateXChaList.CompanyStatusLookupAPI.Result.getTaxGbn : {}", result.getTaxGbn());
//        logger.info("updateXChaList.CompanyStatusLookupAPI.Result.getCloseDate : {}", result.getCloseDate());

        return result;
    }

    @Override
    public void insertJobhistory(HashMap<String, Object> map) throws Exception {
        bankMgmtDAO.insertJobhistory(map);
    }

    @Override
    public void updateWebuser(SysChaMgmtDTO dto) throws Exception {
        bankMgmtDAO.updateWebuser(dto);
    }

    @Override
    public List<KftcCode> getBnkList(Map<String, Object> paramMap) throws Exception {
        return bankMgmtDAO.getBnkList(paramMap);
    }

    @Override
    public String insertXchalist(SysChaMgmtDTO dto) throws Exception {
        dto.setRcpdtdupyn("N");
        dto.setUsesmsyn("N");
        dto.setNotsmsyn("N");
        dto.setRcpsmsyn("N");
        dto.setRcpsmsty("N");
        dto.setUsemailyn("N");
        dto.setCusgubnyn1("N");
        dto.setCusgubnyn2("N");
        dto.setCusgubnyn3("N");
        dto.setCusgubnyn4("N");
        dto.setChasvcyn("N");
        dto.setBillimgty("A");
        dto.setUsepgyn("N");
        dto.setChkcash("Y");
        dto.setChkoff("Y");
        return bankMgmtDAO.insertXchaList(dto);
    }

    @Override
    public void updateXchaList(SysChaMgmtDTO dto) throws Exception {
        bankMgmtDAO.updateXchaList(dto);
    }
}
