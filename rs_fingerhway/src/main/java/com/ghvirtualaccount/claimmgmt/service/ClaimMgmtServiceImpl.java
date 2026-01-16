package com.ghvirtualaccount.claimmgmt.service;

import com.ghvirtualaccount.claimmgmt.dao.ClaimMgmtDao;
import com.ghvirtualaccount.cmmn.StrUtil;
import com.ghvirtualaccount.vo.*;
import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("claimMgmtService")
public class ClaimMgmtServiceImpl implements ClaimMgmtService {

	private static Logger logger = LoggerFactory.getLogger(ClaimMgmtServiceImpl.class);
	
	@Resource(name="claimMgmtDao")
	private ClaimMgmtDao claimMgmtDao;
	
    @Value("20007421")
    private String serviceId;
	
	@Override
	public int getPayerInfoMasterListCnt(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getPayerInfoMasterListCnt start ========");
		return claimMgmtDao.selectPayerInfoMasterListCnt(paramVO);
		
	}
	
	@Override
	public List<PayerInfoMasterInfoVO> getPayerInfoMasterList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getPayerInfoMasterList start ========");
		return claimMgmtDao.selectPayerInfoMasterList(paramVO);
		
	}
	
	@Override
	public int getAvailAcntCnt() throws Exception{
		
		logger.debug("========= getAvailAcntCnt start ========");
		return claimMgmtDao.selectAvailAcntCnt();
		
	}
	
	@Override
	public void regPayerInfo(List<List<Object>> itemList, String userId) throws Exception{
		
		logger.debug("========= regPayerInfo start ========");
	
		TbPayerInfoMasterVO tbPayerInfoMasterVO = new TbPayerInfoMasterVO();
		int claimCnt = itemList.size()-1;
//		1. 청구마스터 Insert - On demand 배치로 처리 할 경우,컨트롤러에서 처리 하여야 함. 
		tbPayerInfoMasterVO.setPayerInfoId(claimMgmtDao.selectSeqPayerInfoId());
		tbPayerInfoMasterVO.setClaimCnt(new BigDecimal(claimCnt));
		tbPayerInfoMasterVO.setRegUserId(userId);
		
		claimMgmtDao.insertTbPayerInfoMaster(tbPayerInfoMasterVO);
		
//		2. 사용 가능한 가상계좌 조회
		//해당 수량만 획득하기 위함
		
		List<TbVirtualAcntVO> tbVirtualAcntList = claimMgmtDao.selectVirtualAcntNotUsed(claimCnt);
		logger.debug("========= 사용 가능한 가상계좌 조회 End ========");
//		int acntIdx = 0;
		String tString = "";	//금액의 콤마를 제거하기 위한 변수
		StrUtil strUtil = new StrUtil();

		//양식 체크를 위하여 itemList의 0번째는 해더가 들어가 있으므로, 1번째부터 처리 한다.
		logger.info("create payer info");
		List<TbPayerInfoVO> voList = new ArrayList<>();
		for (int i=1;i<itemList.size();i++) {
			int acntIdx = i-1;
//		3. 청구건 Insert
			List<Object> item = itemList.get(i);
			TbPayerInfoVO tbPayerInfoVO = new TbPayerInfoVO();
			//0 : 차량번호
			tbPayerInfoVO.setCarNum((String)item.get(0));
			//1 : 차량주
			tbPayerInfoVO.setCarOwnerNm((String)item.get(1));
			//2 : 납부기한
			tString = ((String)item.get(2)).replaceAll("\\.", "");
			tbPayerInfoVO.setPayDueDt(tString);
			//3 : 주소
			tbPayerInfoVO.setCarOwnerAddr((String)item.get(3));
			//4 : 지로번호
			tbPayerInfoVO.setGiroNum((String)item.get(4));
			//5 : 고객조회번호
			tbPayerInfoVO.setCustInqNum((String)item.get(5));
//			logger.debug((String)item.get(6));
//			logger.debug(strUtil.getAmtString((String)item.get(6)));
			//6 : 총금액
			tbPayerInfoVO.setTotAmt(new BigDecimal(strUtil.getAmtString((String)item.get(6))));
			//7 : 고지금&코드
			tbPayerInfoVO.setNotiAmtCd((String)item.get(7));
			//8 : 지로코드
			tbPayerInfoVO.setGiroCd((String)item.get(8));
			//9 : 발생일시1
			tbPayerInfoVO.setOccurDttm1((String)item.get(9));
			//10 : 발생원인1
			tbPayerInfoVO.setOccurReason1((String)item.get(10));
			//11 : 통행요금1 - 1번 항목만 통행장소와 위치가 바껴 있다.
			tbPayerInfoVO.setPassAmt1(new BigDecimal(strUtil.getAmtString((String)item.get(11))));
			//12 : 통행장소1 - 1번 항목만 통행요금과 위치가 바껴 있다.
			tbPayerInfoVO.setPassPlace1((String)item.get(12));
			//13 : 발생일시2
			tbPayerInfoVO.setOccurDttm2((String)item.get(13));
			//14 : 발생원인2
			tbPayerInfoVO.setOccurReason2((String)item.get(14));
			//15 : 통행장소2
			tbPayerInfoVO.setPassPlace2((String)item.get(15));
			//16 : 통행요금2
			tbPayerInfoVO.setPassAmt2(new BigDecimal(strUtil.getAmtString((String)item.get(16))));
			//17 : 발생일시3
			tbPayerInfoVO.setOccurDttm3((String)item.get(17));
			//18 : 발생원인3
			tbPayerInfoVO.setOccurReason3((String)item.get(18));
			//19 : 통행장소3
			tbPayerInfoVO.setPassPlace3((String)item.get(19));
			//20 : 통행요금3
			tbPayerInfoVO.setPassAmt3(new BigDecimal(strUtil.getAmtString((String)item.get(20))));
			//21 : 발생일시4
			tbPayerInfoVO.setOccurDttm4((String)item.get(21));
			//22 : 발생원인4
			tbPayerInfoVO.setOccurReason4((String)item.get(22));
			//23 : 통행장소4
			tbPayerInfoVO.setPassPlace4((String)item.get(23));
			//24 : 통행요금4
			tbPayerInfoVO.setPassAmt4(new BigDecimal(strUtil.getAmtString((String)item.get(24))));
			//25 : 발생일시5
			tbPayerInfoVO.setOccurDttm5((String)item.get(25));
			//26 : 발생원인5
			tbPayerInfoVO.setOccurReason5((String)item.get(26));
			//27 : 통행장소5
			tbPayerInfoVO.setPassPlace5((String)item.get(27));
			//28 : 통행요금5
			tbPayerInfoVO.setPassAmt5(new BigDecimal(strUtil.getAmtString((String)item.get(28))));
			//29 : 발생일시6
			tbPayerInfoVO.setOccurDttm6((String)item.get(29));
			//30 : 발생원인6
			tbPayerInfoVO.setOccurReason6((String)item.get(30));
			//31 : 통행장소6
			tbPayerInfoVO.setPassPlace6((String)item.get(31));
			//32 : 통행요금6
			tbPayerInfoVO.setPassAmt6(new BigDecimal(strUtil.getAmtString((String)item.get(32))));
			//33 : 발생일시7
			tbPayerInfoVO.setOccurDttm7((String)item.get(33));
			//34 : 발생원인7
			tbPayerInfoVO.setOccurReason7((String)item.get(34));
			//35 : 통행장소7
			tbPayerInfoVO.setPassPlace7((String)item.get(35));
			//36 : 통행요금7
			tbPayerInfoVO.setPassAmt7(new BigDecimal(strUtil.getAmtString((String)item.get(36))));
			//37 : 발생일시8
			tbPayerInfoVO.setOccurDttm8((String)item.get(37));
			//38 : 발생원인8
			tbPayerInfoVO.setOccurReason8((String)item.get(38));
			//39 : 통행장소8
			tbPayerInfoVO.setPassPlace8((String)item.get(39));
			//40 : 통행요금8
			tbPayerInfoVO.setPassAmt8(new BigDecimal(strUtil.getAmtString((String)item.get(40))));
			//41 : 발생일시9
			tbPayerInfoVO.setOccurDttm9((String)item.get(41));
			//42 : 발생원인9
			tbPayerInfoVO.setOccurReason9((String)item.get(42));
			//43 : 통행장소9
			tbPayerInfoVO.setPassPlace9((String)item.get(43));
			//44 : 통행요금9
			tbPayerInfoVO.setPassAmt9(new BigDecimal(strUtil.getAmtString((String)item.get(44))));
			//45 : 발생일시10
			tbPayerInfoVO.setOccurDttm10((String)item.get(45));
			//46 : 발생원인10
			tbPayerInfoVO.setOccurReason10((String)item.get(46));
			//47 : 통행장소10
			tbPayerInfoVO.setPassPlace10((String)item.get(47));
			//48 : 통행요금10
			tbPayerInfoVO.setPassAmt10(new BigDecimal(strUtil.getAmtString((String)item.get(48))));
			//49 : 운행기간
			tbPayerInfoVO.setRunDt((String)item.get(49));
			//납부자정보ID
			tbPayerInfoVO.setPayerInfoId(tbPayerInfoMasterVO.getPayerInfoId());
			//가상계좌번호
			tbPayerInfoVO.setVirtualAcntNum(tbVirtualAcntList.get(acntIdx).getVirtualAcntNum());
			//거래번호
			tbPayerInfoVO.setTrno(claimMgmtDao.selectSeqTrno());
			voList.add(tbPayerInfoVO);

//			claimMgmtDao.insertTbPayerInfo(tbPayerInfoVO);
		}

		logger.info("insert payer info");
		claimMgmtDao.batchInsertTbPayerInfo(voList);

//		for (TbPayerInfoVO tbPayerInfoVO: voList) {
//			claimMgmtDao.insertTbPayerInfo(tbPayerInfoVO);
//		}


		logger.info("create aegis");
//		4. 인터페이스 테이블 Insert
		List<AegisTransMsgVO> voList2 = new ArrayList<>();
		for (TbPayerInfoVO tbPayerInfoVO: voList) {
			AegisTransMsgVO aegisTransMsgVO = new AegisTransMsgVO();
			
			aegisTransMsgVO.setRecordtype("D");
			aegisTransMsgVO.setServiceid(serviceId);
			aegisTransMsgVO.setTrno(tbPayerInfoVO.getTrno());
			aegisTransMsgVO.setVano(tbPayerInfoVO.getVirtualAcntNum());
			aegisTransMsgVO.setRcpenddt(tbPayerInfoVO.getPayDueDt());
			aegisTransMsgVO.setRcpprtenddt(tbPayerInfoVO.getPayDueDt());
			//고객명 길이 30바이트로 제한
			String cusnm = strUtil.strCut(tbPayerInfoVO.getCarOwnerNm(), 30);
			aegisTransMsgVO.setCusnm(cusnm);
			aegisTransMsgVO.setContent1(tbPayerInfoVO.getCustInqNum());
			aegisTransMsgVO.setContent2(tbPayerInfoVO.getOccurDttm1());
			aegisTransMsgVO.setChatrno(tbPayerInfoVO.getTrno());
			aegisTransMsgVO.setAdjfigrpcd("");
			aegisTransMsgVO.setPayitemnm("경기고속도로 미수납 통행요금");
			aegisTransMsgVO.setPayitemamt(tbPayerInfoVO.getTotAmt().toString());
			aegisTransMsgVO.setRegseq(claimMgmtDao.selectRegSeq().toString());
//			logger.debug("aegisTransMsgVO : " +  aegisTransMsgVO.getRegseq());

			voList2.add(aegisTransMsgVO);
		}

		logger.info("insert aegis");
//		for(AegisTransMsgVO aegisTransMsgVO : voList2) {
//			claimMgmtDao.insertAEGISTRANSMSG(aegisTransMsgVO);
//		}
		claimMgmtDao.batchInsertAEGISTRANSMSG(voList2);
			
//		5. 가상계좌 사용중 Update
		logger.info("update va");
		for(int acntIdx=0; acntIdx < voList2.size(); acntIdx++) {
			TbPayerInfoVO tbPayerInfoVO = voList.get(acntIdx);

			TbVirtualAcntVO tbVirtualAcntVO = tbVirtualAcntList.get(acntIdx);
			tbVirtualAcntVO.setUseYn("Y");
			tbVirtualAcntVO.setPayDuDt(tbPayerInfoVO.getPayDueDt());
//			claimMgmtDao.updateTbVirtualAcnt(tbVirtualAcntVO);
			
			//다음 가상계좌를 사용하기 위하여 Index 증가
//			acntIdx++;
		}

//		for(int acntIdx=0; acntIdx < voList2.size(); acntIdx++) {
//			claimMgmtDao.updateTbVirtualAcnt(tbVirtualAcntList.get(acntIdx));
//		}
		claimMgmtDao.batchUpdateTbVirtualAcnt(tbVirtualAcntList);
		
		logger.info("update master");
		
//		6. 청구 마스터 Update
//		1. 청구마스터 Insert		
		tbPayerInfoMasterVO.setStatus("1");
		claimMgmtDao.updateTbPayerInfoMaster(tbPayerInfoMasterVO);
	}
	
	@Override
	public List<PayerInfoVO> getPayerInfoList(String payerInfoId) throws Exception{
		
		logger.debug("========= getPayerInfoList start ========");
		return claimMgmtDao.selectPayerInfoList(payerInfoId);
		
	}

	@Override
	public Map<String,Object> validatePayerInfo(List<List<Object>> itemList) throws Exception{
		
		logger.debug("========= validatePayerInfo start ========");
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String[] columnName = {"A","B","C","D","E","F","G","H","I","J","K","L","M"};
		String[] headerStr = {"차량번호","차량주","납부기한","주소","지로번호","고객조회번호","총금액","고지금&코드","지로코드",
				"발생일시1","발생원인1","통행요금1","통행장소1","발생일시2","발생원인2","통행장소2","통행요금2",
				"발생일시3","발생원인3","통행장소3","통행요금3","발생일시4","발생원인4","통행장소4","통행요금4",
				"발생일시5","발생원인5","통행장소5","통행요금5","발생일시6","발생원인6","통행장소6","통행요금6",
				"발생일시7","발생원인7","통행장소7","통행요금7","발생일시8","발생원인8","통행장소8","통행요금8",
				"발생일시9","발생원인9","통행장소9","통행요금9","발생일시10","발생원인10","통행장소10","통행요금10",
				"운헁기간"};
		
		//컬럼수 50개 체크 - itemList의 0번째는 타이틀
		List<Object> item = itemList.get(0);
		for(int i=0;i<item.size();i++){
			if(!headerStr[i].equals((String)item.get(i))){
				returnMap.put("retCode", "1001");
				returnMap.put("retMsg", "청구등록 형식에 맞는 엑셀파일을 선택해주시기 바랍니다.");
				
				return returnMap;
			}
		}
		
		//필수값 체크 - itemList의 0번째는 타이틀
		for(int i=1;i<itemList.size();i++){
			//12번째 컬럼까지 빈 값 있으면 오류
			for(int j=0;j<13;j++){
				if(StringUtil.isEmpty((String)itemList.get(i).get(j))){
					returnMap.put("retCode", "1002");
					returnMap.put("retMsg", "필수항목 중 빈 값이 존재합니다. 필수항목 입력 후 다시 업로드 해주시기 바랍니다. ["+(i+3)+"행"+columnName[j]+"열]");
					
					return returnMap;
				}
			}
		}
		
		//가상계좌 수량 체크
		int vcntCnt = claimMgmtDao.selectAvailAcntCnt();
		
		//청구 등록 건수보다 사용 가능한 가상계좌 번호가 적을 경우 오류  - itemList의 0번째는 타이틀이므로 수량 하나 빼준다.
		if(vcntCnt<(itemList.size()-1)){
			returnMap.put("retCode", "1003");
			returnMap.put("retMsg", "사용 가능한 가상계좌 번호가 부족 합니다.");
			
			return returnMap;
		}

		//중복데이터 체크 - itemList의 0번째는 타이틀
		for(int i=1;i<itemList.size();i++){
			TbPayerInfoVO tbPayerInfoVO = new TbPayerInfoVO();

			final String payDueDateString = ((String)itemList.get(i).get(2)).replace(".", "");
			tbPayerInfoVO.setPayDueDt(payDueDateString);
			tbPayerInfoVO.setCustInqNum((String)itemList.get(i).get(5));
			tbPayerInfoVO.setOccurDttm1((String)itemList.get(i).get(9));
			
			final int chkCnt = claimMgmtDao.selectPayerInfo(tbPayerInfoVO);
			
			if(chkCnt > 0){
				returnMap.put("retCode", "1004");
				returnMap.put("retMsg", "이미 등록된 청구건이 존재 합니다. 청구목록을 확인 후 다시 업로드 해주시기 바랍니다. ["+(i+3)+"행]");
				
				return returnMap;
			}
		}
		
		return null;
		
	}
}

