package kr.co.finger.msgagent.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * BioMessageFormat.xml의 <messages> 정보
 */
@XStreamAlias("messages")
public class Messages {

    @XStreamAlias("charset")
    @XStreamAsAttribute
    private String charset;

    /**
     * 메세지들
     */
    @XStreamConverter(MessageFormatConverter.class)
    @XStreamAlias("message")
    @XStreamImplicit(itemFieldName = "message")
    private List<Message> messageList = new ArrayList<Message>();

    /**
     * 메세지들을 취득 합니다.
     *
     * @return 메세지들
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * 메세지들을 설정 합니다.
     *
     * @param messageList 메세지들
     */
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("charset", charset)
                .toString();
    }
}
