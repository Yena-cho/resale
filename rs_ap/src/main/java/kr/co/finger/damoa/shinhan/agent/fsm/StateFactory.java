package kr.co.finger.damoa.shinhan.agent.fsm;

import kr.co.finger.damoa.model.msg.Code;

import java.util.HashMap;
import java.util.Map;

public class StateFactory {
    private static StateFactory ourInstance = new StateFactory();

    public static StateFactory getInstance() {
        return ourInstance;
    }

    private StateFactory() {
    }

    private static Map<String, SimpleState> TEMP = new HashMap<>();

    static {
        //START 상태
        TEMP.put("START", new StartState());
        // 다모아 장애상태...
        TEMP.put("ERROR", new DamoaErrorState());


        ///////// 아래 두경우는 사용하지 않음.
        TEMP.put("FINISH", new FinishState());
        TEMP.put("FINISH_NOTICE", new FinishNoticeState());

    }

    public SimpleState findState(Code state) {
        return findState(state.getId());
    }
    public SimpleState findState(String state) {
        return TEMP.get(state);
    }
}
