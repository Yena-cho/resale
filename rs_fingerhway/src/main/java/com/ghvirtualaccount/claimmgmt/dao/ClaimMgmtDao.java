package com.ghvirtualaccount.claimmgmt.dao;

import com.ghvirtualaccount.vo.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository("claimMgmtDao")
public class ClaimMgmtDao {

    @Resource
    private SqlSessionTemplate sqlSession;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //납부자 마스터 카운트 조회
    public int selectPayerInfoMasterListCnt(ParamVO paramVO) throws Exception {
        return sqlSession.selectOne("claimMgmtDao.selectPayerInfoMasterListCnt", paramVO);
    }

    //납부자 마스터 목록 조회
    public List<PayerInfoMasterInfoVO> selectPayerInfoMasterList(ParamVO paramVO) throws Exception {
        return sqlSession.selectList("claimMgmtDao.selectPayerInfoMasterList", paramVO);
    }

    //사용 가능한 가상계좌의 갯수
    public int selectAvailAcntCnt() throws Exception {
        return sqlSession.selectOne("claimMgmtDao.selectAvailAcntCnt");
    }

    //납부자마스터 ID 채번
    public String selectSeqPayerInfoId() throws Exception {
        return sqlSession.selectOne("claimMgmtDao.selectSeqPayerInfoId");
    }

    //거래번호 채번
    public String selectSeqTrno() throws Exception {
        return sqlSession.selectOne("claimMgmtDao.selectSeqTrno");
    }

    //REGSEQ 채번
    public BigDecimal selectRegSeq() throws Exception {
        return sqlSession.selectOne("claimMgmtDao.selectRegSeq");
    }

    //납부자 마스터 Insert
    public void insertTbPayerInfoMaster(TbPayerInfoMasterVO paramVO) throws Exception {
        sqlSession.insert("claimMgmtDao.insertTbPayerInfoMaster", paramVO);
    }

    //납부자 마스터 Update
    public void updateTbPayerInfoMaster(TbPayerInfoMasterVO paramVO) throws Exception {
        sqlSession.update("claimMgmtDao.updateTbPayerInfoMaster", paramVO);
    }

    //사용 가능한 가상계좌 번호 목록 조회
    public List<TbVirtualAcntVO> selectVirtualAcntNotUsed(int endIdx) throws Exception {
        return sqlSession.selectList("claimMgmtDao.selectVirtualAcntNotUsed", endIdx);
    }

    //가상계좌번호 Update
    public void updateTbVirtualAcnt(TbVirtualAcntVO paramVO) throws Exception {
        sqlSession.update("claimMgmtDao.updateTbVirtualAcnt", paramVO);
    }

    //납부자정보 등록
    public void insertTbPayerInfo(TbPayerInfoVO paramVO) throws Exception {
        sqlSession.insert("claimMgmtDao.insertTbPayerInfo", paramVO);
    }

