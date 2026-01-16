package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.model.msg.AdminMessage;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.util.DamoaBizUtil;
import kr.co.finger.msgagent.client.NettyProducer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

/**
 * 종료상태를 의미한다.
 * 처음 상태임.
 * 개시전문을 받아야 START state로 전송됨.
 * START State로 전이한다.
 */
public class FinishState extends SimpleState {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public FinishState() {
        super("FINISH");
    }

    /**
     * 개시전문만 받을 수 있다.
     * 바로 START 상태가 된다.
     *
     * @param context
     * @param msgIF
     * @return
     */
    @Override
    public boolean validate(Context context, MsgIF msgIF, DamoaContext damoaContext) {
        LOG.info("******************** finishstate validate override ********************");
        Code code = getCode(msgIF);
        if (isAdminRequest(code)) {
            return true;
        } else {
            String typeCode = msgIF.getMsgTypeCode() + msgIF.getDealTypeCode();
            LOG.error("개시이후에 전문을 받을 수 있음." + msgIF.getType());
            msgIF.setResCode("V143");
            msgIF.setResMsg("개설기관 업무 종료");
            return false;
        }
    }

    @Override
    public void doExecute(Context context, MsgIF msgIF) throws Exception {
        LOG.info("******************** finishstate doexecute ********************"+msgIF);
        context.executeCommand(msgIF);
        Code code = getCode(msgIF);
        if (isAdminRequest(code)) {
            AdminMessage adminMessage = (AdminMessage) msgIF;
            String adminInfo = adminMessage.getAdminInfo();
            Code adminCode = Code.createById(adminInfo);
            if (MessageCode.ADMIN_START.equals(adminCode)
                    || MessageCode.ADMIN_RESTART.equals(adminCode)
                    || MessageCode.ADMIN_DISABILITY.equals(adminCode)
                    || MessageCode.ADMIN_DISABILITY_RECOVERY.equals(adminCode)

                    ) {
                context.changeState("START");
                DamoaBizUtil.touchFile("START");
            } else {
                // 이때는 변경하지 않음.
            }
        }


    }


}
