package kr.co.finger.damoa.shinhan.agent.domain.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class DomainObjectTemplate {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
