package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.model.msg.AdminMessage;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.msgagent.client.NettyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 장애State
 * 장애회복전문만 받을 수 있다.
 */
public class DamoaErrorState extends SimpleState {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public DamoaErrorState() {
        super("ERROR");
    }


    @Override
    /**
     * 관리회복전문만 받는다.
     */
    public boolean validate(Context context, MsgIF msgIF, DamoaContext damoaContext) {

        Code code = getCode(msgIF);
        if (isAdminResponse(code)) {
            AdminMessage adminMessage = (AdminMessage) msgIF;
            String adminInfo = adminMessage.getAdminInfo();
            Code adminCode = Code.createById(adminInfo);
            if (MessageCode.ADMIN_DISABILITY_RECOVERY.equals(adminCode) || MessageCode.ADMIN_DISABILITY.equals(adminCode)) {
                LOG.error("통신망응답전문 ... 장애회복, 장애전문을 받음..");
                return true;
            } else {
                LOG.error("다모아 장애상태에서는 장애(회복)전문만 받을 수 있음.");
                msgIF.setResMsg("중계센터 SYSTEM 장애");
                msgIF.setResCode("V121");
                return false;
            }
        } else {
            LOG.error("다모아 장애상태에서는 장애(회복)전문만 받을 수 있음.");
            msgIF.setResMsg("중계센터 SYSTEM 장애");
            msgIF.setResCode("V121");
            return false;
        }
    }

    @Override
    /**
     * 장애회복전문을 받으면 START상태로 전이
     */
    public void doExecute(Context context, MsgIF msgIF) throws Exception {
        Code code = getCode(msgIF);
        if (isAdminResponse(code)) {
            AdminMessage adminMessage = (AdminMessage) msgIF;
            String adminInfo = adminMessage.getAdminInfo();
            Code adminCode = Code.createById(adminInfo);
            if (MessageCode.ADMIN_DISABILITY_RECOVERY.equals(adminCode)) {
                context.changeState("START");
            }
        }
    }

}
