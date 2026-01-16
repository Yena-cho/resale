package com.finger.shinhandamoa.sys.bbs.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.data.file.mapper.SimpleFileMapper;
import com.finger.shinhandamoa.data.table.mapper.FwFileMapper;
import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finger.shinhandamoa.sys.bbs.dao.SysAsistDAO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;
import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;

@Service
public class SysAsistServiceImple implements SysAsistService {

    @Autowired
    private SysAsistDAO SysAsistDAO;

    @Autowired
    private FwFileMapper fwFileMapper;

    @Autowired
    private SimpleFileMapper simpleFileMapper;

    //공지사항 관리 리스트
    @Override
    public List<SysDTO> noticeListAll(Map<String, Object> map) throws Exception {
        return SysAsistDAO.noticeListAll(map);
    }

    //페이징 처리 카운트
    @Override
    public int noticeTotalCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.noticeTotalCount(map);
    }

    @Override
    public int popupTotalCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.popupTotCount(map);
    }

    @Override
    public List<PopupDTO> popupListAll(Map<String, Object> map) throws Exception {
        return SysAsistDAO.popupListAll(map);
    }

    @Override
    public void popupWrite(Map<String, Object> map) throws Exception {
        SysAsistDAO.popupWrite(map);
    }

    @Override
    public SysDTO selectByNo(Map<String, Object> map) throws Exception {
        return SysAsistDAO.selectByNo(map);
    }

    @Override
    public List<SysDTO> faqListAll(Map<String, Object> map) throws Exception {
        return SysAsistDAO.faqListAll(map);
    }

    @Override
    public int faqTotalCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.faqTotalCount(map);
    }

    @Override
    public void doNoticeWrite(Map<String, Object> map) throws Exception {
        SysAsistDAO.doNoticeWrite(map);
    }

    @Override
    public void doFaqWrite(Map<String, Object> map) throws Exception {
        SysAsistDAO.doFaqWrite(map);
    }

    @Override
    public void faqUpdate(Map<String, Object> map) throws Exception {
        SysAsistDAO.faqUpdate(map);
    }

    @Override
    public void noticeUpdate(Map<String, Object> map) throws Exception {
        SysAsistDAO.noticeUpdate(map);

    }

    @Override
    public int qnaTotalCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.qnaTotalCount(map);
    }

    @Override
    public List<SysDTO> qnaListAll(Map<String, Object> map) throws Exception {
        return SysAsistDAO.qnaListAll(map);
    }

    @Override
    public SysDTO selectByFromno(Map<String, Object> map) throws Exception {
        return SysAsistDAO.selectByFromno(map);
    }

    @Override
    public void qnaInsert(Map<String, Object> map) throws Exception {
        SysAsistDAO.qnaInsert(map);
    }

    @Override
    public void qnaUpdqte(Map<String, Object> map) throws Exception {
        SysAsistDAO.qnaUpdqte(map);
    }

    @Override
    public void popupDelete(Map<String, Object> map) throws Exception {
        SysAsistDAO.popupDelete(map);
    }

    @Override
    public PopupDTO popupDetail(Map<String, Object> map) throws Exception {
        return SysAsistDAO.popupDetail(map);
    }

    @Override
    public void popupUpdateNofile(Map<String, Object> map) throws Exception {
        SysAsistDAO.popupUpdateNofile(map);
    }

    @Override
    public void popupUpdateWithFile(Map<String, Object> map) throws Exception {
        SysAsistDAO.popupUpdateWithFile(map);
    }

    @Override
    public int exposynCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.exposynCount(map);
    }

    @Override
    public void updateExposeN(Map<String, Object> map) throws Exception {
        SysAsistDAO.updateExposeN(map);
    }

    @Override
    public int bannerTotalCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.bannerTotCount(map);
    }

    @Override
    public int bannerTypeCount(Map<String, Object> map) throws Exception {
        return SysAsistDAO.bannerTypeCount(map);
    }

    @Override
    public List<BannerDTO> bannerListAll(Map<String, Object> map) throws Exception {
        return SysAsistDAO.bannerListAll(map);
    }

    @Override
    public BannerDTO bannerInfo(Map<String, Object> map) throws Exception {
        return SysAsistDAO.bannerInfo(map);
    }

    @Override
    public void bannerWrite(Map<String, Object> map) throws Exception {
        SysAsistDAO.bannerWrite(map);
    }

    @Override
    public BannerDTO bannerDetail(Map<String, Object> map) throws Exception {
        return SysAsistDAO.bannerDetail(map);
    }

    @Override
    public void bannerDelete(Map<String, Object> map) throws Exception {
        SysAsistDAO.bannerDelete(map);
    }

    @Override
    public void bannerUpdate(Map<String, Object> map) throws Exception {
        SysAsistDAO.bannerUpdate(map);
    }

    @Override
    public int orderCheck(Map<String, Object> map) throws Exception {
        return SysAsistDAO.orderCheck(map);
    }

    @Override
    public FwFile getFileInfo(String id) {
        FwFile fwFile = fwFileMapper.selectByPrimaryKey(id);
        return fwFile;
    }

    @Override
    public InputStream getFile(String id) throws IOException {
        InputStream is = simpleFileMapper.load("BANNER", id);
        return is;
    }
}
