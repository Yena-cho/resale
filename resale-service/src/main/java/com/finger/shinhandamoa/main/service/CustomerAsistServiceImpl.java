package com.finger.shinhandamoa.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finger.shinhandamoa.main.dao.CustomerAsistDAO;
import com.finger.shinhandamoa.main.dto.NoticeDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Service
public class CustomerAsistServiceImpl implements CustomerAsistService {


	@Autowired
	private CustomerAsistDAO customerAsistdao;
	
	// 공지사항
	@Override
	public int noticeTotalCount(Map<String, Object> map) throws Exception {
		return customerAsistdao.noticeTotalCount(map);
	}

	@Override
	public int noticeOnlyCount(Map<String, Object> map) throws Exception {
		return customerAsistdao.noticeOnlyCount(map);
	}

	@Override
	public List<NoticeDTO> noticeListAll(Map<String, Object> map) throws Exception {
		return customerAsistdao.noticeListAll(map);
	}
	
	@Override
	public List<NoticeDTO> ajaxNoticeListAll(Map<String, Object> map) throws Exception {
		return customerAsistdao.ajaxNoticeListAll(map);
	}

	@Transactional
	@Override
	public NoticeDTO noticeDetail(long no) throws Exception {
		customerAsistdao.noticeIncrease(no);
		return customerAsistdao.noticeDetail(no);
	}
	
	@Override
	public List<NoticeDTO> nextList(long no, int bbs, int isfix) throws Exception {
		return customerAsistdao.nextList(no, bbs, isfix);
	}

	// 자주하는 질문
	@Override
	public int faqTotalCount(Map<String, Object> map) throws Exception {
		return customerAsistdao.faqTotalCount(map);
	}

	@Override
	public List<NoticeDTO> faqListAll(Map<String, Object> map) throws Exception {
		return customerAsistdao.faqListAll(map);
	}
	
	// QnA
	@Override
	public int qnaTotalCount(Map<String, Object> map) throws Exception {
		return customerAsistdao.qnaTotalCount(map);
	}
	
	@Override
	public List<NoticeDTO> qnaListAll(Map<String, Object> map) throws Exception {
		return customerAsistdao.qnaListAll(map);
	}

	@Override
	public List<NoticeDTO> repleList(Map<String, Object> map) throws Exception {
		return customerAsistdao.repleList(map);
	}

	@Override
	public List<NoticeDTO> qnaNextList(Map<String, Object> map) throws Exception {
		return customerAsistdao.qnaNextList(map);
	}

	@Override
	public void qnaDelete(Map<String, Object> map) throws Exception {
		customerAsistdao.qnaDelete(map);
	}

	@Override
	public int getDailyQnaCount(Map<String, Object> map) throws Exception {
		return customerAsistdao.getDailyQnaCount(map);
	}

	@Override
	public void qnaInsert(Map<String, Object> map) throws Exception {
		customerAsistdao.qnaInsert(map);
	}

	@Override
	public NoticeDTO getPhoneNumber(String code) throws Exception {
		return customerAsistdao.getPhoneNumber(code);
	}

	@Override
	public void qnaUpdateExcute(Map<String, Object> map) throws Exception {
		customerAsistdao.qnaUpdateExcute(map);
		
	}
	@Override
	public void qnaUpdateRemoveFile(Map<String, Object> map) throws Exception {
		customerAsistdao.qnaUpdateRemoveFile(map);
	}

	@Override
	public void qnaUpdateChangeFile(Map<String, Object> map) throws Exception {
		customerAsistdao.qnaUpdateChangeFile(map);
	}

	@Override
	public NoticeDTO qnaDetail(HashMap<String, Object> map) throws Exception {
		return customerAsistdao.qnaDetail(map);
	}


}
