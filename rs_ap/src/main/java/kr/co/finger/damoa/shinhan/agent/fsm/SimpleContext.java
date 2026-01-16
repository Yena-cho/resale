package kr.co.finger.damoa.shinhan.agent.fsm;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.handler.command.*;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.command.Command;
import kr.co.finger.msgagent.command.CommandFactory;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * FSM 패턴에서 Context를 구현함.
 * 처음은 FINISH State 로 시작한다.
 * 적당한 상황에서 State가 변경이 된다.
 * 각 State 마다 처리할 수 있는 전문을 정의하고 전문을 처리한다.
 * 전문에 따라 처리하는 것은 Command 패턴을 이용하였고 Context에서 일괄 처리한다.
 */
@Component
public class SimpleContext implements Context {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private State state = StateFactory.getInstance().findState("START");  // SimpleState 클래스를 가져옴
    private ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap;
    private MessageFactory messageFactory;
    private NettyProducer nettyProducer;
    private CommandFactory commandFactory;
    private DamoaContext damoaContext;

    @Autowired
    private DamoaDao damoaDao;

    public SimpleContext(ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, MessageFactory messageFactory, NettyProducer nettyProducer, CommandFactory commandFactory,DamoaContext damoaContext) {
        this.concurrentMap = concurrentMap;
        this.messageFactory = messageFactory;
        this.nettyProducer = nettyProducer;
        this.commandFactory = commandFactory;
        this.damoaContext = damoaContext;
    }

    public void changeState(State state){
        LOGGER.info("[상태변경]" + this.state.getStateCode() + " ==> " + state.getStateCode());
        this.state = state;
//        state.preExecute(this);
    }

    @Override
    public void changeState(Code state) {
        changeState(state.getId());
    }
    public void changeState(String state) {
        changeState(StateFactory.getInstance().findState(state));

    }
    @Override
    public void execute(MsgIF msgIF) throws Exception {
        LOGGER.info("******************** simplecontext execute ********************");

        // SimpleState 클래스의 execute 메소드를 호출함
        state.execute(this, msgIF,nettyProducer,messageFactory,damoaContext);
    }

    @Override
    public boolean isValid() {
        return state.isValid();
    }

    public String getState() {
        return state.getStateCode();
    }

    @Override
    public void executeCommand(MsgIF request) throws Exception {
        // 수신한 전문의 종류를 확인 후 개시, 종료, 수취조회, 입금통지, 취소 전문에 해당하는 클래를 가져온다.
        final Command command = findCommand(request);
        LOGGER.info("******************** simplecontext executeCommand ********************");
        if(command == null) {
            LOGGER.warn("{}에 해당하는 Command가 없음", request.getType());
            String msg = request.getType()+" 에 해당하는 Command가 없음";
            throw new Exception(msg);
        }

        final MsgIF response = response(request);
        if(!command.executeWithPush(request, response)) {
            command.execute(request, concurrentMap, nettyProducer);
            return;
        }

        // 응답전문변환
        LOGGER.debug("== 응답전문변환 전 : {}", response.toString());
        final String responseMsg = encode(messageFactory, response);
        
        // 전송.
        final String msgTypeCode = getMsgType(response);
        nettyProducer.write(responseMsg, new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                insertTransDataAcct(damoaDao, msgTypeCode, request.getDealSeqNo(), Constants.SEND_DESTINATION, responseMsg, LOGGER);
//                putMsgBlockingQueue(damoaQueue, response, Constants.SEND_DESTINATION, packetNo);

            }
        });
    }

    MsgIF response(MsgIF msgIF) throws CloneNotSupportedException {
        MsgIF response = (MsgIF) msgIF.clone();
        response.response();
        return response;
    }

    private void insertTransDataAcct(DamoaDao damoaDao, String typeCode, String dealSeqNo, String destination, String msg, Logger LOG) {
        damoaDao.insertTransDataAcct(typeCode, dealSeqNo, destination, msg);
    }

    private String getMsgType(MsgIF msgIF) {
        return msgIF.getMsgTypeCode() + msgIF.getDealTypeCode();
    }

    private String encode(MessageFactory messageFactory, MsgIF msgIF) throws Exception {
        return messageFactory.encode(msgIF);
    }


    /**
     * inputMsg에 포함된 전문종류를 확인하여 , 개시, 종료, 수취조회, 입금통지, 취소전문에 해당하는 클래스를 불러온다.
     * @param inputMsg
     * @return
     */
    private Command findCommand(MsgIF inputMsg) {
        Command command = null;
        Code code = createByMsgIF(inputMsg);
        if (MessageCode.RANGE_ACCOUNT_RESPONSE.equals(code)) {
            command = commandFactory.findCommand(RangeAccountMsgCommand.class);
        } else if (MessageCode.DEPOSIT_NOTICE_REQUEST.equals(code)
                || MessageCode.TRANSFER_MONEY_NOTICE_REQUEST.equals(code)
                ) {
            command = commandFactory.findCommand(NoticeMsgCommand.class);        // 입금통지
        } else if (MessageCode.TRANSFER_MONEY_NOTICE_CANCEL_REQUEST.equals(code)
                || MessageCode.DEPOSIT_NOTICE_CANCEL_REQUEST.equals(code)
                ) {
            command = commandFactory.findCommand(CancelNoticeMsgCommand.class); // 입금취소
        }  else if (MessageCode.RECEIPT_INQUIRY_REQUEST.equals(code)) {
            command = commandFactory.findCommand(QueryMsgCommand.class);
        } else if (MessageCode.ADMIN_REQUEST.equals(code) || MessageCode.ADMIN_RESPONSE.equals(code)) {
            command = commandFactory.findCommand(AdminMsgCommand.class);
        } else if (MessageCode.POLLING_TYPE_RESPONSE.equals(code) || MessageCode.POLLING_TYPE_REQUEST.equals(code)) {
            command = commandFactory.findCommand(PollingMsgCommand.class);
        } else if (MessageCode.AGGREGATE_RESPONSE.equals(code) || MessageCode.AGGREGATE_REQUEST.equals(code)) {
            command = commandFactory.findCommand(AggregateMsgCommand.class);   // 집계
        } else {
            LOGGER.warn("[처리할 Commnad를 찾을 수 없음.]" + inputMsg);
        }
        return command;
    }

    private Code createByMsgIF(MsgIF inputMsg) {
        return Code.createByMsgIF(inputMsg);
    }
}