    public void batchInsertTbPayerInfo(final List<TbPayerInfoVO> voList) throws Exception {
        String sql = "INSERT INTO TB_PAYER_INFO (" +
                "CAR_NUM" +
                ",CAR_OWNER_NM" +
                ",PAY_DUE_DT" +
                ",CAR_OWNER_ADDR" +
                ",GIRO_NUM" +
                "		  , CUST_INQ_NUM" +
                "		  , TOT_AMT" +
                "		  , NOTI_AMT_CD" +
                "		  , GIRO_CD" +
                "		  , OCCUR_DTTM1" +
                "		  , OCCUR_REASON1" +
                "		  , PASS_PLACE1" +
                "		  , PASS_AMT1" +
                "		  , OCCUR_DTTM2" +
                "		  , OCCUR_REASON2" +
                "		  , PASS_PLACE2" +
                "		  , PASS_AMT2" +
                "		  , OCCUR_DTTM3" +
                "		  , OCCUR_REASON3" +
                "		  , PASS_PLACE3" +
                "		  , PASS_AMT3" +
                "		  , OCCUR_DTTM4" +
                "		  , OCCUR_REASON4" +
                "		  , PASS_PLACE4" +
                "		  , PASS_AMT4" +
                "		  , OCCUR_DTTM5" +
                "		  , OCCUR_REASON5" +
                "		  , PASS_PLACE5" +
                "		  , PASS_AMT5" +
                "		  , OCCUR_DTTM6" +
                "		  , OCCUR_REASON6" +
                "		  , PASS_PLACE6" +
                "		  , PASS_AMT6" +
                "		  , OCCUR_DTTM7" +
                "		  , OCCUR_REASON7" +
                "		  , PASS_PLACE7" +
                "		  , PASS_AMT7" +
                "		  , OCCUR_DTTM8" +
                "		  , OCCUR_REASON8" +
                "		  , PASS_PLACE8" +
                "		  , PASS_AMT8" +
                "		  , OCCUR_DTTM9" +
                "		  , OCCUR_REASON9" +
                "		  , PASS_PLACE9" +
                "		  , PASS_AMT9" +
                "		  , OCCUR_DTTM10" +
                "		  , OCCUR_REASON10" +
                "		  , PASS_PLACE10" +
                "		  , PASS_AMT10" +
                "		  ,	PAYER_INFO_ID" +
                "		  , RUN_DT" +
                "		  , VIRTUAL_ACNT_NUM" +
                "		  , TRNO)" +
                "		   VALUES (?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TbPayerInfoVO item = voList.get(i);
                ps.setString(1, item.getCarNum());  //차량번호    
                ps.setString(2, item.getCarOwnerNm()); //차량주      
                ps.setString(3, item.getPayDueDt()); //납부기한    
                ps.setString(4, item.getCarOwnerAddr());  //차량주주소  
                ps.setString(5, item.getGiroNum());    //지로번호    
                ps.setString(6, item.getCustInqNum()); //고객조회번호
                ps.setBigDecimal(7, item.getTotAmt()); //총금액      
                ps.setString(8, item.getNotiAmtCd());  //고지금&코드 
                ps.setString(9, item.getGiroCd());   //지로코드    
                ps.setString(10, item.getOccurDttm1());  //발생일시1   
                ps.setString(11, item.getOccurReason1());  //발생원인1   
                ps.setString(12, item.getPassPlace1()); //통행장소1   
                ps.setBigDecimal(13, item.getPassAmt1());  //통행요금1   
                ps.setString(14, item.getOccurDttm2()); //발생일시2  
                ps.setString(15, item.getOccurReason2());
                ps.setString(16, item.getPassPlace2());
                ps.setBigDecimal(17, item.getPassAmt2());
                ps.setString(18, item.getOccurDttm3());  //발생일시3   
                ps.setString(19, item.getOccurReason3());
                ps.setString(20, item.getPassPlace3());
                ps.setBigDecimal(21, item.getPassAmt3());
                ps.setString(22, item.getOccurDttm4());  //발생일시4   
                ps.setString(23, item.getOccurReason4());
                ps.setString(24, item.getPassPlace4());
                ps.setBigDecimal(25, item.getPassAmt4());
                ps.setString(26, item.getOccurDttm5());  //발생일시5   
                ps.setString(27, item.getOccurReason5());
                ps.setString(28, item.getPassPlace5());
                ps.setBigDecimal(29, item.getPassAmt5());
                ps.setString(30, item.getOccurDttm6());  //발생일시6   
                ps.setString(31, item.getOccurReason6());
                ps.setString(32, item.getPassPlace6());
                ps.setBigDecimal(33, item.getPassAmt6());
                ps.setString(34, item.getOccurDttm7());  //발생일시7   
                ps.setString(35, item.getOccurReason7());
                ps.setString(36, item.getPassPlace7());
                ps.setBigDecimal(37, item.getPassAmt7());
                ps.setString(38, item.getOccurDttm8());  //발생일시8   
                ps.setString(39, item.getOccurReason8());
                ps.setString(40, item.getPassPlace8());
                ps.setBigDecimal(41, item.getPassAmt8());
                ps.setString(42, item.getOccurDttm9());  //발생일시9
                ps.setString(43, item.getOccurReason9());
                ps.setString(44, item.getPassPlace9());
                ps.setBigDecimal(45, item.getPassAmt9());
                ps.setString(46, item.getOccurDttm10());  //발생일시10  
                ps.setString(47, item.getOccurReason10());
                ps.setString(48, item.getPassPlace10());
                ps.setBigDecimal(49, item.getPassAmt10());
                ps.setString(50, item.getPayerInfoId()); //납부자정보ID 
                ps.setString(51, item.getRunDt());  //운행기간   
                ps.setString(52, item.getVirtualAcntNum()); //가상계좌번호
                ps.setString(53, item.getTrno());  //거래번호
            }

