package kr.co.finger.damoa.shinhan.agent.controller;

import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.msg.AdminMessage;
import kr.co.finger.damoa.model.msg.AggregateMessage;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.VirtualAccountRangeMessage;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.fsm.Context;
import kr.co.finger.damoa.shinhan.agent.handler.command.ReqResSyncCommand;
import kr.co.finger.damoa.shinhan.agent.model.AggregateBean;
import kr.co.finger.damoa.shinhan.agent.model.VaBean;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.command.CommandExecutor;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.co.finger.damoa.shinhan.agent.util.DamoaBizUtil.agrregate;

@RestController
public class GatewayController {
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private MessageFactory messageFactory;
    //    @Autowired
//    private CounterService counterService;
    @Autowired
    private NettyProducer nettyProducer;
    @Autowired
    private DamoaDao damoaDao;
    @Autowired
    private Context context;
    private int index = 10;
    @Value("${damoa.rangeMessage.mode:TEST}")
    private String rangeMessageMode;
    @Value("${damoa.corpCode:FINGER}")
    private String damoaCorpCode;

    private Logger LOG = LoggerFactory.getLogger(getClass());

//    @Autowired
//    public GatewayController(CommandExecutor commandExecutor, MessageFactory messageFactory, CounterService counterService,NettyProducer nettyProducer,DamoaDao damoaDao,Context context) {
//        this.commandExecutor = commandExecutor;
//        this.messageFactory = messageFactory;
//        this.counterService = counterService;
//        this.nettyProducer = nettyProducer;
//        this.damoaDao = damoaDao;
//        this.context = context;
//    }


