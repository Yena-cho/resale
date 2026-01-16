package kr.co.finger.msgagent.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyContext {

    private Map<String, MyBean> DATE_MAP = new HashMap<>();


    public void putFirst(String dealSeqNo) {
        DATE_MAP.put(dealSeqNo, new MyBean(new Date()));
    }

    public void putLast(String dealSeqNo) {
        MyBean myBean = DATE_MAP.get(dealSeqNo);
        if (myBean != null) {
            myBean.setLast(new Date());
        }
    }


    public Map<String, MyBean> getDATE_MAP() {
        return DATE_MAP;
    }
}
