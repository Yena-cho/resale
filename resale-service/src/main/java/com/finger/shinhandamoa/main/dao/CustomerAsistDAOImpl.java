
package com.finger.shinhandamoa.main.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finger.shinhandamoa.main.dto.NoticeDTO;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
@Repository
public class CustomerAsistDAOImpl implements CustomerAsistDAO {

	private static final Logger logger = LoggerFactory.getLogger(CustomerAsistDAOImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	//QnA 이전글 다음글 가져오기
	@Override
	public List<NoticeDTO> qnaNextList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("CustomerAsist.qnaNextList", map);
	}
	//QnA 답글 가져오기
	@Override
	public List<NoticeDTO> repleList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("CustomerAsist.repleList", map);
	}
	//QnA 총 글 수 세기
	@Override
	public int qnaTotalCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("CustomerAsist.qnaTotalCount", map);
	}
	//QnA 목록
	@Override
	public List<NoticeDTO> qnaListAll(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("CustomerAsist.qnaListAll", map);
	}
	//QnA 글삭제
	@Override
	public void qnaDelete(Map<String, Object> map) throws Exception {
		sqlSession.delete("CustomerAsist.qnaDelete", map);
	}
	//QnA 글쓰기
	@Override
	public void qnaInsert(Map<String, Object> map) throws Exception {
		sqlSession.selectList("CustomerAsist.qnaInsert", map);
	}
	//폰번호 가져오기
	@Override
	public NoticeDTO getPhoneNumber(String code) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("code", code);
		return sqlSession.selectOne("CustomerAsist.getPhoneNumber", map);
		
	}
	// 금일 등록한 QnA 수
	@Override
	public int getDailyQnaCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("CustomerAsist.getDailyQnaCount", map);
	}
	//QnA 수정  실행
	@Override
	public void qnaUpdateExcute(Map<String, Object> map) throws Exception {
		sqlSession.update("CustomerAsist.qnaUpdateExcute", map);
	}
	//qna 파일 삭제
	@Override
	public void qnaUpdateRemoveFile(Map<String, Object> map) throws Exception {
		sqlSession.update("CustomerAsist.qnaUpdateRemoveFile", map);
	}
	//qna 파일 변경
	@Override
	public void qnaUpdateChangeFile(Map<String, Object> map) throws Exception {
		sqlSession.update("CustomerAsist.qnaUpdateChangeFile", map);
	}

	@Override
	public NoticeDTO qnaDetail(HashMap<String, Object> map) throws Exception {
		return sqlSession.selectOne("CustomerAsist.qnaDetail", map);
	}


	//공지사항 총 글수 세기
	@Override
	public int noticeOnlyCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("CustomerAsist.noticeOnlyCount", map);
	}

	
	// 공지사항 total count
	@Override
	public int noticeTotalCount(Map<String, Object> map) throws Exception {

		return sqlSession.selectOne("CustomerAsist.noticeTotalCount", map);
	}

	// 공지사항 목록
	@Override
	public List<NoticeDTO> noticeListAll(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectList("CustomerAsist.noticeListAll", map);
	}
	
	
	
	@Override
	public List<NoticeDTO> ajaxNoticeListAll(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList("CustomerAsist.noticeListAll", map);
	}

	// 공지사항 상세 보기
	@Override
	public NoticeDTO noticeDetail(long no) throws Exception {
		logger.info("DAOIMP-----NOTICEDETAIL");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("no", no);
		return sqlSession.selectOne("CustomerAsist.noticeDetail", map);
	}

	// 공지사항 조회수 증가
	@Override
	public void noticeIncrease(long no) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		logger.info("DAOIMP-----NOTICEINCREASE"+no);
		map.put("no", no);
		sqlSession.update("CustomerAsist.noticeIncrease", map);
	}

	// 공지사항 상세보기 - 이전글, 다음글
	@Override
	public List<NoticeDTO> nextList(long no, int bbs, int isfix) throws Exception {
		
		Map<String, Object> map=new HashMap<String, Object>();
		//map.put("role", role);
		map.put("no", no);
		map.put("bbs", bbs);
		map.put("isfix", isfix);
		return sqlSession.selectList("CustomerAsist.noticeNextList", map);
	}
	
	// 자주하는 질문 total count
	@Override
	public int faqTotalCount(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectOne("CustomerAsist.faqTotalCount", map);
	}

	// 자주하는 질문 목록
	@Override
	public List<NoticeDTO> faqListAll(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectList("CustomerAsist.faqListAll", map);
	}

}

