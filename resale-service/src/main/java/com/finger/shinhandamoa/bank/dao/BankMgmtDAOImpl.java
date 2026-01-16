package com.finger.shinhandamoa.bank.dao;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.data.table.model.KftcCode;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  홍길동
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Transactional
@Repository
public class BankMgmtDAOImpl implements BankMgmtDAO {

	private static final Logger logger = LoggerFactory.getLogger(BankMgmtDAOImpl.class);

	@Inject
	private SqlSession sqlSession;

	@Override
	public int getBranchListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("BankMgmtDao.getBranchListTotalCount", map);	
	}

	@Override
	public List<BankReg01DTO> getBranchListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("BankMgmtDao.getBranchListAll", map);
	}
	
	@Override
	public int getCollectorListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("BankMgmtDao.getCollectorListTotalCount", map);	
	}

	@Override
	public List<BankReg01DTO> getCollectorListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("BankMgmtDao.getCollectorListAll", map);
	}
	
	@Override
	public int getNewChaListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("BankMgmtDao.getNewChaListTotalCount", map);
	}

	@Override
	public List<BankReg01DTO> getNewChaListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("BankMgmtDao.getNewChaListAll", map);
	}
	
	@Override
	public BankReg01DTO selectChaListInfo(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("BankMgmtDao.selectChaListInfo", map);		
	}

    @Override
    public SysChaMgmtDTO selectChaListDetail(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("BankMgmtDao.selectChaListDetail", map);
    }

	@Override
	public List<HashMap<String, Object>> getAgencyList(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("BankMgmtDao.getAgencyList", map);
	}
	
	@Override
	public void updateChaSt(HashMap<String, Object> map) throws Exception {
		sqlSession.update("BankMgmtDao.updateChaSt", map);
	}
	
	@Override
	public BankReg01DTO getBankFeeListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("BankMgmtDao.getBankFeeListTotalCount", map);				
	}

	@Override
	public List<BankReg01DTO> getBankFeeListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("BankMgmtDao.getBankFeeListAll", map);
	}
	
	@Override
	public void updateXChaList(BankReg01DTO dto) throws Exception {
        HashMap<String, Object> map =  new HashMap<String, Object>();
        map.put("chacd", dto.getChaCd());
        if(!dto.getJobType().equals("ACMS")) {
            map.put("maker", dto.getMaker());
            map.put("chast", dto.getChast());
            map.put("chaname", dto.getChaName());
            map.put("owner", dto.getOwner());
            map.put("ownertel", dto.getOwnerTel());
            map.put("chrmail", dto.getChrMail());
            map.put("chaoffno", dto.getChaOffNo());
            map.put("chrname", dto.getChrName());
            map.put("chrtelno", dto.getChrTelNo());
            map.put("chrhp", dto.getChrHp());
            map.put("chazipcode", dto.getChaZipCode());
            map.put("chaaddress1", dto.getChaAddress1());
            map.put("chaaddress2", dto.getChaAddress2());
            map.put("feeaccno", dto.getFeeAccNo());
            map.put("adjaccyn", dto.getAdjaccyn());
            map.put("prechast", dto.getPreChast());
            map.put("cmsFileName", dto.getCmsFileName());
            map.put("chatrty", dto.getChatrty());
            map.put("daMngMemo", dto.getDaMngMemo());
            map.put("usepgyn", dto.getUsePgYn());
            map.put("pgAprDt", dto.getPgAprDt());
            map.put("pgAprMemo", dto.getPgAprMemo());
            map.put("usesmsyn", dto.getUseSmsYn());
            map.put("smsAplDt", dto.getSmsAplDt());
            map.put("smsAplMemo", dto.getSmsAplMemo());
            map.put("pgComName", dto.getPgComName());
            map.put("pgServiceId", dto.getPgServiceId());
            map.put("pgLicenKey", dto.getPgLicenKey());
            map.put("chaNewRegDt", dto.getChaNewRegDt());
            map.put("chaCloseDt", dto.getChaCloseDt());
            map.put("notminlimit", dto.getNotMinLimit());
            map.put("notminfee", dto.getNotMinFee());
            map.put("notcntfee", dto.getNotCntFee());
            map.put("pgRevCommiRate", dto.getPgRevCommiRate());
            map.put("fingerRevCommiRate", dto.getFingerRevCommiRate());
            map.put("chaCloseChk", dto.getChaCloseChk());
            map.put("chaCloseSt", dto.getChaCloseSt());
            map.put("chaCloseVarReson", dto.getChaCloseVarReson());
            map.put("chast", dto.getChast());
            map.put("rcpCntFee", dto.getRcpCntFee());
            map.put("rcpBnkFee", dto.getRcpBnkFee());
            map.put("chaStatus", dto.getChaStatus());
            map.put("chaType", dto.getChaType());
            map.put("amtChkTy", dto.getAmtChkTy());
            map.put("rcpDueChk", dto.getRcpDueChk());
            map.put("partialPayment", dto.getPartialPayment());
            map.put("rcpReqYn", dto.getRcpReqYn());
            map.put("rcpReqTy", dto.getRcpReqTy());
            map.put("mnlRcpReqTy", dto.getMnlRcpReqTy());
            map.put("noTaxYn", dto.getNoTaxYn());
            map.put("rcpReqSveTy", dto.getRcpReqSveTy());
            map.put("cusNameTy", dto.getCusNameTy());
            map.put("mandRcpYn", dto.getMandRcpYn());
            map.put("concurrentBlockYn", dto.getConcurrentBlockYn());
        }else{
            map.put("bnkCd", dto.getBnkCd());
            map.put("finFeeAccNo", dto.getFinFeeAccNo());
            map.put("finFeeAccIdent", dto.getFinFeeAccIdent());
            map.put("finFeeAccOwner", dto.getFinFeeAccOwner());
            map.put("finFeeOwnNo", dto.getFinFeeOwnNo());
            map.put("finFeePayTy", dto.getFinFeePayTy());
        }
        // 작업타입 정의
        map.put("jobType", dto.getJobType());

		if(dto.getJobType().equals("N")){
			sqlSession.update("BankMgmtDao.updateNewXChaList", map);
		}else{
			sqlSession.update("BankMgmtDao.updateXChaList", map);
		}

	}

    @Override
    public void updateChaListInfo(HashMap<String, Object> map) throws Exception {
        sqlSession.update("BankMgmtDao.updateChaListInfo", map);
    }
	
	@Override
	public void insertJobhistory(HashMap<String, Object> map) throws Exception {
		sqlSession.update("BankMgmtDao.insertJobhistory", map);
	}

	@Override
	public void updateWebuser(SysChaMgmtDTO dto) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		map.put("chacd", dto.getChaCd());
		map.put("chast", dto.getChast());
		
		sqlSession.update("BankMgmtDao.updateWebuser", map);
		
	}

	@Override
	public List<KftcCode> getBnkList(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectList("BankMgmtDao.getBnkList", paramMap);
	}

    @Override
    public String insertXchaList(SysChaMgmtDTO dto) throws Exception {
        sqlSession.insert("BankMgmtDao.insertXchalist", dto);
        return dto.getChaCd();
    }

    @Override
    public void updateXchaList(SysChaMgmtDTO dto) throws Exception {
        sqlSession.update("BankMgmtDao.updateXchaList", dto);
    }

}
