package kr.co.finger.damoa.scheduler.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.finger.damoa.scheduler.model.LayOut;

/**
 * @author  by puki
 * @date    2018. 5. 13.
 * @desc    최초생성
 * @version 
 * 
 */
public interface BillsSendDao {

	// 출력의뢰 기관
	public List<LayOut> selChaCd(HashMap<String, Object> map) throws Exception; 
	// 출력의뢰  데이터 - 공통
	public LayOut printMsg(HashMap<String, Object> map) throws Exception; 
	// 출력의뢰 데이터 - 메시지
	public List<LayOut> printMsgData(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 데이터 - 청구항목
	public List<LayOut> printItemData(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 데이터 생성 - body
	public List<LayOut> printReqData(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 ftp 전송 후 상태코드 수정
	public void updateReqSt(HashMap<String, Object> map) throws Exception;
	// 출력의뢰 데이터 생성 - remark
	public List<LayOut> remarkMsgData(HashMap<String, Object> map) throws Exception;
}
