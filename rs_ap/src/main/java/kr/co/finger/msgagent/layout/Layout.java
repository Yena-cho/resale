package kr.co.finger.msgagent.layout;

import java.io.Serializable;

/**
 * 각 필드마다 정보를 가지는 클래스.
 */
public class Layout implements Serializable {
    private String name;
    private String type;
    private String value;
    private String desc;
    private String length;

    public Layout(String name, String type, String value, String desc, String length) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.desc = desc;
        this.length = length;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
