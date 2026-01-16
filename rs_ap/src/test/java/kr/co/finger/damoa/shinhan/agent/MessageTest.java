package kr.co.finger.damoa.shinhan.agent;

import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.model.CancelBean;
import kr.co.finger.damoa.shinhan.agent.model.NotiDetBean;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResaleShinhanApApplication.class)
public class MessageTest {

	@Autowired
	private MessageFactory messageFactory;

	@Autowired
	private DamoaDao damoaDao;

	@Test
	public void contextLoads() {
//		List<Map<String, Object>> rcps = damoaDao.findRcpCanCelInfo("4000000000000000004 ");
//		List<Map<String, Object>> notiMapList = damoaDao.findNotiCanCelInfo("4000000000000000004 ");
		Map<String, Object> result = damoaDao.findChaSimpleInfo("30001001");
		Map<String, NotiDetBean> detBeans = new HashMap<>();

	}

	@Test
	public void findCancel() {
		String dealSeqNo = "0001130";
		String date = "20210317";
		final List<Map<String,Object>> mapList = damoaDao.findCancel(dealSeqNo,date);
		if (mapList == null || mapList.size()==0) {
			System.out.println("수납건이 없음.. "+dealSeqNo+"\t"+date);
		}

		CancelBean cancelBean = new CancelBean(dealSeqNo,date);
		for (Map<String, Object> map : mapList) {
			System.out.println( Maps.getValue(map, "rcpmascd") +"?" + Maps.getValue(map, "notimascd"));
			cancelBean.add(Maps.getValue(map, "rcpmascd"),Maps.getValue(map, "notimascd"));
		}

		cancelBean.getCancelPairs().toString();
	}



}
