package kr.co.finger.damoa.shinhan.agent.handler.command;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.model.msg.*;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaException;
import kr.co.finger.damoa.shinhan.agent.model.AggregateBean;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

import static kr.co.finger.damoa.shinhan.agent.util.DamoaBizUtil.agrregate;

/**
 * 집계대사전문 처리
 * 요청과 응답 모두 유입됨..
 * 테이블 조회하여 계산
 * 수납전문 테이블 필요
 * 수납일자,기관,가상계좌,수납금액,형태(수납,취소)
 */
@Component
public class AggregateMsgCommand extends BasicCommand {
    private String START = "001";

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private DamoaDao damoaDao;
    @Autowired
    private DamoaContext damoaContext;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        AggregateMessage aggregateMessage = (AggregateMessage) msgIF;
        ResultBean result = validateCommons(aggregateMessage, messageFactory,damoaContext,LOG);
        if ("0".equalsIgnoreCase(result.getCode())==false) {
            throw new DamoaException(result.getCode(),result.getMsg());
        }

        result = validateAggregate(aggregateMessage, messageFactory,damoaContext,LOG);
        if ("0".equalsIgnoreCase(result.getCode())==false) {
            throw new DamoaException(result);
        }

        if (MessageCode.AGGREGATE_RESPONSE.equals(Code.createByMsgIF(msgIF))) {
            LOG.info("<<< [" + aggregateMessage.getDesc() + "]" + aggregateMessage);

            CompletableFuture<MsgIF> completableFuture = concurrentMap.get(msgIF.getKey());
            if (completableFuture != null) {
                completableFuture.complete(msgIF);
            }

        } else {
            insertTransDataAcct(damoaDao,getMsgType(aggregateMessage), aggregateMessage.getDealSeqNo(), Constants.RECEIVE_DESTINATION, aggregateMessage.getOriginMessage(), LOG);
            AggregateMessage response = null;
            if (insertRcpSum(damoaDao, aggregateMessage, LOG) == false) {
                response = (AggregateMessage) response(aggregateMessage);
                response.setResCode("V121");
                response.setResMsg("중계센터 SYSTEM 장애");
                LOG.error("집계대사 장애발생.. " + response.getCorpCode() + " " + response.getDealDate());
                response.setupAggregate(aggregateMessage.getDepositTotalNo()
                        , aggregateMessage.getDepositTotalAmount()
                        , aggregateMessage.getDepositCancelNo()
                        , aggregateMessage.getDepostCancelAmount()
                        , aggregateMessage.getFeesNo()
                        , aggregateMessage.getFeesAmount());

            } else {

                LOG.info("<<< [TotalNo]"+aggregateMessage.getDepositTotalNo()
                        +"[TotalAmount]"+aggregateMessage.getDepositTotalAmount()
                        +"[CancelNo]"+aggregateMessage.getDepositCancelNo()
                        +"[CancelAmount]"+aggregateMessage.getDepostCancelAmount()
                        +"[FeesNo]"+aggregateMessage.getFeesNo()
                        +"[FeesAmount]"+aggregateMessage.getFeesAmount()
                );

                response = (AggregateMessage) response(aggregateMessage);
                String corpCode = response.getCorpCode().trim();
                String rcpDate = response.getDealDate().trim();

                //요청온 fgcd에 해당하는 모든 기관의 rcp_hist select
                List<Map<String, Object>> mapList = damoaDao.findAggregateMsgInfo(corpCode, startOfRcpDate(rcpDate), endOfRcpDate(rcpDate));
                if (mapList == null) {
                    LOG.error("집계대사정보를 수집할 수 없음. "+corpCode+"\t"+startOfRcpDate(rcpDate)+"\t"+endOfRcpDate(rcpDate));
                    response.setupAggregate(aggregateMessage.getDepositTotalNo()
                            , aggregateMessage.getDepositTotalAmount()
                            , aggregateMessage.getDepositCancelNo()
                            , aggregateMessage.getDepostCancelAmount()
                            , aggregateMessage.getFeesNo()
                            , aggregateMessage.getFeesAmount());
                } else {
                    AggregateBean bean = agrregate(mapList);

                    LOG.info(">>> [TotalNo]"+bean.getTotalCount()
                            +"[TotalAmount]"+bean.getTotalAmount()
                            +"[CancelNo]"+bean.getCancelCount()
                            +"[CancelAmount]"+bean.getCancelAmount()
                            +"[FeesNo]"+bean.getFeeCount()
                            +"[FeesAmount]"+bean.getFeeAmount()
                    );
                    response.setupAggregate(bean.getTotalCount() + "", bean.getTotalAmount() + "", bean.getCancelCount() + "", bean.getCancelAmount() + "", bean.getFeeCount() + "", bean.getFeeAmount() + "");
                }
            }
            String msgType = getMsgType(response);

            String responseMsg = encode(messageFactory, response);
            nettyProducer.write(responseMsg, new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                    insertTransDataAcct(damoaDao,msgType, aggregateMessage.getDealSeqNo(), Constants.SEND_DESTINATION, responseMsg, LOG);
                }
            });
        }
    }

    private boolean insertRcpSum(DamoaDao damoaDao, AggregateMessage aggregateMessage, Logger LOG) {
        try {
            String corp = aggregateMessage.getCorpCode().trim();
            String date = aggregateMessage.getDealDate().trim();
            Map<String, Object> map = damoaDao.findRcpSum(aggregateMessage);
            if (map == null) {
                damoaDao.insertRcpSum(aggregateMessage);
            } else {
                LOG.info("집계대사 테이블에 이미 존재함. " + aggregateMessage.selectParam());
            }

            return true;
        } catch (ParseException e) {
            LOG.error("집계전문거래일자예외.. ", e);
            LOG.error("ERRORMSG=" + aggregateMessage.getOriginMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            LOG.error("ERRORMSG=" + aggregateMessage.getOriginMessage());
        }

        return false;
    }

    /**
     * @param rcpDate 8자리 yyyyMMdd
     * @return
     */
    private String startOfRcpDate(String rcpDate) {
        return rcpDate + "000000000";
    }

    private String endOfRcpDate(String rcpDate) {
        return rcpDate + "235959999";
    }


    private String nowDate() {
        return DateUtils.toString(new Date(), "yyyyMMdd");
    }


}
