package com.finger.shinhandamoa.org.custMgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;

/**
 * @author by puki
 * @date 2018. 4. 12.
 * @desc 최초생성
 */
@Repository
public class CustExcelDAOImpl implements CustExcelDAO {

    @Inject
    private SqlSession sqlSession;

    @Inject
    private ShaEncoder shaEncoder;

    @Override
    public List<CustReg01DTO> selectExcelSaveCustReg(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("CustExcelDao.selectExcelSaveCustReg", map);
    }

    @Override
    public List<CustReg01DTO> excelList(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("CustExcelDao.selExcelItemSample", map);
    }

    @Override
    public void custExcelInsert(CustReg01DTO dto) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notiMasCd", StrUtil.nullToVoid(dto.getVano()));
        map.put("chaCd", StrUtil.nullToVoid(dto.getChaCd()));
        map.put("vano", StrUtil.nullToVoid(dto.getVano()));
        map.put("masKey", StrUtil.nullToVoid(dto.getMasKey()));
        map.put("cusKey", StrUtil.nullToVoid(dto.getCusKey()));
        map.put("cusGubn1", StrUtil.nullToVoid(dto.getCusGubn1()));
        map.put("cusGubn2", StrUtil.nullToVoid(dto.getCusGubn2()));
        map.put("cusGubn3", StrUtil.nullToVoid(dto.getCusGubn3()));
        map.put("cusGubn4", StrUtil.nullToVoid(dto.getCusGubn4()));
        map.put("cusName", StrUtil.nullToVoid(dto.getCusName()));
        map.put("cusHp", StrUtil.nullToVoid(dto.getCusHp()));
        map.put("cusMail", StrUtil.nullToVoid(dto.getCusMail()));
        map.put("smsYn", StrUtil.nullToVoid(dto.getSmsYn()));
        map.put("mailYn", StrUtil.nullToVoid(dto.getMailYn()));
        map.put("cusOffNo", StrUtil.nullToVoid(dto.getCusOffNo()));
        map.put("makeDt", StrUtil.nullToVoid(dto.getMakeDt()));
        map.put("maker", StrUtil.nullToVoid(dto.getMaker()));
        map.put("disabled", StrUtil.nullToVoid(dto.getDisabled()));
        map.put("rcpGubn", StrUtil.nullToVoid(dto.getRcpGubn()));
        map.put("rcpReqTy", StrUtil.nullToVoid(dto.getRcpReqTy()));
        map.put("memo", StrUtil.nullToVoid(dto.getMemo()));
        map.put("cusType", StrUtil.nullToVoid(dto.getCusType()));
        map.put("confirm", StrUtil.nullToVoid(dto.getConfirm()));
        map.put("passVano", shaEncoder.encoding(StrUtil.nullToVoid(dto.getVano())));
        map.put("passCushp", shaEncoder.encoding(StrUtil.nullToVoid(dto.getCusHp())));

        sqlSession.insert("CustExcelDao.custExcelInsert", map);
    }

    @Override
    public void custDetailExcelInsert(CustReg01DTO dto) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notiMasCd", dto.getNotiMasCd());
        map.put("remark", dto.getRemark());
        map.put("chaCd", dto.getChaCd());

        sqlSession.insert("CustExcelDao.custDetailExcelInsert", map);
    }

    @Override
    public void custDetExcelInsert(CustReg01DTO dto) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notiMasCd", dto.getNotiMasCd());
        map.put("remark", dto.getRemark());
        map.put("chaCd", dto.getChaCd());

        sqlSession.insert("CustExcelDao.custDetExcelInsert", map);
    }

    @Override
    public void custFailDelete(String chaCd) throws Exception {
        sqlSession.delete("CustExcelDao.custFailDelete", chaCd);
    }

    @Override
    public void custExcelFailInsert(CustReg01DTO dto) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", dto.getChaCd());
        map.put("xRow", dto.getxRow());
        map.put("masKey", StrUtil.nullToVoid(dto.getMasKey()));
        map.put("cusKey", StrUtil.nullToVoid(dto.getCusKey()));
        map.put("cusGubn1", StrUtil.nullToVoid(dto.getCusGubn1()));
        map.put("cusGubn2", StrUtil.nullToVoid(dto.getCusGubn2()));
        map.put("cusGubn3", StrUtil.nullToVoid(dto.getCusGubn3()));
        map.put("cusGubn4", StrUtil.nullToVoid(dto.getCusGubn4()));
        map.put("cusName", StrUtil.nullToVoid(dto.getCusName()));
        map.put("cusHp", StrUtil.nullToVoid(dto.getCusHp()).replaceAll("-", ""));
        map.put("cusMail", StrUtil.nullToVoid(dto.getCusMail()));
        map.put("smsYn", StrUtil.nullToVoid(dto.getSmsYn()));
        map.put("mailYn", StrUtil.nullToVoid(dto.getMailYn()));
        map.put("cusOffNo", StrUtil.nullToVoid(dto.getCusOffNo()).replaceAll("-", ""));
        map.put("rcpGubn", StrUtil.nullToVoid(dto.getRcpGubn()));
        map.put("rcpReqTy", StrUtil.nullToVoid(dto.getRcpReqTy()));
        map.put("result", dto.getResult());
        map.put("maker", StrUtil.nullToVoid(dto.getChaCd()));
        map.put("memo", StrUtil.nullToVoid(dto.getMemo()));
        map.put("vano", StrUtil.nullToVoid(dto.getVano()));

        sqlSession.insert("CustExcelDao.custExcelFailInsert", map);
    }

    @Override
    public List<CustReg01DTO> failList(String chaCd, int start, int end) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", chaCd);
        map.put("start", start);
        map.put("end", end);

        return sqlSession.selectList("CustExcelDao.selectFailList", map);
    }

    @Override
    public int failTotalCount(String chaCd) throws Exception {
        return sqlSession.selectOne("CustExcelDao.failTotalCount", chaCd);
    }

    @Override
    public List<CustReg01DTO> failExcelList(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("CustExcelDao.selExcelFailData", map);
    }

    @Override
    public int selDupCusInfo(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("CustExcelDao.selDupCusInfo", map);
    }

    @Override
    public void custExcelUpdate(Map<String, Object> map) throws Exception {
        sqlSession.update("CustExcelDao.updateCust", map);
    }
}
