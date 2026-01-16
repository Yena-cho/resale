package kr.co.finger.damoa.shinhan.agent.handler;

import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.fsm.Context;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 수신된 전문을 비동기로 처리할 때 처음 관문이 되는 클래스.
 * 수신가능한 전문만 걸러내는 작업을 수행함.
 */
@Component
public class DamoaBizHandler implements BizHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DamoaBizHandler.class);

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private ExceptionHandler exceptionHandler;

    @Autowired
    private DamoaIdleHandler damoaIdleHandler;

    @Autowired
    private Context context;

    /**
     * 1. 응답전문을 생성하고 보낸다.
     * 2. 비즈니스로직을 처리하고 응답전문을 보낸다.
     *
     * @param message
     */
    @Override
    public synchronized void execute(String message) {
        LOGGER.info("******************** DamoaBizHandler execute ********************");
        LOGGER.info("<<< ]" + message + "[");

        if (StringUtils.isBlank(message)) {
            LOGGER.warn("요청 내용 없음");
            return;
        }

        MsgIF inputMsg = null;
        try {
            damoaIdleHandler.init();
            LOGGER.info("******************** messageFactory ********************" + message);
            inputMsg = messageFactory.decode(message);
            LOGGER.info("******************** messageFactory ********************" + inputMsg.toString());
            if(inputMsg == null) {
                LOGGER.warn("디코딩할 수 없는 전문");
                return;
            }

            if (message.startsWith("HDR")) {
                LOGGER.debug("폴링 전문");
                context.executeCommand(inputMsg);
                return;
            }

            if (!checkMsg(inputMsg)) {
                LOGGER.error("처리할 수 없는 전문");
                return;
            }

            context.execute(inputMsg);
        } catch (Exception e) {
            exceptionHandler.handle(e, inputMsg, message);
        }
    }

    /**
     * 처리가능한 전문을 골라낸다.
     * 통지전문요청, 관리전문모두, 집계전문 모두
     *
     * @param inputMsg
     * @return
     */
    public boolean checkMsg(MsgIF inputMsg) {
        LOGGER.info("******************** damoabizhandler checkMsg ********************"+ inputMsg.getOriginMessage());
        Code code = Code.createByMsgIF(inputMsg);
        if (MessageCode.DEPOSIT_NOTICE_REQUEST.equals(code)
                || MessageCode.TRANSFER_MONEY_NOTICE_REQUEST.equals(code)
                || MessageCode.RECEIPT_INQUIRY_REQUEST.equals(code)
                || MessageCode.TRANSFER_MONEY_NOTICE_CANCEL_REQUEST.equals(code)
                || MessageCode.DEPOSIT_NOTICE_CANCEL_REQUEST.equals(code)) {
            // 통지전문요청을 받는다.
            return true;
        } else if (MessageCode.ADMIN_REQUEST.equals(code) || MessageCode.ADMIN_RESPONSE.equals(code)) {
            // 관리전문을 받는다.
            return true;
        } else if (MessageCode.AGGREGATE_RESPONSE.equals(code) || MessageCode.AGGREGATE_REQUEST.equals(code)) {
            // 집계전문을 받는다.
            return true;
        } else if (MessageCode.RANGE_ACCOUNT_RESPONSE.equals(code)) {
            return true;
        } else {
            LOGGER.error("처리할 수 업는 전문]" + code);
            return false;
        }
    }

    @PostConstruct
    public void log() {
    }
}
