package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MsgIF;

/**
 * Finite State machine 에서 Context 역할을 수행함.
 * State 변환과 실제 전문처리 역할을 대행한다.
 */
public interface Context {
    /**
     * 상태전이
     * @param state
     */
    public void changeState(Code state);

    /**
     * 상태전이
     * @param state
     */
    public void changeState(String state);

    /**
     * 전문을 처리함.
     * @param msgIF
     * @throws Exception
     */
    public void execute(MsgIF msgIF) throws Exception;

    boolean isValid();

    public String getState();

    /**
     * 전문의 종류에 따라 전문 처리함.
     * 요청전문인 경우 응답전문을 생성하고 전송함.
     * 응답전문인 경우 적당한 비즈니스로직 처리.
     * @param msgIF
     * @throws Exception
     */
    void executeCommand(MsgIF msgIF) throws Exception;
}
