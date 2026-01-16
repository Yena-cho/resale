package com.damoa.comm.db;

import com.damoa.comm.UserException;
import com.damoa.log.MyLogger;
import com.damoa.util.MirRecord;
import com.damoa.util.MirRecordSet;
import com.damoa.util.StringUtil;

import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * DB 커넥션 생성/관리 및 쿼리실행
 * @author jhjeong@finger.co.kr
 * @modified 2018. 9. 6.
 */
public abstract class DBConn {
    protected Connection conn = null;

    protected boolean COMMIT = true;
    protected boolean DEBUG = false;

    protected int SELECTLIMIT = 300; //FIXME 한번 청구최대사이즈

    protected int SELECTCNT = 0;
    protected boolean SELECTLIMITYN = false;

    protected boolean errSqlView = false; // true면 에러 날때 sql도 출력함.

    public abstract void init();

    private MyLogger log;
    public DBConn(){
        log = new MyLogger();
    }

    /**
     * debugging mode 전환
     * @param isDebug debugging mode 상태. true or false
     * @return void
     */
    public void debug(boolean isDebug) {
        this.DEBUG = isDebug;
    }

    /**
     * Oracle connection 반환.
     */
    public Connection getConnection() {
        return this.conn;
    } // end getConnection

    /**
     * 커넥션 closing 여부
     * true = closed, false=connected
     * @return
     */
    public boolean isClose() {
    	if (this.conn == null) return true;
    	try {
			return this.conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
    }
    
    /**
     * Oracle commit
     */
    public boolean commit() {
        // commit
        try {
            this.conn.commit();
            return true;
        } catch (Exception er) {
            log.println("commit error !\n" + er.toString());
            return false;
        }
    } // end commit

    /**
     * Oracle rollback
     */
    public boolean rollback() {
        // rollback
        try {
            this.conn.rollback();
            return true;
        } catch (Exception er) {
            log.println("rollback error !\n" + er.toString());
            return false;
        }
    } // end rollback

    /**
     * Oracle DB DisConnect
     */
    public void disconnect() {
        // JDBC 연결 끊기
        try {
            if (this.COMMIT==false) rollback();
            this.conn.close();
            if (this.DEBUG) log.println("JDBC Connection disconnected ... !!!");
        } catch (Exception e) {
            log.println("[ERROR : JDBC연결 끊기 실패]\n" + e.toString());
        } finally {
        	this.conn = null;
        }
    } // end disconnect

    /**
     * select 개수 반환.
     * @return int
     */
    public int getSelectCnt() {
        return this.SELECTCNT;
    }

    /**
     * ResultSet close
     * @throws UserException
     */
    public void closeRs(ResultSet rs) throws UserException {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            throw new UserException("stmt close error !\n" + e.toString());
        } finally {
        	rs = null;
        }
    } // end closeStmt

    /**
     * PreparedStatement close
     * @throws UserException
     */
    public void closeStmt(PreparedStatement pstmt) throws UserException {
        try {
            if (pstmt != null) pstmt.close();
        } catch (Exception e) {
            throw new UserException("stmt close error !\n" + e.toString());
        } finally {
        	pstmt = null;
        }
    } // end closeStmt

