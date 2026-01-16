package com.finger.damo.rcpcusttestserver.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

/**
 * 공통코드
 *
 */
public class Code implements Serializable {
    private String id;
    private String groupCode;
    private String name;
    private String description;

    public Code() {

    }

    public Code(String id) {
        this(id, "", "", "");
    }


    public Code(String id, String groupCode, String name, String description) {
        this.id = id;
        this.groupCode = groupCode;
        this.name = name;
        this.description = description;
    }

    public Code(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Code createById(String id) {
        Code code = new Code();
        code.setId(id);
        return code;
    }

    public static Code createByMsgIF(MsgIF msgIF) {
        return createById(msgIF.getType());
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Code)) {
            return false;
        }

        Code code = (Code) o;
        return code.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("id", id)
                .append("name", name)
                .toString();
    }
}
