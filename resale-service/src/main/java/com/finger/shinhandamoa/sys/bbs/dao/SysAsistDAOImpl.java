package com.finger.shinhandamoa.sys.bbs.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.main.dao.CustomerAsistDAOImpl;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;
import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;

@Repository
public class SysAsistDAOImpl implements SysAsistDAO {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAsistDAOImpl.class);

    @Autowired
    private SqlSession sqlSession;

    //공지사항 관리 목록 가져오기
    @Override
    public List<SysDTO> noticeListAll(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("SysAsist.noticeListAll", map);
    }

    @Override
    public void popupWrite(Map<String, Object> map) throws Exception {
        sqlSession.insert("SysAsist.popupWrite", map);
    }

    //공지사항 토탈 카운트
    @Override
    public int noticeTotalCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.noticeTotalCount", map);
    }

    @Override
    public SysDTO selectByNo(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.selectByNo", map);
    }

    @Override
    public List<SysDTO> faqListAll(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("SysAsist.faqListAll", map);
    }

    @Override
    public int faqTotalCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.faqTotalCount", map);
    }

    @Override
    public void doNoticeWrite(Map<String, Object> map) throws Exception {
        sqlSession.insert("SysAsist.doNoticeWrite", map);
    }

    @Override
    public void doFaqWrite(Map<String, Object> map) throws Exception {
        sqlSession.insert("SysAsist.doFaqWrite", map);
    }

    @Override
    public void faqUpdate(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.faqUpdate", map);
    }

    @Override
    public void noticeUpdate(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.noticeUpdate", map);
    }

    @Override
    public List<SysDTO> qnaListAll(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("SysAsist.qnaListAll", map);
    }

    @Override
    public int qnaTotalCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.qnaTotalCount", map);
    }

    @Override
    public SysDTO selectByFromno(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.selectByFromno", map);
    }

    @Override
    public void qnaUpdqte(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.qnaUpdate", map);
    }

    @Override
    public void qnaInsert(Map<String, Object> map) throws Exception {
        sqlSession.insert("SysAsist.qnaInsert", map);
    }

    @Override
    public int popupTotCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.popupTotCount", map);
    }

    @Override
    public List<PopupDTO> popupListAll(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("SysAsist.popupListAll", map);
    }

    @Override
    public PopupDTO popupDetail(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.popupDetail", map);
    }

    @Override
    public void popupDelete(Map<String, Object> map) throws Exception {
        sqlSession.delete("SysAsist.popupDelete", map);
    }

    @Override
    public void popupUpdateNofile(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.popupUpdateNofile", map);
    }

    @Override
    public void popupUpdateWithFile(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.popupUpdateWithFile", map);
    }

    @Override
    public int exposynCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.exposynCount", map);
    }

    @Override
    public void updateExposeN(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.updateExposeN", map);
    }

    @Override
    public int bannerTotCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.bannerTotCount", map);
    }

    @Override
    public int bannerTypeCount(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.bannerTypeCount", map);
    }

    @Override
    public List<BannerDTO> bannerListAll(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("SysAsist.bannerListAll", map);
    }

    @Override
    public BannerDTO bannerInfo(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.bannerInfo", map);
    }

    @Override
    public void bannerWrite(Map<String, Object> map) throws Exception {
        sqlSession.insert("SysAsist.bannerWrite", map);
    }

    @Override
    public BannerDTO bannerDetail(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.bannerDetail", map);
    }

    @Override
    public void bannerDelete(Map<String, Object> map) throws Exception {
        sqlSession.delete("SysAsist.bannerDelete", map);
    }

    @Override
    public void bannerUpdate(Map<String, Object> map) throws Exception {
        sqlSession.update("SysAsist.bannerUpdate", map);
    }

    @Override
    public int orderCheck(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne("SysAsist.orderCheck", map);
    }
}
