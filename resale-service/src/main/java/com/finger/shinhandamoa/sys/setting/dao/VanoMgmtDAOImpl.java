package com.finger.shinhandamoa.sys.setting.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.finger.shinhandamoa.sys.setting.dto.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;

import kr.co.finger.damoa.commons.biz.VirtualAccountNoChecker;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository

public class VanoMgmtDAOImpl implements VanoMgmtDAO {

	private static final Logger logger = LoggerFactory.getLogger(VanoMgmtDAOImpl.class);

	@Inject
	private SqlSession sqlSession;

	@Value("${jdbc.driver}")
	private String driver;
	
	@Value("${jdbc.url}")
	private String url;
	
	@Value("${jdbc.username}")
	private String username;
	
	@Value("${jdbc.password}")
	private String password;

	@Override
	public VanoMgmtDTO getSysMainInfo01(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.getSysMainInfo01", map);
	}

	@Override
	public int getVano01ListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.getVano01ListTotalCount", map);
	}

	@Override
	public List<VanoMgmtDTO> getVano01ListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getVano01ListAll", map);
	}

	@Override
	public List<VanoMgmtDTO> getVano01ListExcel(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysVanoExcelDao.getVano01ListExcel", map);
	}

	@Override
	public List<VanoMgmtDTO> getVanoIssuedListExcel(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysVanoExcelDao.getVanoIssuedListExcel", map);
	}

	@Override
	public List<VanoMgmt2DTO> getChaVanoListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getChaVanoListTotalCount", map);
	}

	@Override
	public List<VanoMgmt2DTO> getChaVanoListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getChaVanoListAll", map);
	}

	@Override
	public List<VanoMgmt2DTO> getChaVanoListExcel(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysVanoExcelDao.getChaVanoListExcel", map);
	}

	@Override
	public int getVanoTranHisListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.getVanoTranHisListTotalCount", map);
	}

	@Override
	public List<VanoMgmtDTO> getVanoTranHisListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getVanoTranHisListAll", map);
	}

	@Override
	public List<VanoMgmtDTO> getVanoTranHisListExcel(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("SysVanoExcelDao.getVanoTranHisListExcel", map);
	}	
	
	@Override
	public int getVanoIssuedListTotalCount(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.getVanoIssuedListTotalCount", map);
	}

	@Override
	public List<VanoMgmtDTO> getMisassignVanoCount() throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getMisassignVanoCount");
	}

	@Override
	public List<VanoMgmtDTO> getVanoIssuedListAll(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getVanoIssuedListAll", map);
	}


	/**
	 *
	 * @param map
	 * @throws Exception
	 * Exception 터지면 롤백
	 *
	 */
	@Transactional
	@Override
	public void insertVirtualAccount(HashMap<String, Object> map) throws Exception {

		sqlSession.insert("VanoMgmtDao.insertVirtualAccount", map);
	}


	@Override
	public void deleteVirtualAccount() throws Exception {

		sqlSession.delete("VanoMgmtDao.deleteVirtualAccount");
	}

	
	@Transactional
	@Override
	public void insertValist(HashMap<String, Object> map) throws Exception {
		
		Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO VALIST(FICD, VANO, FITXCD, CHACD, USEYN, REGDT, VAREGTY) VALUES(?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISSsss'), ?, ?, SYSDATE, ?)";

        try {
        	Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            pstmt = con.prepareStatement(sql) ;
            con.setAutoCommit(false);

            String startAccount = (String)map.get("startAccount");
            String startAcc = startAccount.substring(0, startAccount.length() - 1);
            int    acccnt = (Integer)map.get("acccnt");

    		for(int i = 0; i < acccnt; i++) {
    			long   idx  = Long.parseLong(startAcc);
    			String vano = Long.toString(idx+i);
    			String val  = VirtualAccountNoChecker.find14CrcNo(vano);

    			pstmt.setString(1, (String) map.get("ficd"));		// FICD
    			pstmt.setString(2, vano+val);						// VANO
    			pstmt.setString(3, (String) map.get("chacd"));		// CHACD
    			pstmt.setString(4, (String) map.get("useyn"));		// USEYN
    			pstmt.setString(5, (String) map.get("varegty"));	// VAREGTY

    			// addBatch에 담기
                pstmt.addBatch();
                // 파라미터 Clear
                pstmt.clearParameters();
                // Batch 실행
                pstmt.executeBatch();
                // Batch 초기화
                pstmt.clearBatch();
            }

            pstmt.executeBatch();
            con.commit();

        } catch(Exception e) {
            e.printStackTrace();

            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (pstmt != null) try {pstmt.close();pstmt = null;} catch(SQLException ex){}
            if (con != null)   try {con.close();  con   = null;} catch(SQLException ex){}
        }
    }

	@Override
	public void doVanoIssueReq(HashMap<String, Object> map) throws Exception {
		sqlSession.update("VanoMgmtDao.doVanoIssueReq", map);
	}

	@Override
	public HashMap<String, Object> doVanoIssueReqAfterCount(HashMap<String, Object> map) throws Exception {
		return  sqlSession.selectOne("VanoMgmtDao.doVanoIssueReqAfterCount", map);
	}

	@Override
	public HashMap<String, Object> doVanoIssueReqAfterList(HashMap<String, Object> map) throws Exception {
		return  sqlSession.selectOne("VanoMgmtDao.doVanoIssueReqAfterList", map);
	}

	@Override
	public void doVanoIssueReqAfterInsert(VanoReqDTO vanoReqDTO) throws Exception {

		sqlSession.insert("VanoMgmtDao.doVanoIssueReqAfterInsert", vanoReqDTO);

	}

	@Override
	public void csvUploadResultInsert(HashMap<String, Object> map) throws Exception {
		sqlSession.update("VanoMgmtDao.csvUploadResultInsert", map);
	}

	@Override
	public void csvUploadResultInsertAfter(HashMap<String, Object> map) throws Exception {
		sqlSession.update("VanoMgmtDao.csvUploadResultInsertAfter", map);
	} //csv 업로드 후 후처리

	@Override
	public int getVanoTranChkListCnt(HashMap<String, Object> reqMap) {
		return sqlSession.selectOne("VanoMgmtDao.getVanoTranChkListCnt", reqMap);
	}

	@Override
	public List<TransInfoDTO> getVanoTranChkList(HashMap<String, Object> reqMap) {
		return sqlSession.selectList("VanoMgmtDao.getVanoTranChkList", reqMap);
	}

	@Override
	public int getVanoTranInitChkCnt(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.getVanoTranInitChkCnt", reqMap);
	}

	@Override
	public List<TransInfoDTO> getVanoTranInitChk(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.getVanoTranInitChk", reqMap);
	}


	@Override
	public int getTempVanoAccount(HashMap<String, Object> reqMap) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.getTempVanoAccount", reqMap);
	}

	@Override
	public int doVanoIssueReqCheck(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.doVanoIssueReqCheck", map);
	}


	@Override
	public int pgVanoSendListCnt(VanoPgDTO dto) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.pgVanoSendListCnt", dto);
	}

	@Override
	public List<VanoPgDTO> pgVanoSendList(VanoPgDTO dto) throws Exception {
		return sqlSession.selectList("VanoMgmtDao.pgVanoSendList", dto);
	}

	@Override
	public void updateSendListStat(Map<String, Object> map) throws Exception {
		sqlSession.update("VanoMgmtDao.updateSendListStat", map);
	} //csv 업로드 후 후처리

	@Override
	public int pgVanoCheckCnt(VanoPgDTO dto) throws Exception {
		return sqlSession.selectOne("VanoMgmtDao.pgVanoCheckCnt", dto);
	}



}
