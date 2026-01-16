package com.finger.shinhandamoa.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.main.dto.NoticeDTO;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 * @version
 * 
 */
public interface CustomerAsistService {

	// 공지사항
	public int noticeTotalCount(Map<String, Object> map) throws Exception;

	public int noticeOnlyCount(Map<String, Object> map) throws Exception;

	public List<NoticeDTO> noticeListAll(Map<String, Object> map) throws Exception;

	public List<NoticeDTO> ajaxNoticeListAll(Map<String, Object> map) throws Exception;

	public NoticeDTO noticeDetail(long no) throws Exception;

	public List<NoticeDTO> nextList(long no, int bbs, int isfix) throws Exception;

	// 자주하는 질문
	public int faqTotalCount(Map<String, Object> map) throws Exception;

	public List<NoticeDTO> faqListAll(Map<String, Object> map) throws Exception;

	// QnA
	public int qnaTotalCount(Map<String, Object> map) throws Exception;

	public List<NoticeDTO> qnaListAll(Map<String, Object> map) throws Exception;

	public List<NoticeDTO> repleList(Map<String, Object> map) throws Exception;

	public List<NoticeDTO> qnaNextList(Map<String, Object> map) throws Exception;

	public void qnaDelete(Map<String, Object> map) throws Exception;

	public int getDailyQnaCount(Map<String, Object> map) throws Exception;

	public void qnaInsert(Map<String, Object> map) throws Exception;

	public void qnaUpdateExcute(Map<String, Object> map) throws Exception;

	public NoticeDTO getPhoneNumber(String code) throws Exception;

	public void qnaUpdateRemoveFile(Map<String, Object> map) throws Exception;

	public void qnaUpdateChangeFile(Map<String, Object> map) throws Exception;

	NoticeDTO qnaDetail(HashMap<String, Object> map) throws Exception;
}
