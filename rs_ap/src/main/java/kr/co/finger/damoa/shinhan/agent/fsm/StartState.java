package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.model.msg.*;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.handler.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 시작State
 * 종료전문, 장애회복전문도 받을 수 있다.
 * <p>
 * 장애전문을 받아도 전이하지 않는다.
 * 종료예고전문을 받으면 종료예고상태로 전이.
 * 나머지는 그대로..
 */
public class StartState extends SimpleState {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public StartState() {
        super("START");
    }

    @Override
    /**
     모든 전문을 다 받을 수 있음.
     */
    public boolean validate(Context context, MsgIF msgIF, DamoaContext damoaContext) {
        LOG.info("******************** startstate validate override ********************"+msgIF);
        Code code = getCode(msgIF);
        if (isAdminRequest(code)) {
//            AdminMessage adminMessage = (AdminMessage) msgIF;
//            String institutionCode = adminMessage.getInstitutionCode();
//            if ("20007481".equalsIgnoreCase(institutionCode)) {
//            }
//            String status = damoaContext.findCorpCdStatus("20007481");
//            String adminInfo = adminMessage.getAdminInfo();
//            if ("001".equalsIgnoreCase(adminInfo) || "002".equalsIgnoreCase(adminInfo)) {
//                return true;
//            }else if("004".equalsIgnoreCase(adminInfo)){
//                return true;
//            }else {
//                return true;
//            }


            return true;
        } else if (isAdminResponse(code)) {
            return true;
        } else {
            return validate(damoaContext, msgIF);
        }
    }


    private boolean validate(DamoaContext damoaContext, MsgIF msgIF) {
        Code code = getCode(msgIF);
        LOG.info("******************** startstate validate ********************"+msgIF);
        if (MessageCode.DEPOSIT_NOTICE_REQUEST.equals(code)
                || MessageCode.TRANSFER_MONEY_NOTICE_REQUEST.equals(code)
                || MessageCode.TRANSFER_MONEY_NOTICE_CANCEL_REQUEST.equals(code)
                || MessageCode.DEPOSIT_NOTICE_CANCEL_REQUEST.equals(code)
                ) {
            LOG.info(" 기관개시(종료) 상태 체크 입금(이체),취소요청.. "+msgIF);
            // 입금(이체) 취소전문...
            NoticeMessage noticeMessage = (NoticeMessage) msgIF;
            String corpCode = noticeMessage.getDepositCorpCode().trim();
            String status = damoaContext.findCorpCdStatus(corpCode);
            return validate(status, msgIF,corpCode);
        } else if (MessageCode.RECEIPT_INQUIRY_REQUEST.equals(code)) {
            LOG.info(" 기관개시(종료) 상태 체크 수취조회요청.. "+msgIF);
            QueryMessage queryMessage = (QueryMessage) msgIF;
            String corpCode = queryMessage.getDepositCorpCode().trim();
            String status = damoaContext.findCorpCdStatus(corpCode);
            return validate(status, msgIF,corpCode);
        } else if (MessageCode.AGGREGATE_REQUEST.equals(code)) {
            LOG.info(" 기관개시(종료) 상태 체크 집계요청.. "+msgIF);
            AggregateMessage aggregateMessage = (AggregateMessage) msgIF;
            String corpCode = aggregateMessage.getCorpCode().trim();
            String status = damoaContext.findCorpCdStatus(corpCode);
            return validate(status, msgIF,corpCode);
        } else {
            LOG.info(" 기관개시(종료) 상태 체크하지 않음.. "+msgIF);
            return true;
        }
    }

    @Override
    /**
     *
     */
    public void doExecute(Context context, MsgIF msgIF) throws Exception {
        LOG.info("******************** startstate doexecute ********************"+msgIF);
        context.executeCommand(msgIF);
        changeState(context, msgIF);
    }


    private void changeState(Context context, MsgIF msgIF) {
        // START 상태는 임의로 변경되지 않음
        // 관리자가 다모아 장애상태,또는 개시상태로 변경 가능..
    }
}
