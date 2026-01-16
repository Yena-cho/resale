package com.finger.shinhandamoa.home.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.main.dto.NoticeDTO;

@Repository
public class DataDAOImpl implements DataDAO {

	private  static final Logger logger = LoggerFactory.getLogger(DataDAOImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private ShaEncoder shaEncoder;
	
	@Value("${jdbc.driver}")
	private String driver;
	
	@Value("${jdbc.url}")
	private String url;
	
	@Value("${jdbc.username}")
	private String username;
	
	@Value("${jdbc.password}")
	private String password;
	
	@Override
	public void cusInfoShaEncoder(HashMap<String, Object> map) throws Exception {
		Connection con = null;
        PreparedStatement pstmt = null;
        
        String sql = "UPDATE WEBUSER SET LOGINPW = ? WHERE LOGINID = ?";
        
        try {
        	Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            pstmt = con.prepareStatement(sql) ;
            con.setAutoCommit(false);
            
            pstmt.setString(1, (String)map.get("loginPw"));
            pstmt.setString(2, (String)map.get("loginId"));

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
	
	@Transactional
	@Override
	public void orgPasswordUpdate(HashMap<String, Object> map) throws Exception {
		/* 20180703 : hwLee start */
		Connection con = null;
        PreparedStatement pstmt = null, pstmt1 = null;
        ResultSet rs = null;
        
        /*
        String sql 	= "SELECT A.CHACD                                                         AS CHACD  \n"
        			+ "      ,A.CHACD || '_' || substr(A.CHAOFFNO, length(A.CHAOFFNO) - 3, 4) AS PW     \n"
        			+ "FROM   XCHALIST A                                                                \n"
        			+ "      ,WEBUSER  B                                                                \n"
        			+ "WHERE  A.CHACD  = B.CHACD                                                        \n"
        			//+ "AND    A.CHACD IN (                                                              \n"
        			//+ "                   '20004467'                                                    \n"
        			//+ "                  ,'20004082'                                                    \n"
        			//+ "                  ,'20003484'                                                    \n"
        			//+ "                  ,'20005660'                                                    \n"
        			//+ "                  ,'20003402'                                                    \n"
        			//+ "                  ,'20002983'                                                    \n"
        			//+ "                  ,'20001749'                                                    \n"
        			//+ "                  ,'20001591'                                                    \n"
        			//+ "                  ,'20007389', '20006158'                                        \n"
        			//+ "                  )                                                              \n";
        */
        
        String sql = "SELECT A.CHACD                                                         AS CHACD  \n"
    				+ "      ,A.CHACD || '_' || substr(A.CHAOFFNO, length(A.CHAOFFNO) - 3, 4) AS PW     \n"
    				+ "FROM   XCHALIST A                                                                \n"
    				+ "      ,WEBUSER  B                                                                \n"
    				+ "WHERE  A.CHACD  = B.CHACD                                                        \n";
    				
        
        String uSql = "UPDATE WEBUSER     \n"
        		    + "SET    LOGINPW = ? \n"
        		    + "WHERE  CHACD   = ? \n";
        
        ///////////////////////////////////////
        //XCHAGROU 테이블 초기 비밀번호
        String sqlGroup = "SELECT A.CHACD                                                  AS CHACD   \n"
						  + "      ,A.CHACD || '_' || substr(A.chacd, length(A.chacd) - 3, 4) AS PW     \n"
						  + "FROM   WEBUSER  A                                                          \n"
						  + "WHERE  A.idtype in ('03', '04')                                            \n";

        
        
        String uSqlGroup  = "UPDATE WEBUSER     \n"
			    		    + "SET    LOGINPW = ? \n"
			    		    + "WHERE  CHACD   = ? \n";
        
        
        
        try {
        	Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql) ;
            pstmt1 = con.prepareStatement(uSql);
            
            rs = pstmt.executeQuery();
    		while(rs.next()) {
    			pstmt1.setString(1, shaEncoder.encoding(rs.getString("PW")));
    			pstmt1.setString(2, rs.getString("CHACD"));
    			
    			System.out.println("기관코드 : " + rs.getString("CHACD") + ", 패스워드 암호화 전 : " + rs.getString("PW") + ", 패스워드 암호화 후 : " + shaEncoder.encoding(rs.getString("PW")) );
    			// addBatch에 담기
                pstmt1.addBatch();                
    		}
    		pstmt1.executeBatch();
            pstmt1.clearBatch();
            con.commit();
    		
            pstmt.close();
            pstmt1.close();
            rs.close();
            
            
    		
    		//xchagroup 테이블 비밀번호 초기화
    		pstmt = con.prepareStatement(sqlGroup) ;
            pstmt1 = con.prepareStatement(uSqlGroup);
            
            rs = pstmt.executeQuery();
    		while(rs.next()) {
    			pstmt1.setString(1, shaEncoder.encoding(rs.getString("PW")));
    			pstmt1.setString(2, rs.getString("CHACD"));
    			
    			System.out.println("기관코드 : " + rs.getString("CHACD") + ", 패스워드 암호화 전 : " + rs.getString("PW") + ", 패스워드 암호화 후 : " + shaEncoder.encoding(rs.getString("PW")) );
    			// addBatch에 담기
                pstmt1.addBatch();                
    		}
    		

            pstmt1.executeBatch();
            pstmt1.clearBatch();
            con.commit();
               
        } catch(Exception e) {
            e.printStackTrace();
               
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (pstmt != null)  try {pstmt.close();pstmt = null;}   catch(SQLException ex){}
            if (pstmt1 != null) try {pstmt1.close();pstmt1 = null;} catch(SQLException ex){}
            if (con != null)    try {con.close();  con   = null;}   catch(SQLException ex){}
            if (rs != null)	     try {rs.close();   rs    = null;}   catch(SQLException ex){}
        }
		
	}
	
	@Transactional
	public void orgCusPassUpdate(HashMap<String, Object> map) {
		/* 20180703 : hwLee start */
		Connection con = null;
        PreparedStatement pstmt = null, pstmt1 = null;
        ResultSet rs = null;
        
        String sql 	= "SELECT CHACD           as CHACD   \n"
        			+ "      ,NVL(VANO, '')   as VANO    \n" 
        			+ "      ,NVL(CUSHP, '')  as CUSHP   \n" 
        			+ "FROM   XCUSMAS                    \n" ;
        
        /*String tmpSql = "create table XCUSPW_TMP  \n"
    		    	  + "AS                       \n"
    		    	  + "SELECT CHACD             \n"
    		    	  + "      ,PASS_VANO         \n"
    		    	  + "      ,PASS_CUSHP        \n"
    		    	  + "WHERE  1=0               \n";
   
        */
        
        String uSql = "UPDATE XCUSMAS        \n"
        		    + "SET    PASS_VANO  = ? \n"
        		    + "      ,PASS_CUSHP = ? \n"
        		    + "WHERE  CHACD      = ? \n"
        		    + "AND    VANO       = ? \n";
        
        
        
        try {
        	Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql) ;
            pstmt1 = con.prepareStatement(uSql);
            
            rs = pstmt.executeQuery();
            
            int rowCnt = 0;
    		while(rs.next()) {
    			String chaCd     = new String();
    			String passVano  = new String();
    			String passCusHp = new String();
    			
    			chaCd     = StrUtil.nullToVoid( rs.getString("CHACD") );
    			passVano  = StrUtil.nullToVoid( rs.getString("VANO") );
    			passCusHp = StrUtil.nullToVoid( rs.getString("CUSHP") );
    					
    			pstmt1.setString(1, shaEncoder.encoding(passVano));
    			pstmt1.setString(2, shaEncoder.encoding(passCusHp));
    			pstmt1.setString(3, chaCd);
    			pstmt1.setString(4, passVano);
    			
    			// addBatch에 담기
                pstmt1.addBatch();
                ++rowCnt ;
                
                /*System.out.println("처리행 : " + rowCnt +  ", 기관코드 : " + rs.getString("CHACD") + ", VANO 암호화 전 : " + passVano + ", VANO 암호화 후 : " + shaEncoder.encoding(passVano)
				   + ", CUSHP 암호화 전 : " + passCusHp + ", CUSHP 암호화 후 : " + shaEncoder.encoding(passCusHp) 
                );*/
                
                if( (rowCnt%10000) == 0 ) {
                	 
                	logger.info("배치처리 시작: " + rowCnt);
                	pstmt1.executeBatch();
                    pstmt1.clearBatch();
                    con.commit();
                    logger.info("배치처리 끝: " + rowCnt);
                }
                
                //pstmt1.executeBatch();
                //pstmt1.clearBatch();
                
    			//pstmt1.executeUpdate();
    		}

    		logger.info("배치처리 마지막 시작: " + rowCnt);
        	pstmt1.executeBatch();
            pstmt1.clearBatch();
            con.commit();
            logger.info("배치처리 마지막 끝: " + rowCnt);
               
        } catch(Exception e) {
            e.printStackTrace();
               
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (pstmt != null)  try {pstmt.close();pstmt = null;}   catch(SQLException ex){}
            if (pstmt1 != null) try {pstmt1.close();pstmt1 = null;} catch(SQLException ex){}
            if (con != null)    try {con.close();  con   = null;}   catch(SQLException ex){}
            if (rs != null)	    try {rs.close();   rs    = null;}   catch(SQLException ex){}
        }
	}
	
	@Transactional
	@Override
	public void payerPasswordUpdate(HashMap<String, Object> map) throws Exception {
		Connection con = null;
        PreparedStatement pstmt = null, pstmt1 = null;
        ResultSet rs = null;
        
        String sql = "SELECT VANO, CUSHP FROM XCUSMAS WHERE PASS_VANO IS NULL";
        String uSql = "UPDATE XCUSMAS SET PASS_VANO = ?, PASS_CUSHP = ? , MAKEDT = SYSDATE WHERE VANO = ?";
        
        try {
        	Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql) ;
            pstmt1 = con.prepareStatement(uSql);
            
            rs = pstmt.executeQuery();
            int i = 0;
    		while(rs.next()) {
//    			logger.info(">>>>>>>>>>>>>>>>>>>>"+shaEncoder.encoding(rs.getString("cusHp")));
    			logger.info(">>>>>>>>>>>>>>>>>>>>"+rs.getString("vano"));
    			pstmt1.setString(1, shaEncoder.encoding(rs.getString("vano")));
    			if(rs.getString("cusHp") != "" && rs.getString("cusHp") != null) {
    				pstmt1.setString(2, shaEncoder.encoding(rs.getString("cusHp")));
    			} else {
    				pstmt1.setString(2, "");
    			}
    			
    			pstmt1.setString(3, rs.getString("vano"));
    			
    			// addBatch에 담기
                pstmt1.addBatch();
                // 파라미터 Clear
                pstmt1.clearParameters();
                // Batch 실행
                pstmt1.executeBatch();
                // Batch 초기화
                pstmt1.clearBatch();
    		}

            pstmt1.executeBatch();
            con.commit();
               
        } catch(Exception e) {
            e.printStackTrace();
               
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (pstmt != null)  try {pstmt.close();pstmt = null;}   catch(SQLException ex){}
            if (pstmt1 != null) try {pstmt1.close();pstmt1 = null;} catch(SQLException ex){}
            if (con != null)    try {con.close();  con   = null;}   catch(SQLException ex){}
            if (rs != null)	    try {rs.close();   rs    = null;}   catch(SQLException ex){}
        }
	}

	@Override
	public List<NoticeDTO> list(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectList("DataCrypt.selChaList", map);
	}

	@Override
	public void updateCrypt(HashMap<String, Object> map) throws Exception {
		sqlSession.update("DataCrypt.update", map);
	}

	@Override
	public void insertCrypt(HashMap<String, Object> map) throws Exception {
		sqlSession.update("DataCrypt.insertCrypt", map);
	}

}
