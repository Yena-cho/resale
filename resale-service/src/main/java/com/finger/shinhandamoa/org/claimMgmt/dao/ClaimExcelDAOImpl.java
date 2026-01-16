package com.finger.shinhandamoa.org.claimMgmt.dao;

import com.finger.shinhandamoa.common.CmmnUtils;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
@Repository
@Slf4j
public class ClaimExcelDAOImpl implements ClaimExcelDAO {

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
	public List<ClaimDTO> excelList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selExcelItemSample", map);
	}

	@Override
	public List<ClaimDTO> excelBeforeList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selExcelBeforeSample", map);
	}

	@Override
	public void claimFailDelete(String chaCd) throws Exception {
		sqlSession.delete("ClaimExcelDao.claimFailDelete", chaCd);
	}

	@Override
	public void claimExcelFailInsert(Map<String, Object> map) throws Exception {
		sqlSession.insert("ClaimExcelDao.claimExcelFailInsert", map);
	}

	@Override
	public List<ClaimDTO> failList(String chaCd, int start, int end) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chaCd", chaCd);
		map.put("start", start);
		map.put("end", end);

		return sqlSession.selectList("ClaimExcelDao.selectFailList", map);
	}

	@Override
	public List<ClaimDTO> selectClaimFailMasterExcel(String chaCd) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selectClaimFailMasterExcel", chaCd);
	}

	@Override
	public List<ClaimItemDTO> selectFailDetailsForExcel(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selectFailDetailsForExcel", map);
	}

	@Override
	public int failTotalCount(String chaCd) throws Exception {
		return sqlSession.selectOne("ClaimExcelDao.failTotalCount", chaCd);
	}

	@Override
	public List<ClaimDTO> failExcelList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selExcelFailData", map);
	}

	@Override
	public String selClaimConfirm(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimExcelDao.selClaimConfirm", map);
	}

	@Override
	public List<ClaimDTO> excelSave(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selectClaimExcel", map);
	}

	@Override
	public String selNotiMasCd(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimExcelDao.selectNotiMasCd", map);
	}

	@Override
	public int selPcpGubn(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimExcelDao.selPcpGubn", map);
	}

	@Override
	public List<ClaimDTO> excelItemList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.excelItemList", map);
	}

	@Override
	public List<ClaimDTO> excelCusList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.excelCusList", map);
	}

	@Override
	public List<ClaimDTO> selExcelBeforeMonth(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selExcelBeforeMonth", map);
	}
	
	private void bulkUploadExcel_selectNotiDet(Connection conn) {
		String info = "SELECT A.CUSHP, A.CUSMAIL, A.CUSOFFNO, A.DISABLED, A.RCPGUBN, A.VANO, A.CUSKEY"
				+ "	,  A.CUSGUBN1,  A.CUSGUBN2,  A.CUSGUBN3,  A.CUSGUBN4, A.SMSYN, A.MAILYN, D.AMTCHKTY"
				+ "	,  NVL(B.CONYN, 'N') AS CONYN, NVL(C.CNT, 0) AS CNT, ? AS PAYITEMAMT"
				+ "  FROM XCUSMAS A, (SELECT CASE WHEN COUNT(*) > 0 THEN 'Y' ELSE 'N' END AS CONYN, MAS.VANO 	"
				+ "				     FROM XNOTIMAS 	MAS, XNOTIDET 	DET										"
				+ "					WHERE MAS.NOTIMASCD   = DET.NOTIMASCD									"
				+ "					  AND MAS.NOTI_CAN_YN = DET.NOTI_CAN_YN									"
				+ "					  AND MAS.NOTI_CAN_YN = 'N'												"
				+ "					  AND MAS.CHACD 	  = ?												"
				+ "					  AND MAS.MASMONTH    = ?												"
				+ "					  AND MAS.VANO	      = ?												"
				+ "					  AND DET.PAYITEMCD   = ?												"
				+ "					  AND MAS.NOTIMASST   > 'PA00'											"
				+ "					GROUP BY MAS.VANO) B													"
				+ "    ,  (SELECT COUNT(*) AS CNT, MAS.VANO FROM XNOTIMAS MAS, XNOTIDET DET					"
				+ "		 WHERE MAS.NOTIMASCD 	= DET.NOTIMASCD												"
				+ "		   AND MAS.NOTI_CAN_YN = DET.NOTI_CAN_YN											"
				+ "		   AND MAS.NOTI_CAN_YN = 'N'														"
				+ "		   AND MAS.CHACD 		= ?															"
				+ "		   AND MAS.VANO 		= ?															"
				+ "		   AND MAS.MASMONTH 	= ?															"
				+ "		 GROUP BY MAS.VANO) C																"
				+ "	,	XCHALIST D																			"
				+ " WHERE A.VANO  = B.VANO(+)																	"
				+ "   AND A.VANO  = C.VANO(+)																	"
				+ "   AND A.CHACD = D.CHACD																	"
				+ "   AND A.VANO  = ?																			";
	}
	
	private void bulkUploadExcel_insert1(Connection conn, String chaCd, String xRow, String masMonth, String cusKey, String cusName, String startDate, String endDate, String printDate, int xCount, String xAmt, String retVal, String payItemCd, String remark, String remark1, String remark2, String remark3, String cusHp, String cusMail, String cusOffNo) {
		String fail = "INSERT INTO XNOTIMASFAIL(CHACD, XROW, MASMONTH, CUSKEY, CUSNAME, CUSHP, CUSMAIL, CUSOFFNO	"
				+ "					,    STARTDATE, ENDDATE, PRINTDATE, XCOUNT, XAMT, RESULT, MAKEDT		"
				+ "					, 	 MAKER, REGDT, PAYITEMCD, PTRITEMREMARK, REMARK)			"
				+ "			   VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?, ?, ?)		";

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(fail);

			pstmt.setString(1, chaCd);
			pstmt.setString(2, xRow);
			pstmt.setString(3, masMonth);
			pstmt.setString(4, cusKey);
			pstmt.setString(5, cusName);
			pstmt.setString(6, StringUtils.defaultString(cusHp));
			pstmt.setString(7, StringUtils.defaultString(cusMail));
			pstmt.setString(8, StringUtils.defaultString(cusOffNo));
			pstmt.setString(9, startDate);
			pstmt.setString(10, endDate);
			pstmt.setString(11, printDate);
			pstmt.setInt(12, xCount);
			pstmt.setString(13, xAmt);
			pstmt.setString(14, retVal);
			pstmt.setString(15, chaCd);
			pstmt.setString(16, payItemCd);
			pstmt.setString(17, remark);
			pstmt.setString(18, StringUtils.joinWith("\n", remark1, remark2, remark3));

			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
	
	private void bulkUploadExcel_updateNotiMas(Connection conn, String masMonth, String startDate, String endDate, String printDate, String date, String remark1, String remark2, String remark3, String chaCd, String notiMasCd) {
		String upMas = "UPDATE XNOTIMAS 																	"
				+ "   SET MASMONTH  = ?																		"
				+ "	 , MASDAY	 = TO_CHAR(now(), 'YYYYMMDD')												"
				+ "	 , STARTDATE = ?																		"
				+ "	 , ENDDATE	 = ?																		"
				+ "	 , PRINTDATE = COALESCE (?, ?)																"
				+ "	 , REMARK	 = TRIM(RPAD(?, 120, ' ')) || CHR(10) || TRIM(RPAD(?, 120, ' ')) || CHR(10) || TRIM(RPAD(?, 120, ' '))	"
				+ "	 , MAKEDT 	 = now()																	"
				+ "	 , MAKER	 = ?																		"
				+ " WHERE NOTIMASCD = ? 																		";

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(upMas);
			pstmt.setString(1, masMonth);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			pstmt.setString(4, printDate);
			pstmt.setString(5, endDate);
			pstmt.setString(6, remark1);
			pstmt.setString(7, remark2);
			pstmt.setString(8, remark3);
			pstmt.setString(9, chaCd);
			pstmt.setString(10, notiMasCd);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	private void bulkUploadExcel_updateNotiDet(Connection conn, String xAmt, String remark, String chaCd, String notidetcd) {
		String upDet = "UPDATE XNOTIDET 																			"
				+ "   SET PAYITEMAMT  = ?																		"
				+ "	,  PTRITEMREMARK = TRIM(RPAD(?, 50, ' '))												"
				+ "	,  MAKEDT	   = now()																"
				+ "	,  MAKER  	   = ?																		"
				+ " WHERE NOTIDETCD   = ?																		";

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(upDet);

			pstmt.setInt(1, Integer.parseInt(xAmt));
			pstmt.setString(2, remark);
			pstmt.setString(3, chaCd);
			pstmt.setString(4, notidetcd);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
	}


	private void bulkUploadExcel_insertNotiDet(Connection conn, String notidetcd, String notiMasCd, String payItemCd, String adjfiregkey, String payitemname, String xAmt, String payitemselyn, String rcpitemyn, String chaoffno, String remark, String ptritemorder, String chaCd, String ptritemname) {
		String inDet = "INSERT INTO XNOTIDET(NOTIDETCD, 	NOTIMASCD, 		PAYITEMCD, 	ADJFIREGKEY, PAYITEMNAME 	"
				+ "				   , PAYITEMAMT,	PAYITEMSELYN, 	RCPITEMYN,	CHAOFFNO,	 PTRITEMREMARK 	"
				+ "				   , PTRITEMORDER,	NOTIDETST,	MAKEDT,		 MAKER		 					"
				+ "				   , REGDT,			CHATRTY,		NOTI_CAN_YN, PTRITEMNAME) 				"
				+ "			VALUES(COALESCE (?, nextval('seqxnotidetcd')::varchar), ?, ?, ?, ?, ?, ?, ?, ?   	 				"
				+ "				   ,  TRIM(RPAD(?, 50, ' ')), ?, 'PA00', now(), ?, now(), '01', 'N', ?)	";


		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(inDet);

			pstmt.setString(1, notidetcd);
			pstmt.setString(2, notiMasCd);
			pstmt.setString(3, payItemCd);
			pstmt.setString(4, adjfiregkey);
			pstmt.setString(5, payitemname);
			pstmt.setInt(6, Integer.parseInt(xAmt));
			pstmt.setString(7, payitemselyn);
			pstmt.setString(8, rcpitemyn);
			pstmt.setString(9, chaoffno);
			pstmt.setString(10, remark);
			pstmt.setString(11, ptritemorder);
			pstmt.setString(12, chaCd);
			pstmt.setString(13, ptritemname);

			log.warn("{}", pstmt);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	private Map<String, String> bulkUploadExcel_selectNotiDet2(Connection con, String chaCd, String payItemCd, String notiMasCd) {
		String seDet = "SELECT B.NOTIDETCD,    A.PAYITEMCD, A.ADJFIREGKEY,  A.PAYITEMNAME  							"
				+ " 	,  A.PAYITEMSELYN, A.RCPITEMYN, A.PTRITEMORDER, A.CHAOFFNO,   A.PTRITEMNAME 			"
				+ "  FROM  XPAYITEM A"
				+ "  left outer join XNOTIDET B on A.PAYITEMCD    = B.PAYITEMCD and B.NOTIMASCD = ?									"
				+ " WHERE 														"
				+ "   A.CHACD 		  = ? 																	"
				+ "   AND  A.PAYITEMCD 	  = ? 																	";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String> resultMap = new HashMap<>();
		try {
			pstmt = con.prepareStatement(seDet);

			pstmt.setString(1, notiMasCd);
			pstmt.setString(2, chaCd);
			pstmt.setString(3, payItemCd);

			rs = pstmt.executeQuery();

			if(rs.next()) {
				resultMap.put("NOTIDETCD", rs.getString("notidetcd"));
				resultMap.put("ADJFIREGKEY", rs.getString("adjfiregkey"));
				resultMap.put("PAYITEMNAME", rs.getString("payitemname"));
				resultMap.put("PAYITEMSELYN", rs.getString("payitemselyn"));
				resultMap.put("RCPITEMYN", rs.getString("rcpitemyn"));
				resultMap.put("CHAOFFNO", rs.getString("chaoffno"));
				resultMap.put("PTRITEMORDER", rs.getString("ptritemorder"));
				resultMap.put("PTRITEMNAME", rs.getString("ptritemname"));
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}

			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
		
		return resultMap;
	}

	private void bulkUploadExcel_insertNotiMas(Connection conn, String notiMasCd, String chaCd, String vano, String masMonth, String cuskey, String cusgubn1, String cusgubn2, String cusgubn3, String cusgubn4, String cusName, String cushp, String cusmail, String smsyn, String mailyn, String cusoffno, String startDate, String endDate, String printDate, String amtchkty, String disabled, String remark1, String remark2, String remark3) {
		String inMas = "INSERT INTO XNOTIMAS(NOTIMASCD, CHACD, 	     VANO, 	 	MASMONTH,  MASDAY 					"
				+ "				   , CUSKEY, 	CUSGUBN1,    CUSGUBN2, 	CUSGUBN3,  CUSGUBN4 				"
				+ "				   , CUSNAME, 	CUSHP, 	     CUSMAIL,  	SMSYN,	    MAILYN					"
				+ "				   , CUSOFFNO,	STARTDATE,   ENDDATE,	PRINTDATE, AMTCHKTY 				"
				+ "				   , NOTIMASST,	MAKEDT,	     MAKER,	 	REGDT,	" +
				"	CHATRTY 				"
				+ "				   , DISABLED,	NOTI_CAN_YN, REMARK)										"
				+ "			VALUES(?, ?, ?, ?, TO_CHAR(now(), 'YYYYMMDD'), ?, ?, ?, ?, ?					"
				+ "				   , ?, ?, ?, ?, ?, ?, ?, ?, COALESCE (?, ?), ?, 'PA00',now() 					"
				+ "				   ,?, now(), '01', ?, 'N'												"
				+ "				   , TRIM(RPAD(?, 120, ' ')) || CHR(10) || TRIM(RPAD(?, 120, ' ')) || CHR(10) || TRIM(RPAD(?, 120, ' ')))	";

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(inMas);
			pstmt.setString(1, notiMasCd);
			pstmt.setString(2, chaCd);
			pstmt.setString(3, vano);
			pstmt.setString(4, masMonth);
			pstmt.setString(5, cuskey);
			pstmt.setString(6, cusgubn1);
			pstmt.setString(7, cusgubn2);
			pstmt.setString(8, cusgubn3);
			pstmt.setString(9, cusgubn4);
			pstmt.setString(10, cusName);
			pstmt.setString(11, cushp);
			pstmt.setString(12, cusmail);
			pstmt.setString(13, smsyn);
			pstmt.setString(14, mailyn);
			pstmt.setString(15, cusoffno);
			pstmt.setString(16, startDate);
			pstmt.setString(17, endDate);
			pstmt.setString(18, printDate);
			pstmt.setString(19, endDate);
			pstmt.setString(20, amtchkty);
			pstmt.setString(21, chaCd);
			pstmt.setString(22, disabled);
			pstmt.setString(23, remark1);
			pstmt.setString(24, remark2);
			pstmt.setString(25, remark3);

			log.warn("{}", pstmt);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
	
	private String bulkUploadExcel_generateNotiMasCd(Connection conn) {
		String masNv = "SELECT	NEXTVAL('seqxnotimascd') AS notimascd									";

		String notiMasCd = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(masNv);
			rs = pstmt.executeQuery();

			if (rs.next()) {
                notiMasCd = rs.getString("notimascd");
            }
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
		
		return notiMasCd;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int bulkUploadExcel(List<Map<String, Object>> list, int rowNo, int rowNum) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null, pstmt2 = null, pstmt3 = null, pstmt5 = null;
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;

		String info = "SELECT A.CUSHP, A.CUSMAIL, A.CUSOFFNO, A.DISABLED, A.RCPGUBN, A.VANO, A.CUSKEY"
				+ "	,  A.CUSGUBN1,  A.CUSGUBN2,  A.CUSGUBN3,  A.CUSGUBN4, A.SMSYN, A.MAILYN, D.AMTCHKTY"
				+ "	,  COALESCE (B.CONYN, 'N') AS CONYN, COALESCE(C.CNT, 0) AS CNT, ? AS PAYITEMAMT"
				+ "  FROM XCUSMAS A "
				+ "  left outer join (SELECT 'N' AS CONYN, MAS.VANO 	"
				+ "				     FROM XNOTIMAS 	MAS, XNOTIDET 	DET										"
				+ "					WHERE MAS.NOTIMASCD   = DET.NOTIMASCD									"
				+ "					  AND MAS.NOTI_CAN_YN = DET.NOTI_CAN_YN									"
				+ "					  AND MAS.NOTI_CAN_YN = 'N'												"
				+ "					  AND MAS.CHACD 	  = ?												"
				+ "					  AND MAS.MASMONTH    = ?												"
				+ "					  AND MAS.VANO	      = ?												"
				+ "					  AND DET.PAYITEMCD   = ?												"
				+ "					  AND MAS.NOTIMASST   = 'PA00'											"
				+ "					GROUP BY MAS.VANO) B on A.VANO  = B.VANO								"
				+ "  left outer join (SELECT COUNT(*) AS CNT, MAS.VANO FROM XNOTIMAS MAS, XNOTIDET DET		"
				+ "		 WHERE MAS.NOTIMASCD 	= DET.NOTIMASCD												"
				+ "		   AND MAS.NOTI_CAN_YN = DET.NOTI_CAN_YN											"
				+ "		   AND MAS.NOTI_CAN_YN = 'N'														"
				+ "		   AND MAS.CHACD 		= ?															"
				+ "		   AND MAS.VANO 		= ?															"
				+ "		   AND MAS.MASMONTH 	= ?															"
				+ "		 GROUP BY MAS.VANO) C on A.VANO  = C.VANO											"
				+ "	 inner join	XCHALIST D on A.CHACD = D.CHACD												"
				+ " WHERE 																"
				+ "    A.VANO  = ?																		"
				+ "   AND A.CHACD  = ?																		"
				+ "	  AND A.DISABLED = 'N'																	";

		String masCd = "SELECT	NOTIMASCD 																			"
				+ "FROM	XNOTIMAS																			"
				+ "WHERE 	CHACD 	  	= ?																		"
				+ "AND 	MASMONTH  	= ?																		"
				+ "AND 	VANO 	  	= ?																		"
				+ "AND		NOTIMASST 	< 'PA02' 																"
				+ "AND 	NOTI_CAN_YN = 'N'																	";


		int failCnt = 0;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.setAutoCommit(false);

			String retVal = "";

			for(Map<String, Object> map : list) {
				String remark1 = (String) map.get("remark1");
				if ("null".equals(remark1) || remark1 == null) {
					remark1 = StringUtils.EMPTY;
				}

				String remark2 = (String) map.get("remark2");
				if ("null".equals(remark2) || remark2 == null) {
					remark2 = StringUtils.EMPTY;
				}

				String remark3 = (String) map.get("remark3");
				if ("null".equals(remark3) || remark3 == null) {
					remark3 = StringUtils.EMPTY;
				}

				String remark = (String) map.get("remark");
				if ("null".equals(remark) || remark == null) {
					remark = StringUtils.EMPTY;
				}

				String xAmt = (String) map.get("xAmt");
				String chaCd = (String) map.get("chaCd");
				String masMonth = (String) map.get("masMonth");
				String vano = (String) map.get("vano");
				String payItemCd = (String) map.get("payItemCd");
				String result = (String) map.get("result");
				String xRow = (String) map.get("xRow");
				String cusKey = (String) map.get("cusKey");
				String cusName = (String) map.get("cusName");
				String startDate = (String) map.get("startDate");
				String endDate = (String) map.get("endDate");
				String printDate = (String) map.get("printDate");
				String cusGubn1 = (String) map.get("cusGubn1");
				String cusGubn2 = (String) map.get("cusGubn2");
				String cusGubn3 = (String) map.get("cusGubn3");
				String cusGubn4 = (String) map.get("cusGubn4");
				int xCount = (int) map.get("xCount");

				/**
				 * 고객 정보 확인
				 */
				pstmt = con.prepareStatement(info);
				pstmt.setString(1, xAmt);
				pstmt.setString(2, chaCd);
				pstmt.setString(3, masMonth);
				pstmt.setString(4, vano);
				pstmt.setString(5, payItemCd);
				pstmt.setString(6, chaCd);
				pstmt.setString(7, vano);
				pstmt.setString(8, masMonth);
				pstmt.setString(9, vano);
				pstmt.setString(10, chaCd);

				rs = pstmt.executeQuery();

				if(!rs.next()) {
					/**
					 * 고객 정보가 없으면 엑셀 등록 실패 건 저장
					 */
					failCnt++;
					if(StringUtils.isNotEmpty(result)) {
						retVal = result;
					}
					retVal = StringUtils.defaultIfEmpty(retVal, "고객정보 또는 가상계좌 없음");
					map.put("result", retVal);

					bulkUploadExcel_insert1(con, chaCd, xRow, masMonth, cusKey, cusName, startDate, endDate, printDate, xCount, xAmt, retVal, payItemCd, remark, remark1, remark2, remark3, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
					
					continue;
				}

				/**
				 * 엑셀 등록 시 확인할 것. 엑셀 실패 사유에 출력
				 *
				 * TODO : 비고 25자 확인, 청구건수 확인, 추가안내사항 80자 확인
				 */
				if ("Y".equals(rs.getString("disabled"))) {
					retVal = "정지 또는 해지 고객";
				} else if ("N".equals(rs.getString("rcpgubn"))) {
					retVal = "납부제외고객";
				} else if ("Y".equals(rs.getString("conyn"))) {
					retVal = "청구항목 중복 청구건";
				} else if (!"".equals((String) map.get("result"))) {
					retVal = result;
				}
                map.put("result", retVal);

				if (StringUtils.isNotBlank(retVal)) {
					/**
					 * 엑셀 등록 실패
					 */
					failCnt++;
					
					bulkUploadExcel_insert1(con, chaCd, xRow, masMonth, cusKey, cusName, startDate, endDate, printDate, xCount, xAmt, retVal, payItemCd, remark, remark1, remark2, remark3, rs.getString("CUSHP"), rs.getString("CUSMAIL"), rs.getString("CUSOFFNO"));
					continue;
				}
				
				/**
				 * 엑셀 등록 성공
				 */
				pstmt2 = con.prepareStatement(masCd);
				pstmt2.setString(1, (String) map.get("chaCd"));
				pstmt2.setString(2, (String) map.get("masMonth"));
				pstmt2.setString(3, (String) map.get("vano"));

				String notiMasCd = "";
				rs1 = pstmt2.executeQuery();
				if (rs1.next()) {
					/**
					 * 기존에 존재하는 청구 건
					 */
					notiMasCd = rs1.getString("notimascd");
					log.warn("bulkUploadExcel_updateNotiMas {}", notiMasCd );
					bulkUploadExcel_updateNotiMas(con, masMonth, startDate, endDate, printDate, endDate, remark1, remark2, remark3, chaCd, notiMasCd);
				} else {
					/**
					 * 신규로 등록하는 청구 건
					 */
					notiMasCd = bulkUploadExcel_generateNotiMasCd(con);
					if(CmmnUtils.empty(cusGubn1) && CmmnUtils.empty(cusGubn2) && CmmnUtils.empty(cusGubn3) && CmmnUtils.empty(cusGubn4)){
						bulkUploadExcel_insertNotiMas(con, notiMasCd, chaCd, vano, masMonth, rs.getString("cuskey"), rs.getString("cusgubn1"), rs.getString("cusgubn2"), rs.getString("cusgubn3"), rs.getString("cusgubn4"), cusName, rs.getString("cushp"), rs.getString("cusmail"), rs.getString("smsyn"), rs.getString("mailyn"), rs.getString("cusoffno"), startDate, endDate, printDate, rs.getString("amtchkty"), rs.getString("disabled"), remark1, remark2, remark3);
					}else{
						log.warn("bulkUploadExcel_insertNotiMas {}", notiMasCd );
						bulkUploadExcel_insertNotiMas(con, notiMasCd, chaCd, vano, masMonth, rs.getString("cuskey"), cusGubn1,cusGubn2,cusGubn3,cusGubn4, cusName, rs.getString("cushp"), rs.getString("cusmail"), rs.getString("smsyn"), rs.getString("mailyn"), rs.getString("cusoffno"), startDate, endDate, printDate, rs.getString("amtchkty"), rs.getString("disabled"), remark1, remark2, remark3);
					}
				}
				
				Map<String, String> notiDetMap = bulkUploadExcel_selectNotiDet2(con, chaCd, payItemCd, notiMasCd);
				
				if (!notiDetMap.isEmpty()) {
					String notiDetMap_notiDetCd = notiDetMap.get("NOTIDETCD");
					String notiDetMap_ADJFIREGKEY = notiDetMap.get("ADJFIREGKEY");
					String notiDetMap_PAYITEMNAME = notiDetMap.get("PAYITEMNAME");
					String notiDetMap_payitemselyn = notiDetMap.get("PAYITEMSELYN");
					String notiDetMap_rcpitemyn = notiDetMap.get("RCPITEMYN");
					String notiDetMap_chaoffno = notiDetMap.get("CHAOFFNO");
					String notiDetMap_ptritemorder = notiDetMap.get("PTRITEMORDER");
					String notiDetMap_ptritemname = notiDetMap.get("PTRITEMNAME");
					
					if (StringUtils.isNotEmpty(notiDetMap_notiDetCd)) {
						log.warn("bulkUploadExcel_updateNotiDet {}", notiDetMap_notiDetCd);
						bulkUploadExcel_updateNotiDet(con, xAmt, remark, chaCd, notiDetMap_notiDetCd);
					} else {
						log.warn("bulkUploadExcel_insertNotiDet {}", notiDetMap_notiDetCd);
						bulkUploadExcel_insertNotiDet(con, notiDetMap_notiDetCd, notiMasCd, payItemCd, notiDetMap_ADJFIREGKEY, notiDetMap_PAYITEMNAME, xAmt, notiDetMap_payitemselyn, notiDetMap_rcpitemyn, notiDetMap_chaoffno, remark, notiDetMap_ptritemorder, chaCd, notiDetMap_ptritemname);
					}
				}
			}

//			if (rowNo == rowNum) {
				con.commit();
//			}
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
		} finally {
			if (pstmt  != null) try {pstmt.close();  pstmt  = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (pstmt2 != null) try {pstmt2.close(); pstmt2 = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (pstmt3 != null) try {pstmt3.close(); pstmt3 = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (pstmt5 != null) try {pstmt5.close(); pstmt5 = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (con    != null) try {con.close();    con    = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (rs     != null) try {rs.close();     rs     = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (rs1    != null) try {rs1.close();    rs1    = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (rs2    != null) try {rs2.close();    rs2    = null;} catch(SQLException ex){log.error(ex.getMessage());}
			if (rs3    != null) try {rs3.close();    rs3    = null;} catch(SQLException ex){log.error(ex.getMessage());}
		}
		return failCnt;
	}

	@Override
	public List<ClaimItemDTO> selectDetailsForExcel(String notimasCd) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selectDetailsForExcel", notimasCd);
	}

	@Override
	public List<ClaimDTO> excelMasterList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("ClaimExcelDao.selectClaimMasterExcel", map);
	}

	@Override
	public ClaimDTO listForExcelTrans(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("ClaimExcelDao.listForExcelTrans", map);
	}
}
