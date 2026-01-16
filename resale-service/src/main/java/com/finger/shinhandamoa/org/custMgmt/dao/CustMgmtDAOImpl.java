package com.finger.shinhandamoa.org.custMgmt.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.common.ShaEncoder;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;

/**
 * @author 홍길동
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
@Repository
public class CustMgmtDAOImpl implements CustMgmtDAO {

    @Inject
    private SqlSession sqlSession;

    @Inject
    private ShaEncoder shaEncoder;

    // 고객등록목록 total count
    @Override
    public int custReg01TotalCount(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("CustMgmtDao.custReg01TotalCount", map);
    }

    // 고객등록목록
    @Override
    public List<CustReg01DTO> custReg01ListAll(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("CustMgmtDao.custReg01List", map);
    }

    @Override
    public void updateVanoUseYn(HashMap<String, Object> map) throws Exception {
        sqlSession.update("CustMgmtDao.updateVanoUseYn", map);
    }

    @Override
    public void deleteXcusMas(HashMap<String, Object> map) throws Exception {
        sqlSession.delete("CustMgmtDao.deleteXcusMas", map);
    }

    @Override
    public void updateVano(HashMap<String, Object> map) throws Exception {
        sqlSession.update("CustMgmtDao.updateVano", map);
    }

    @Override
    public void deleteCusInfo(HashMap<String, Object> map) throws Exception {
        sqlSession.delete("CustMgmtDao.deleteCusInfo", map);
    }

    // 고객 등록(insert)
    @Override
    public void insertXCustMas(CustReg01DTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
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
        map.put("cusHp", StrUtil.nullToVoid(dto.getCusHp()).replaceAll("-", ""));
        map.put("cusMail", StrUtil.nullToVoid(dto.getCusMail()));
        map.put("smsYn", StrUtil.nullToVoid(dto.getSmsYn()));
        map.put("mailYn", StrUtil.nullToVoid(dto.getMailYn()));
        map.put("cusOffNo", StrUtil.nullToVoid(dto.getCusOffNo()).replaceAll("-", ""));
        map.put("makeDt", StrUtil.nullToVoid(dto.getMakeDt()));
        map.put("maker", StrUtil.nullToVoid(dto.getMaker()));
        map.put("regDt", StrUtil.nullToVoid(dto.getRegDt()));
        map.put("disabled", StrUtil.nullToVoid(dto.getDisabled()));
        map.put("rcpGubn", StrUtil.nullToVoid(dto.getRcpGubn()));
        map.put("rcpReqTy", StrUtil.nullToVoid(dto.getRcpReqTy()));
        map.put("confirm", StrUtil.nullToVoid(dto.getConfirm()));
        map.put("memo", StrUtil.nullToVoid(dto.getMemo()));
        map.put("cusType", StrUtil.nullToVoid(dto.getCusType()));

        map.put("passVano", shaEncoder.encoding(StrUtil.nullToVoid(dto.getVano())));
        map.put("passCushp", shaEncoder.encoding(StrUtil.nullToVoid(dto.getCusHp())));

        sqlSession.insert("CustMgmtDao.insertXCustMas", map);

    }

    //고객 정보 수정
    @Override
    public void updateXcusMas(CustReg01DTO dto) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("notiMasCd", dto.getVano());
        map.put("chaCd", dto.getChaCd());
        map.put("vano", dto.getVano());
        map.put("masKey", dto.getMasKey());
        map.put("cusKey", dto.getCusKey());
        map.put("cusGubn1", dto.getCusGubn1());
        map.put("cusGubn2", dto.getCusGubn2());
        map.put("cusGubn3", dto.getCusGubn3());
        map.put("cusGubn4", dto.getCusGubn4());
        map.put("cusName", dto.getCusName());
        map.put("cusHp", dto.getCusHp().replaceAll("-", ""));
        map.put("cusMail", dto.getCusMail());
        map.put("smsYn", dto.getSmsYn());
        map.put("mailYn", dto.getMailYn());
        map.put("cusOffNo", dto.getCusOffNo().replaceAll("-", ""));
        map.put("makeDt", dto.getMakeDt());
        map.put("maker", dto.getMaker());
        map.put("regDt", dto.getRegDt());
        map.put("disabled", dto.getDisabled());
        map.put("rcpGubn", dto.getRcpGubn());
        map.put("rcpReqTy", dto.getRcpReqTy());
        map.put("cusType", dto.getCusType());
        map.put("confirm", dto.getConfirm());
        map.put("memo", dto.getMemo());
        map.put("passCushp", shaEncoder.encoding(StrUtil.nullToVoid(dto.getCusHp())));

        // 간혹 빠트리는 케이스가 있음.
        map.put("jobType", dto.getJobType());

        sqlSession.update("CustMgmtDao.updateXcusMas", map);

    }

    @Override
    public void updateCusmas(HashMap<String, Object> map) throws Exception {
        sqlSession.update("CustMgmtDao.updateCusmas", map);
    }

    //고객 단건 조회
    @Override
    public CustReg01DTO selectDetailCustReg(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("CustMgmtDao.selectXcusMas", map);
    }

    @Override
    public CustReg01DTO getVanoInfo(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("CustMgmtDao.getVanoInfo", map);
    }

    @Override
    public void updateValist(HashMap<String, Object> map) throws Exception {
        sqlSession.update("CustMgmtDao.updateValist", map);
    }

    @Override
    public void updateNotiMasCusName(HashMap<String, Object> map) throws Exception {
        sqlSession.update("CustMgmtDao.updateNotiMasCusName", map);
    }

    @Override
    public CustReg01DTO selectCusDisabled(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("CustMgmtDao.selectCusDisabled", map);
    }

    @Override
    public int updateCashMasInfo(HashMap<String, Object> map) throws Exception {
        return sqlSession.update("CustMgmtDao.updateCashMasInfo", map);
    }
}
