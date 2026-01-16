package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.model.msg.AdminMessage;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.util.DamoaBizUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

/**
 * 종료예고 상태.
 * 종료전문만 받는다.
 */
public class FinishNoticeState extends SimpleState {

    public FinishNoticeState() {
        super("FINISH_NOTICE");
    }

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public boolean validate(Context context, MsgIF msgIF, DamoaContext damoaContext) {
        Code code = getCode(msgIF);
        if (isAdminRequest(code)) {

            return true;
        } else {
            // 입금전문을 받을 수 있음.
            return true;
        }
    }

    @Override
    public void doExecute(Context context, MsgIF msgIF) throws Exception {
        context.executeCommand(msgIF);
        changeState(context, msgIF);
        DamoaBizUtil.touchFile("FINISH");
    }

    private void changeState(Context context, MsgIF msgIF) {
        Code code = getCode(msgIF);
        if (isAdminRequest(code)) {
            AdminMessage adminMessage = (AdminMessage) msgIF;
            String adminInfo = adminMessage.getAdminInfo();
            Code adminCode = Code.createById(adminInfo);
            if (MessageCode.ADMIN_FINISH.equals(adminCode)) {
                context.changeState("FINISH");
            } else if (MessageCode.ADMIN_START.equals(adminCode)
                    || MessageCode.ADMIN_RESTART.equals(adminCode)
                    || MessageCode.ADMIN_DISABILITY.equals(adminCode)
                    || MessageCode.ADMIN_DISABILITY_RECOVERY.equals(adminCode)
                    ) {
                context.changeState("START");
            } else {
                //SKIP...
            }
        }

    }


}
