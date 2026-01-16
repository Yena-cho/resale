package com.finger.shinhandamoa.sys.cashreceipt.mapper;

import com.finger.shinhandamoa.sys.cashreceipt.service.*;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 현금영수증 관련 매퍼
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class CashReceiptMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptMapper.class);
    
    @Autowired
    private SqlSession sqlSession;

    public long countClientWithCashReceiptStatus(String startDate, String endDate, String chaCd, String chaName, String status, String enableCashReceipt, String enableAutomaticCashReceipt, String mandRcpYn) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("chaCd", chaCd);
        param.put("chaName", chaName);
        param.put("chaSt", status);
        param.put("enableCashReceipt", enableCashReceipt);
        param.put("enableAutomaticCashReceipt", enableAutomaticCashReceipt);
        param.put("mandRcpYn", mandRcpYn);
        
        return sqlSession.selectOne("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.countClientWithCashReceiptStatus", param);
    }
    
    public List<ClientWithCashReceiptStatusDTO> getClientWithCashReceiptStatusList(String startDate, String endDate, String chaCd, String chaName, String status, String enableCashReceipt, String enableAutomaticCashReceipt, String orderBy, RowBounds rowBounds, String mandRcpYn) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("chaCd", chaCd);
        param.put("chaName", chaName);
        param.put("chaSt", status);
        param.put("enableCashReceipt", enableCashReceipt);
        param.put("enableAutomaticCashReceipt", enableAutomaticCashReceipt);
        param.put("mandRcpYn", mandRcpYn);
        param.put("orderBy", orderBy);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.getClientWithCashReceiptStatusList", param, rowBounds);
    }

    public long countReceiptWithCashReceiptStatus(String startDate, String endDate, String startDate2, String endDate2, String chaCd, String chaName, String sveCd, String createCashReceipt, String notIssuedErrorCashReceipt) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("startDate2", startDate2);
        param.put("endDate2", endDate2);
        param.put("chaCd", chaCd);
        param.put("chaName", chaName);
        param.put("sveCd", sveCd);
        param.put("createCashReceipt", createCashReceipt);
        param.put("notIssuedErrorCashReceipt", notIssuedErrorCashReceipt);

        return sqlSession.selectOne("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.countReceiptWithCashReceiptStatus", param);
    }

    public long countIssueWithCashReceiptStatus(String startDate, String endDate, String startDate2, String endDate2, String startDate3, String endDate3, String chaCd, String chaName, String sveCd, String createErrorCashReceipt, String duplicationIssuedErrorCashReceipt, String notIssuedErrorCashReceipt) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("startDate2", startDate2);
        param.put("endDate2", endDate2);
        param.put("startDate3", startDate3);
        param.put("endDate3", endDate3);
        param.put("chaCd", chaCd);
        param.put("chaName", chaName);
        param.put("sveCd", sveCd);
        param.put("createErrorCashReceipt", createErrorCashReceipt);
        param.put("duplicationIssuedErrorCashReceipt", duplicationIssuedErrorCashReceipt);
        param.put("notIssuedErrorCashReceipt", notIssuedErrorCashReceipt);

        return sqlSession.selectOne("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.countIssueWithCashReceiptStatus", param);
    }

    public List<ReceiptWithCashReceiptStatusDTO> getReceiptWithCashReceiptStatusList(String startDate, String endDate, String startDate2, String endDate2, String chaCd, String chaName, String sveCd, String createCashReceipt, String notIssuedErrorCashReceipt, String orderBy, RowBounds rowBounds) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("startDate2", startDate2);
        param.put("endDate2", endDate2);
        param.put("chaCd", chaCd);
        param.put("chaName", chaName);
        param.put("sveCd", sveCd);
        param.put("createCashReceipt", createCashReceipt);
        param.put("notIssuedErrorCashReceipt", notIssuedErrorCashReceipt);
        param.put("orderBy", orderBy);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.getReceiptWithCashReceiptStatusList", param, rowBounds);
    }

    public List<IssueWithCashReceiptStatusDTO> getIssueWithCashReceiptStatusList(String startDate, String endDate, String startDate2, String endDate2, String startDate3, String endDate3, String chaCd, String chaName, String sveCd, String createErrorCashReceipt, String duplicationIssuedErrorCashReceipt, String notIssuedErrorCashReceipt, String orderBy, RowBounds rowBounds) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("startDate2", startDate2);
        param.put("endDate2", endDate2);
        param.put("startDate3", startDate3);
        param.put("endDate3", endDate3);
        param.put("chaCd", chaCd);
        param.put("chaName", chaName);
        param.put("sveCd", sveCd);
        param.put("createErrorCashReceipt", createErrorCashReceipt);
        param.put("duplicationIssuedErrorCashReceipt", duplicationIssuedErrorCashReceipt);
        param.put("notIssuedErrorCashReceipt", notIssuedErrorCashReceipt);
        param.put("orderBy", orderBy);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.getIssueWithCashReceiptStatusList", param, rowBounds);
    }

    public List<DetailPopUpCashmasCdDTO> datailPopUpCashmasCd(String cashmasCd) {
        final Map<String, Object> param = new HashMap<>();
        param.put("cashmasCd", cashmasCd);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.datailPopUpCashmasCd", param);
    }

    public List<DetailPopUpChaNameDTO> datailPopUpChaName(String chaCd) {
        final Map<String, Object> param = new HashMap<>();
        param.put("chaCd", chaCd);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.datailPopUpChaName", param);
    }

    public List<DetailPopUpRegDtDTO> datailPopUpRegDt(String rcpmasCd) {
        final Map<String, Object> param = new HashMap<>();
        param.put("rcpmasCd", rcpmasCd);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.datailPopUpRegDt", param);
    }

    public long countNoCashReceiptReceipt(String chaCd, String paymentMedia, String startDate, String endDate) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("chaCd", chaCd);
        param.put("sveCd", paymentMedia);

        return sqlSession.selectOne("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.countNoCashReceiptReceipt", param);
    }

    public List<String> selectNoCashReceiptReceipt(String chaCd, String paymentMedia, String startDate, String endDate, RowBounds rowBounds) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("chaCd", chaCd);
        param.put("sveCd", paymentMedia);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.selectNoCashReceiptReceipt", param, rowBounds);
    }

    public long countReadyCashReceipt(String chaCd, String paymentMedia, String startDate, String endDate) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("chaCd", chaCd);
        param.put("sveCd", paymentMedia);

        return sqlSession.selectOne("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.countReadyCashReceipt", param);
    }

    public List<String> selectReadyCashReceipt(String chaCd, String paymentMedia, String startDate, String endDate, RowBounds rowBounds) {
        final Map<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("chaCd", chaCd);
        param.put("sveCd", paymentMedia);

        return sqlSession.selectList("com.finger.shinhandamoa.sys.cashreceipt.mapper.CashReceiptMapper.selectReadyCashReceipt", param, rowBounds);
    }
}