    /**
     * Statement close
     * @throws UserException
     */
    public void closeStmt(Statement stmt) throws UserException {
        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            throw new UserException("stmt close error !\n" + e.toString());
        } finally {
        	stmt = null;
        }
    } // end closeStmt

    /**
     * Oracle transaction 상태 반환. setAutoCommit(ture)면 true, setAutoCommit(false)면 false.
     */
    public boolean getAutoCommit() {
        return this.COMMIT;
    } // end getAutoCommit

    /**
     * Oracle transaction 제어 변환.
     */
    public void setAutoCommit(boolean status) throws UserException {
    	if (this.COMMIT != status) {
    		this.COMMIT = status;
		    try {
		        this.conn.setAutoCommit(status);
		    } catch (Exception e) {
		        throw new UserException("setAutoCommit 변환 error ("+status+") !\n" + e.toString());
		    }
		}
    } // end setAutoCommit

    /**
     * select 개수 제한값 세팅. 기본은 100개.
     * @param cnt select 개수.
     * @return void
     */
    public void setSelectLimit(int cnt) {
    	this.SELECTLIMIT = cnt;
    }

    /**
     * update나 insert query 실행.
     * @param qry update, insert query
     * @return int 실행결과
     */
    public int update(String qry) throws UserException {
        PreparedStatement stmt = null;
        int result = 0;
        if (this.DEBUG) log.println("sql : " + qry);
        try {
            stmt = this.conn.prepareStatement(qry);
            result = stmt.executeUpdate();
        } catch (Exception e) {
            if (rollback()) log.println("check your queqry !\n");
            log.println("update query error !\n" + qry + "\n" + e.toString());
            //return 0;
            if (!this.errSqlView) qry = "";
            throw new UserException("update query error !\n" + qry + "\n" + e.toString());
        } finally { try {closeStmt(stmt);} catch (Exception e) {return 0;}}

        return result;
    } // end update

    /**
     * update나 insert query 실행.
     * @param qry update, insert query
     * @param clue query안의 변수 벡터
     * @return int 실행결과
     */
    @SuppressWarnings("rawtypes")
	public int update(String qry, Vector clue) throws UserException {
        PreparedStatement stmt = null;
        int result = 0;
        if (this.DEBUG) {
            log.println("sql : " + qry);
            log.println("clues : " + clue);
        }
        try {
            stmt = this.conn.prepareStatement(qry);
            for (int i=1;i<=clue.size();i++) {
                stmt.setString(i, (String)clue.get(i-1));
            } // end for
            result = stmt.executeUpdate();
        } catch (Exception e) {
            if (rollback()) log.println("check your queqry !\n");
            log.println("update query error !\n" + qry + "\n" + e.toString());
            //return 0;
            if (!this.errSqlView) qry = "";
            throw new UserException("update query error !\n" + qry + "\n" + e.toString());
        } finally { try {closeStmt(stmt);} catch (Exception e) {return 0;}}

        return result;
    } // end update

    /**
     * select query 실행
     * @param qry select형 query
     * @return MirRecord
     * @throws UserException
     */
    @SuppressWarnings({ "unchecked" })
	public MirRecord selectRecord(String qry) throws UserException {
        MirRecord mir = new MirRecord();
        MirRecordSet mirREC = new MirRecordSet();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        if (DEBUG) log.println("sql : " + qry);
        try {
            stmt = this.conn.prepareStatement(qry);
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            int limit = 0;
            while (rs.next()) {
                limit++;
                this.SELECTCNT = limit;
                if (limit > this.SELECTLIMIT) {
                    //break;
                	this.SELECTLIMITYN = true;
                } else {
                	this.SELECTLIMITYN = false;
                    Hashtable<String, String> ht = new Hashtable<String, String>();
                    for (int i=1;i<=cols;i++) {
                        ht.put(metaData.getColumnName(i).toUpperCase(), StringUtil.null2void(rs.getString(i)));
                    } // end for
                    mirREC.add(ht);
                }
            } // end while
        } catch (Exception e) {
            if (!this.errSqlView) qry = "";
            throw new UserException("select query error !\n" + qry + "\n" + e.toString());
        } finally {
            closeRs(rs);
            closeStmt(stmt);
        }
        mir.setRec("REC", mirREC);
        return mir;
    } // end selectRecord

    /**
     * select query 실행
     * @param qry select형 query
     * @param clue query안의 변수 벡터
     * @return Vector Hashtable로 이루어진 Vector
     * @throws UserException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector select(String qry, Vector clue) throws UserException {
        Vector result = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        if (this.DEBUG) {
            log.println("sql : " + qry);
            log.println("clues : " + clue);
        }
        try {
            stmt = this.conn.prepareStatement(qry);
            for (int i=1;i<=clue.size();i++) {
                stmt.setString(i, (String)clue.get(i-1));
            } // end for
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            int limit = 0;
            while (rs.next()) {
                limit++;
                this.SELECTCNT = limit;
                if (limit > this.SELECTLIMIT) {
                    //break;
                	this.SELECTLIMITYN = true;
                } else {
                	this.SELECTLIMITYN = false;
                    Hashtable ht = new Hashtable();
                    for (int i=1;i<=cols;i++) {
                        ht.put(metaData.getColumnName(i).toUpperCase(), StringUtil.null2void(rs.getString(i)));
                    } // end for
                    result.add(ht);
                }
            } // end while            
        } catch (Exception e) {
            if (!this.errSqlView) qry = "";
            throw new UserException("select query error !\n" + qry + "\n" + e.toString());
        } finally {
            closeRs(rs);
            closeStmt(stmt);
        }
        return result;
    } // end select
}