            @Override
            public int getBatchSize() {
                return voList.size();
            }
        });
    }

    //다모아 인터페이스 테이블 등록
    public void insertAEGISTRANSMSG(AegisTransMsgVO paramVO) throws Exception {
        sqlSession.insert("claimMgmtDao.insertAEGISTRANSMSG", paramVO);
    }

    //다모아 인터페이스 테이블 등록
    public void batchInsertAEGISTRANSMSG(final List<AegisTransMsgVO> voList) throws Exception {
        final String sql = "INSERT INTO AEGISTRANSMSG (\n" +
                "    RECORDTYPE\n" +
                "  , SERVICEID\n" +
                "  , WORKFIELD\n" +
                "  , SUCCESSYN\n" +
                "  , RESULTCD\n" +
                "  , RESULTMSG\n" +
                "  , TRNO\n" +
                "  , VANO\n" +
                "  , PAYMASMONTH\n" +
                "  , PAYMASDT\n" +
                "  , RCPSTARTDT\n" +
                "  , RCPSTARTTM\n" +
                "  , RCPENDDT\n" +
                "  , RCPENDTM\n" +
                "  , RCPPRTENDDT\n" +
                "  , RCPPRTENDTM\n" +
                "  , CUSNM\n" +
                "  , SMSYN\n" +
                "  , MOBILENO\n" +
                "  , EMAILYN\n" +
                "  , EMAIL\n" +
                "  , CONTENT1\n" +
                "  , CONTENT2\n" +
                "  , CONTENT3\n" +
                "  , CONTENT4\n" +
                "  , CHATRNO\n" +
                "  , ADJFIGRPCD\n" +
                "  , PAYITEMNM\n" +
                "  , PAYITEMAMT\n" +
                "  , PAYITEMYN\n" +
                "  , RCPITEMYN\n" +
                "  , RCPBUSINESSNO\n" +
                "  , RCPPERSONNO\n" +
                "  , PRTPAYITEMNM\n" +
                "  , PRTCONTENT1\n" +
                "  , PRTSEQ\n" +
                "  , TRANST\n" +
                "  , REGSEQ\n" +
                "  , REGDT\n" +
                "  , PAYDAY\n" +
                "  , RCPUSRNAME\n" +
                "  , BNKCD\n" +
                "  , BCHCD\n" +
                "  , MDGUBN\n" +
                "  , TRNDAY\n" +
                "  , RCPAMT\n" +
                "  , VANOBNKCD\n" +
                "  , SVECD\n" +
                ") VALUES (\n" +
                "    ? /*RECORDTYPE*/\n" +
                "  , ?  /*SERVICEID*/\n" +
                "  , 'N'           /*WORKFIELD*/\n" +
                "  , 'Y'           /*SUCCESSYN*/\n" +
                "  , 'D000'        /*RESULTCD*/\n" +
                "  , '정상'        /*RESULTMSG*/\n" +
                "  , ?       /*TRNO*/\n" +
                "  , ?       /*VANO*/\n" +
                "  , DATE_FORMAT(NOW(), '%Y%m')    /*PAYMASMONTH*/\n" +
                "  , DATE_FORMAT(NOW(), '%Y%m%d')    /*PAYMASDT*/\n" +
                "  , DATE_FORMAT(NOW(), '%Y%m%d')    /*RCPSTARTDT*/\n" +
                "  , '0000'    /*RCPSTARTTM*/\n" +
                "  , ?    /*RCPENDDT*/\n" +
                "  , '2359'    /*RCPENDTM*/\n" +
                "  , ?    /*RCPPRTENDDT*/\n" +
                "  , '2359'    /*RCPPRTENDTM*/\n" +
                "  , ?    /*CUSNM*/\n" +
                "  , 'N'    /*SMSYN*/\n" +
                "  , ''    /*MOBILENO*/\n" +
                "  , 'N'    /*EMAILYN*/\n" +
                "  , ''    /*EMAIL*/\n" +
                "  , ?    /*CONTENT1*/\n" +
                "  , ?    /*CONTENT2*/\n" +
                "  , ''    /*CONTENT3*/\n" +
                "  , ''    /*CONTENT4*/\n" +
                "  , ?    /*CHATRNO*/\n" +
                "  , ?    /*ADJFIGRPCD*/\n" +
                "  , ?    /*PAYITEMNM*/\n" +
                "  , ?    /*PAYITEMAMT*/\n" +
                "  , 'N'    /*PAYITEMYN*/\n" +
                "  , 'N'    /*RCPITEMYN*/\n" +
                "  , ''    /*RCPBUSINESSNO*/\n" +
                "  , ''    /*RCPPERSONNO*/\n" +
                "  , ''    /*PRTPAYITEMNM*/\n" +
                "  , ''    /*PRTCONTENT1*/\n" +
                "  , ''    /*PRTSEQ*/\n" +
                "  , '01'    /*TRANST*/\n" +
                "  , ?    /*REGSEQ*/\n" +
                "  , NOW()   /*REGDT*/\n" +
                "  , ''    /*PAYDAY*/\n" +
                "  , ''    /*RCPUSRNAME*/\n" +
                "  , ''    /*BNKCD*/\n" +
                "  , ''    /*BCHCD*/\n" +
                "  , ''    /*MDGUBN*/\n" +
                "  , ''    /*TRNDAY*/\n" +
                "  , ''    /*RCPAMT*/\n" +
                "  , ''    /*VANOBNKCD*/\n" +
                "  , ''    /*SVECD*/\n" +
                ")\n";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AegisTransMsgVO item = voList.get(i);

                ps.setString(1, item.getRecordtype());
                ps.setString(2, item.getServiceid());
                ps.setString(3, item.getTrno());
                ps.setString(4, item.getVano());
                ps.setString(5, item.getRcpenddt());
                ps.setString(6, item.getRcpprtenddt());
                ps.setString(7, item.getCusnm());
                ps.setString(8, item.getContent1());
                ps.setString(9, item.getContent2());
                ps.setString(10, item.getChatrno());
                ps.setString(11, item.getAdjfigrpcd());
                ps.setString(12, item.getPayitemnm());
                ps.setString(13, item.getPayitemamt());
                ps.setString(14, item.getRegseq());
            }

            @Override
            public int getBatchSize() {
                return voList.size();
            }
        });
    }

    //납부자 목록 조회 By PayerInfoId
    public List<PayerInfoVO> selectPayerInfoList(String payerInfoId) throws Exception {
        return sqlSession.selectList("claimMgmtDao.selectPayerInfoList", payerInfoId);
    }

    //납부자 조회(중복 체크용)
    public int selectPayerInfo(TbPayerInfoVO paramVO) throws Exception {
        return sqlSession.selectOne("claimMgmtDao.selectPayerInfo", paramVO);
    }

    public void batchUpdateTbVirtualAcnt(final List<TbVirtualAcntVO> tbVirtualAcntList) {
        String sql = "UPDATE TB_VIRTUAL_ACNT\n" +
                "SET\n" +
                "    USE_YN = ?\n" +
                "  , PAY_DUE_DT = ?\n" +
                "WHERE\n" +
                "    VIRTUAL_ACNT_NUM = ?\n";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final TbVirtualAcntVO item = tbVirtualAcntList.get(i);

                ps.setString(1, item.getUseYn());
                ps.setString(2, item.getPayDuDt());
                ps.setString(3, item.getVirtualAcntNum());
            }

            @Override
            public int getBatchSize() {
                return tbVirtualAcntList.size();
            }
        });
    }
}
