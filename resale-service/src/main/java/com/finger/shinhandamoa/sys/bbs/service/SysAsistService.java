package com.finger.shinhandamoa.sys.bbs.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.data.table.model.FwFile;
import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;
import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;

public interface SysAsistService {
	//공지사항 관리
	public List<SysDTO> noticeListAll(Map<String, Object> map) throws Exception;
	public int noticeTotalCount(Map<String, Object> map) throws Exception;
	public void doNoticeWrite(Map<String, Object> map) throws Exception;
	public SysDTO selectByNo(Map<String, Object> map) throws Exception;
	public void noticeUpdate(Map<String, Object> map) throws Exception;
	
	//FAQ관리
	public List<SysDTO> faqListAll(Map<String, Object> map) throws Exception;
	public int faqTotalCount(Map<String, Object> map) throws Exception;
	public void doFaqWrite(Map<String, Object> map) throws Exception;
	public void faqUpdate(Map<String, Object> map) throws Exception;
	
	//QNA관리
	public int qnaTotalCount(Map<String, Object> map) throws Exception;
	public List<SysDTO> qnaListAll(Map<String, Object> map) throws Exception;
	public SysDTO selectByFromno(Map<String, Object> map) throws Exception;
	public void qnaInsert(Map<String, Object> map) throws Exception;
	public void qnaUpdqte(Map<String, Object> map) throws Exception;
	
	
	//popup관리
	public int popupTotalCount(Map<String, Object> map) throws Exception;
	public List<PopupDTO> popupListAll(Map<String, Object> map) throws Exception;
	public void popupWrite(Map<String, Object> map) throws Exception;
	public PopupDTO popupDetail(Map<String, Object> map) throws Exception;
	public void popupDelete(Map<String, Object> map) throws Exception;
	public void popupUpdateNofile(Map<String, Object> map) throws Exception;
	public void popupUpdateWithFile(Map<String, Object> map) throws Exception;
	public int exposynCount(Map<String, Object> map) throws Exception;
	public void updateExposeN(Map<String, Object> map) throws Exception;

	/**
	 * 배너 관리
	 */
	public int bannerTotalCount(Map<String, Object> map) throws Exception;
	public int bannerTypeCount(Map<String, Object> map) throws Exception;
	public List<BannerDTO> bannerListAll(Map<String, Object> map) throws Exception;
	public BannerDTO bannerInfo(Map<String, Object> map) throws Exception;
	public void bannerWrite(Map<String, Object> map) throws Exception;
	public BannerDTO bannerDetail(Map<String, Object> map) throws Exception;
	public void bannerDelete(Map<String, Object> map) throws Exception;
	public void bannerUpdate(Map<String, Object> map) throws Exception;
	public int orderCheck(Map<String, Object> map) throws Exception;

	public FwFile getFileInfo(String id) throws Exception;
	public InputStream getFile(String id) throws Exception;
}
