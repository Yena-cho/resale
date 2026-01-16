package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MessageCode;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleState implements State {
    protected boolean isValid = true;
    protected String code;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public SimpleState(String code) {
        this.code = code;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    public Code getCode(MsgIF msgIF) {
        return Code.createByMsgIF(msgIF);
    }

    public Code getCode(String id) {
        return Code.createById(id);
    }
    public void execute(Context context, MsgIF msgIF, NettyProducer nettyProducer, MessageFactory messageFactory, DamoaContext damoaContext) throws Exception {
        LOG.info("******************** simplestate execute ********************");
        boolean _isValid = validate(context, msgIF,damoaContext);  // 개시상태 확인
        LOG.info("validate "+_isValid);
        if (_isValid) {
            isValid = true;
            doExecute(context, msgIF); // StartState 클래스의 doExecute 메소드를 호출함
        } else {
            try {
                LOG.error("INVALID "+msgIF);
                MsgIF response = (MsgIF)msgIF.clone();
                String msg = messageFactory.encode(response);
                nettyProducer.write(msg);
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        }
    }

    boolean equals(Code code, String id) {
        if (code.equals(Code.createById(id))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getStateCode() {
        return code;
    }

//    @Override
//    public void preExecute(Context context) throws Exception {
//
//    }

    /**
     * 전문 정합성체크함.
     * @param context
     * @param msgIF
     * @return
     */
    public abstract boolean validate(Context context, MsgIF msgIF,DamoaContext damoaContext);

    /**
     * 올바른 전문을 처리함.
     * @param context
     * @param msgIF
     */
    public abstract void doExecute(Context context, MsgIF msgIF) throws Exception;


    public boolean isAdminMsg(Code code) {
        if (MessageCode.ADMIN_REQUEST.equals(code) || MessageCode.ADMIN_RESPONSE.equals(code)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdminRequest(Code code) {
        if (MessageCode.ADMIN_REQUEST.equals(code)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdminResponse(Code code) {
        if (MessageCode.ADMIN_RESPONSE.equals(code)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SimpleState{");
        sb.append("isValid=").append(isValid);
        sb.append(", code=").append(code);
        sb.append('}');
        return sb.toString();
    }

    public boolean validate(String status,MsgIF msgIF,String corpCode) {
        LOG.info("******************** simplestate validate ********************");
        if ("START".equalsIgnoreCase(status)) {
            LOG.info(corpCode+" 개시상태임..");
            return true;
        } else {
            LOG.error(corpCode+ " 종료상태..  개시이후에 전문을 받을 수 있음." + msgIF.getType());
            msgIF.setResCode("V122");
            msgIF.setResMsg("중계센터 SYSTEM 미개시");
            return false;
        }
    }

}
