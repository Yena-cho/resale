package kr.co.finger.damoa.shinhan.agent.handler;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;

public class DamoaException extends Exception{
    private String code;
    private String msg;
    private int index;
    public DamoaException(String code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public DamoaException(String code, int index) {
        this.code = code;
        this.index = index;
    }

    public DamoaException(String code) {
        this.code = code;
    }

    public DamoaException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DamoaException(ResultBean resultBean) {
        this.code = resultBean.getCode();
        this.msg = resultBean.getMsg();
    }

    public String getMsg() {
        return msg;
    }

    public int getIndex() {
        return index;
    }

    public String getCode() {
        return code;
    }


}
