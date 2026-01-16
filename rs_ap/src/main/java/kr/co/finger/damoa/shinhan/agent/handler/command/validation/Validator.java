package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;

public interface Validator {
    /**
     * 공통
     * @param commonMessage
     * @param damoaContext
     * @return
     */
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext);

    /**
     *
     * @param commonMessage
     * @param damoaContext
     * @param type 개별전문
     * @return
     */
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext,String type);
}
