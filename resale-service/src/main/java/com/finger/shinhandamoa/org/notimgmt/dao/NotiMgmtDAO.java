package com.finger.shinhandamoa.org.notimgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtBaseDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtPdfResDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiMgmtResDTO;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;


@Repository("notiMgmtDao")
public class NotiMgmtDAO {

    //	@Resource
//	private SqlSessionTemplate sqlSession;
    @Inject
    private SqlSession sqlSession;

    /**
     * 기관관리자 세션
     *
     * @param user
     * @return
     */
    public NotiMgmtBaseDTO selectNotiBaseInfo(String user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        return sqlSession.selectOne("Organization.selectNotiBaseInfo", map);
    }

    /**
     * 고지서출력/조회 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<NotiMgmtResDTO> getNotiMgmtList(Map<String, Object> map) {
        return sqlSession.selectList("Organization.getNotiMgmtList", map);
    }

    /**
     * 고지서출력/조회 카운트
     *
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getNotiMgmtTotalCount(Map<String, Object> map) {
        return sqlSession.selectOne("Organization.getNotiMgmtTotalCount", map);
    }

    /**
     * 고지서 출력양식 조회
     *
     * @param map
     * @return
     * @throws Exception
     */
    public String getExistsBillForm(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectOne("Organization.getExistsBillForm", map);
    }

    /**
     * pdf 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<NotiMgmtPdfResDTO> getNotiBillList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("Organization.getNotiBillList", map);
    }

    public List<NotiMgmtPdfResDTO> getPrivewBillList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList("Organization.getPrivewBillList", map);
    }

}
