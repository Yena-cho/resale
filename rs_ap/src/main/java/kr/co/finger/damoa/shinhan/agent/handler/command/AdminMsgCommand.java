package kr.co.finger.damoa.shinhan.agent.handler.command;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.model.msg.AdminMessage;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaException;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * 관리전문은 관리응답전문생성...
 * 관리응답전문은 무시...
 * 관리전문은 저장하지 않음.
 *
 */
@Component
public class AdminMsgCommand extends BasicCommand {
    private String START = "001";

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private DamoaContext damoaContext;

    @Autowired
    private DamoaDao damoaDao;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        AdminMessage input = (AdminMessage) msgIF;

        ResultBean result = validateCommons(input, messageFactory,damoaContext,LOG);
        insertTransDataAcct(damoaDao,getMsgType(input), input.getDealSeqNo(), Constants.RECEIVE_DESTINATION, input.getOriginMessage(), LOG);
        LOG.info("validateCommons "+result);
        if ("0".equalsIgnoreCase(result.getCode())==false) {
            throw new DamoaException(result);
        }

        result = validateAdmin(input,messageFactory,damoaContext,LOG);
        LOG.info("validateAdmin "+result);
        if ("0".equalsIgnoreCase(result.getCode())==false) {
            throw new DamoaException(result);
        }

        if (MessageCode.ADMIN_RESPONSE.equals(Code.createByMsgIF(msgIF))) {
            // 관리응답전문은 무시한다.
            LOG.info("<<< [" + input.getDesc() + "]["+Damoas.checkAdminInfo(input.getAdminInfo().trim())+"]");

            String adminInfo = input.getAdminInfo();
            String institutionCode = input.getInstitutionCode();
            if ("001".equalsIgnoreCase(adminInfo) || "002".equalsIgnoreCase(adminInfo)){
                LOG.info("다모아에서 보낸 (재)개시응답전문 입력.. "+institutionCode);
                damoaContext.insertCorpCdStatus(institutionCode,"START");
            } else if ("004".equalsIgnoreCase(adminInfo)) {
                LOG.info("다모아에서 보낸 종료전문 입력처리.. " + institutionCode);
                if ("20007481".equalsIgnoreCase(institutionCode)) {
                    damoaContext.updateAllFinishStatus("FINISH");
                }else {
                    damoaContext.insertCorpCdStatus(institutionCode, "FINISH");
                }
            } else {
                LOG.info("딱히 처리할 필요없는 통신망전문 " + institutionCode);
            }

        } else {
            LOG.info("<<< [" + input.getDesc() + "]["+Damoas.checkAdminInfo(input.getAdminInfo().trim())+"]");
            AdminMessage response = (AdminMessage) response(input);

            //개시된 정보를 DB 입력..
            String adminInfo = input.getAdminInfo();
            String institutionCode = input.getInstitutionCode();

            if ("001".equalsIgnoreCase(adminInfo) || "002".equalsIgnoreCase(adminInfo)){
                LOG.info("은행에서 보낸 (재)개시전문 입력처리.. "+institutionCode);
                //TODO 모든 기관 insert???
                damoaContext.insertCorpCdStatus(institutionCode,"START");
            } else if ("004".equalsIgnoreCase(adminInfo)) {
                LOG.info("은행에서 보낸 종료전문 입력처리.. " + institutionCode);

                if ("20007481".equalsIgnoreCase(institutionCode)) {
                    damoaContext.updateAllFinishStatus("FINISH");
                }else {
                    damoaContext.insertCorpCdStatus(institutionCode, "FINISH");
                }
            } else {
                LOG.info("딱히 처리할 필요없는 통신망전문 " + institutionCode);
            }

            String responseMsg = encode(messageFactory, response);
            String msgType = getMsgType(response);
            nettyProducer.write(responseMsg, new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                    insertTransDataAcct(damoaDao,msgType, response.getDealSeqNo(), Constants.SEND_DESTINATION, responseMsg, LOG);
                }
            });
        }

    }

}