    @RequestMapping(value = "/send/{id}/", method = RequestMethod.GET, produces = "application/txt")
    public ResponseEntity<String> sendAdminMsg(@PathVariable("id") String id) throws Exception {

        AdminMessage adminMessage = newAdminMessage(id, "");
        LOG.info(">>> [" + adminMessage.getDesc() + "]" + adminMessage);
        String msg = messageFactory.encode(adminMessage);
        nettyProducer.write(msg);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @RequestMapping(value = "/new/virtualaccount/", method = RequestMethod.GET)
    @ResponseBody
    public VaBean requestNewVirtualAccount(
            @RequestParam("type") String type,
            @RequestParam("corpCd") String corpCd,
            @RequestParam("agencyCd") String agencyCd,
            @RequestParam("branchCd") String branchCd,
            @RequestParam("count") int count,
            @RequestParam("remark") String remark
    ) throws Exception {

        LOG.info("범위계좌 요청.... type=" + type + " corpCd=" + corpCd + " agencyCd=" + agencyCd + " branchCd=" + branchCd + " count=" + count + " remark=" + remark);

        if (count <= 0) {
            return new VaBean("잘못된 매수 " + count);
        }

        if (isEmpty(corpCd)) {
            return new VaBean("잘못된 기관코드 " + corpCd);
        }

        Date now = new Date();
        VirtualAccountRangeMessage rangeMessage = new VirtualAccountRangeMessage();
        rangeMessage.setMsgSndRcvCorpCode(damoaCorpCode);
        // DB 조회
        rangeMessage.setDealSeqNo(damoaDao.findDealSeq() + "");
        String dateTime = DateUtils.to14NowString(now);
        rangeMessage.setMsgSndDate(dateTime);
        rangeMessage.setDealStartDate(dateTime);
        rangeMessage.setInstitutionCode(nvl(corpCd));
        rangeMessage.setDeadlineYn("0");        //마감전
        rangeMessage.setOccurGubun("3");
        rangeMessage.setMediaGubun("0");
        if (isEmpty(type)) {
            type = "1";
        }

        rangeMessage.setAccountGubun(type);      //신규
        rangeMessage.setCorpCode(nvl(corpCd));
        if (isEmpty(agencyCd)) {
            agencyCd = "     ";
        }
        rangeMessage.setAgencyCode(agencyCd);
        rangeMessage.setBranchCode(nvl(branchCd));
        rangeMessage.setCount(count + "");
        String ruleMngNo = ruleMngNo(corpCd);
        rangeMessage.setUsrWorkArea1(ruleMngNo);
        rangeMessage.setType(MessageCode.RANGE_ACCOUNT_RESPONSE.getId());
        rangeMessage.setLength(300);
        String msg = messageFactory.encode(rangeMessage);
        LOG.info("MSG]{}[",msg);
        rangeMessage.setOriginMessage(msg);

        VirtualAccountRangeMessage _rangeMessage = decode(msg);
        if (isEmpty(msg)) {
            return new VaBean("범위계좌 전문 생성 실패..");
        }
//        insertVaReq(_rangeMessage,remark);
        // 여기서는 오직 전문 처리만 한다.


        VirtualAccountRangeMessage response = (VirtualAccountRangeMessage) commandExecutor.executeWithResult(ReqResSyncCommand.class, _rangeMessage);
        if (response != null) {
//                    updateVaReq(response);
            return new VaBean(response.getResCode(), response.getStartVirtualAccount(), response.getCount());
        } else {
            // 응답전문을 수신하지 못함.
//                    updateVaReq(_rangeMessage,"0","0");
            return new VaBean("응답전문을 수신하지 못함.", "", "");
        }


    }

    @RequestMapping(value = "/query/aggregate/", method = RequestMethod.GET)
    @ResponseBody
    public String queryAggregateMsg(
            @RequestParam("corpCd") String corpCd,
            @RequestParam("dealDate") String dealDate,
            @RequestParam(value = "messageNo", defaultValue = "-1") long messageNo
    ) throws Exception {
        LOG.info("집계대사 요청.... corpCd=" + corpCd + " dealDate=" + dealDate);
        List<Map<String, Object>> mapList = damoaDao.findAggregateMsgInfo(corpCd, startOfRcpDate(dealDate), endOfRcpDate(dealDate));
        AggregateBean bean = agrregate(mapList);
        AggregateMessage aggregateMessage = newAggregateMessage(dealDate, corpCd, messageNo);
        aggregateMessage.setLength(400);

        aggregateMessage.setupAggregate(bean.getTotalCount() + "", bean.getTotalAmount() + "", bean.getCancelCount() + "", bean.getCancelAmount() + "", bean.getFeeCount() + "", bean.getFeeAmount() + "");
        LOG.info(bean.toString());
        String msg = messageFactory.encode(aggregateMessage, "07001100");
        aggregateMessage.setOriginMessage(msg);

        LOG.info(">>> " + aggregateMessage);
        LOG.info(">>> " + msg);
        AggregateMessage response = (AggregateMessage) commandExecutor.executeWithResult(ReqResSyncCommand.class, aggregateMessage);
        LOG.info("<<< " + response);
        if (response != null) {
            if ("0".equalsIgnoreCase(response.getResCode().trim())) {
                return "성공";
            } else {
                return response.getResCode() + " " + response.getResMsg();

            }
        } else {
            return "응답을 받지 못함.";
        }
    }

    @RequestMapping(value = "/query/aggregates/", method = RequestMethod.GET)
    @ResponseBody
    public String queryAggregateMsgs(
            @RequestParam("corpCd") String corpCd,
            @RequestParam("dealDate") String dealDate,
            @RequestParam(value = "messageNo", defaultValue = "0") long messageNo,
            @RequestParam(value = "interval", defaultValue = "10") long interval,
            @RequestParam(value = "perCount", defaultValue = "20") long perCount
    ) throws Exception {
        List<Map<String, Object>> mapList = damoaDao.findAggregateMsgInfo(corpCd, startOfRcpDate(dealDate), endOfRcpDate(dealDate));
        AggregateBean bean = agrregate(mapList);

        int count = (int) messageNo;

        for (int k = 0; k < interval; k++) {
            for (int i = 0; i < perCount; i++) {
                count++;
                AggregateMessage aggregateMessage = newAggregateMessage(dealDate, corpCd, count);
                aggregateMessage.setLength(400);
                aggregateMessage.setupAggregate(bean.getTotalCount() + "", bean.getTotalAmount() + "", bean.getCancelCount() + "", bean.getCancelAmount() + "", bean.getFeeCount() + "", bean.getFeeAmount() + "");
                String msg = messageFactory.encode(aggregateMessage, "07001100");
                nettyProducer.write(msg);
                LOG.info(">>> " + msg);
                Thread.sleep(1);
            }

            Thread.sleep(1000);
        }


        return "OK";
    }

    @RequestMapping(value = "/change/state/", method = RequestMethod.GET)
    @ResponseBody
    public String changeState(
            @RequestParam("state") String state,
            @RequestParam("corpCd") String corpCd
    ) throws Exception {
        LOG.info("서버상태 설정.." + state);
        if ("ERROR".equalsIgnoreCase(state)) {
            AdminMessage adminMessage = newAdminMessage("005", corpCd);
            adminMessage.setInstitutionCode(adminMessage.getMsgSndRcvCorpCode());
            String msg = messageFactory.encode(adminMessage);
            nettyProducer.write(msg);
            return changeContextState(state);

        } else if ("ERROR_RECOVERY".equalsIgnoreCase(state)) {
            AdminMessage adminMessage = newAdminMessage("006", corpCd);
            adminMessage.setInstitutionCode(adminMessage.getMsgSndRcvCorpCode());
            String msg = messageFactory.encode(adminMessage);
            nettyProducer.write(msg);

            return changeContextState("START");
        } else {
            if ("START".equalsIgnoreCase(state)) {
                // 이렇게 수정될 수도 있음.
                // 상태가 변경이 될 때까지 계속 던진다.
                String now = DateUtils.toDateString(new Date());

                AdminMessage adminMessage = newAdminMessage("001", corpCd);
                String msg = messageFactory.encode(adminMessage);
                nettyProducer.write(msg);

//                damoaDao.findCorpCdStatus()
                return changeContextState("START");
            } else {
                return changeContextState(state);
            }
        }

    }

    private String changeContextState(String state) {
        String before = context.getState();
        context.changeState(state);
        return before + " ==>  " + state;
    }


    private String startOfRcpDate(String rcpDate) {
        return rcpDate + "000000000";
    }

    private String endOfRcpDate(String rcpDate) {
        return rcpDate + "235959999";
    }


    private AggregateMessage newAggregateMessage(String dealDate, String corpCode, long inputMessageNo) {
        long messageNo = inputMessageNo > 0 ? inputMessageNo : damoaDao.findDealSeq();

        Date now = new Date();
        String dateTime = DateUtils.to14NowString(now);

        AggregateMessage msg = new AggregateMessage();
        msg.setSystemId("VAS");
        msg.setMsgSndRcvCorpCode(damoaCorpCode);
        msg.setSndRcvFlag("1");
        msg.setMsgTypeCode("0700");
        msg.setDealTypeCode("1100");
        msg.setHandlingAgencyCode("0");
        msg.setDealSeqNo(String.valueOf(messageNo));
        msg.setMsgSndDate(dateTime);
        msg.setDealStartDate(dateTime);
        msg.setResCode("0000");
        msg.setUsrWorkArea1("");
        msg.setUsrWorkArea2("");
        msg.setDeadlineYn("0");
        msg.setOccurGubun("3");
        msg.setMediaGubun("0");
        msg.setInstitutionCode(corpCode);
        msg.setTerminalInfo("0133600000000000000000000");
        msg.setResMsg("");
        msg.setCommonNetworkNo("003PIS8692739");

        msg.setDealDate(dealDate);
        msg.setCorpCode(corpCode);
        return msg;


    }

    private VirtualAccountRangeMessage decode(String msg) {
        return (VirtualAccountRangeMessage) messageFactory.decode(msg);
    }


    private String ruleMngNo(String corpCd) {
        return corpCd + "0" + DateUtils.to6TimeString(new Date());
    }

    private String nvl(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    private boolean isEmpty(String value) {
        if (value == null || "".equalsIgnoreCase(value)) {
            return true;
        } else {
            return false;
        }
    }

    private AdminMessage newAdminMessage(String adminInfo, String corpCode) {
        Date now = new Date();
        String date = DateUtils.toString(now, "yyyyMMddHHmmss");
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setSystemId("VAS");
        adminMessage.setType("08101100");
        adminMessage.setLength(300);
        adminMessage.setMsgSndRcvCorpCode("20007481");
        adminMessage.setSndRcvFlag("1");
        adminMessage.setMsgTypeCode("0800");
        adminMessage.setDealTypeCode("1100");
        adminMessage.setDealSeqNo(damoaDao.findDealSeq() + "");
        adminMessage.setHandlingAgencyCode("00000000");
        adminMessage.setMsgSndDate(date);
        adminMessage.setResCode("0000");
        adminMessage.setResMsg("");
        adminMessage.setUsrWorkArea1("");
        adminMessage.setUsrWorkArea2("");
        adminMessage.setDeadlineYn("0");
        adminMessage.setOccurGubun("3");
        adminMessage.setMediaGubun("0");
        adminMessage.setInstitutionCode(corpCode);
        adminMessage.setTerminalInfo("0176900000000000000000000");
        adminMessage.setCommonNetworkNo("026");
        adminMessage.setDealStartDate(date);
        adminMessage.setAdminInfo(adminInfo);
        adminMessage.setDealDate(DateUtils.toString(now, "yyyyMMdd"));
        adminMessage.setHolidayCode("0");
        adminMessage.setSafCount("0");
        adminMessage.setFilter("");
        adminMessage.setDesc("관리전문 " + Damoas.checkAdminInfo(adminInfo));
        return adminMessage;
    }


    @RequestMapping(value = "/output-queue/clear/", method = RequestMethod.GET)
    @ResponseBody
    public String clearQueue() {
        nettyProducer.clearQueue();
        System.out.println("@RequestMapping(path=\"/output-queue/clear\")");

        return "";
    }


    private Map<String, Bean> aggregate(List<Map<String, Object>> mapList) {

        Map<String, Bean> map = new HashMap<>();
        for (Map<String, Object> _map : mapList) {
            String vano = Maps.getValue(_map, "VANO");
            String amount = Maps.getValue(_map, "PAYITEMAMT");
            String corpCode = Maps.getValue(_map, "CHACD");
            Long _amount = Long.valueOf(amount);
            if (map.containsKey(vano)) {
                Bean bean = map.get(vano);
                bean.aLong = bean.aLong + _amount;
                map.put(vano, bean);
            } else {
                map.put(vano, new Bean(corpCode, _amount));
            }
        }

        return map;

    }

    private class Bean {
        public String corpCode;
        public Long aLong;

        public Bean(String corpCode, Long aLong) {
            this.corpCode = corpCode;
            this.aLong = aLong;
        }
    }


}
