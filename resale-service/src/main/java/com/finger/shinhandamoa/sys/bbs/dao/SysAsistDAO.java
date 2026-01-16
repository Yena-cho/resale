package com.finger.shinhandamoa.sys.bbs.dao;

import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;
import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public interface SysAsistDAO {

	// 공지사항
	public List<SysDTO> noticeListAll(Map<String, Object> map) throws Exception;
	public int noticeTotalCount(Map<String, Object> map) throws Exception;
	public void doNoticeWrite(Map<String, Object> map) throws Exception;
	public SysDTO selectByNo(Map<String, Object> map) throws Exception;
	public void noticeUpdate(Map<String, Object> map) throws Exception;
	
	// 자주하는 질문
	public List<SysDTO> faqListAll(Map<String, Object> map) throws Exception;
	public int faqTotalCount(Map<String, Object> map) throws Exception;
	public void doFaqWrite(Map<String, Object> map) throws Exception;
	public void faqUpdate(Map<String, Object> map) throws Exception;

	
	//QnA
	public List<SysDTO> qnaListAll(Map<String, Object> map) throws Exception;
	public int qnaTotalCount(Map<String, Object> map) throws Exception;
	public SysDTO selectByFromno(Map<String, Object> map) throws Exception;
	public void qnaUpdqte(Map<String, Object> map) throws Exception;
	public void qnaInsert(Map<String, Object> map) throws Exception;
	
	//popup
	public int popupTotCount(Map<String, Object> map) throws Exception;
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
	public int bannerTotCount(Map<String, Object> map) throws Exception;
	public int bannerTypeCount(Map<String, Object> map) throws Exception;
	public List<BannerDTO> bannerListAll(Map<String, Object> map) throws Exception;
	public BannerDTO bannerInfo(Map<String, Object> map) throws Exception;
	public void bannerWrite(Map<String, Object> map) throws Exception;
	public BannerDTO bannerDetail(Map<String, Object> map) throws Exception;
	public void bannerDelete(Map<String, Object> map) throws Exception;
	public void bannerUpdate(Map<String, Object> map) throws Exception;
	public int orderCheck(Map<String, Object> map) throws Exception;
}
