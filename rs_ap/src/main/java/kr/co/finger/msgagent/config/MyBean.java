package kr.co.finger.msgagent.config;

import java.util.Date;

public class MyBean {
    private Date first;
    private Date last;


    public MyBean(Date first) {
        this.first = first;
    }

    public Date getFirst() {
        return first;
    }

    public void setFirst(Date first) {
        this.first = first;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }
}
