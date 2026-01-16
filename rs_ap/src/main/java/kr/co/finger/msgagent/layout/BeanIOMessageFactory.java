package kr.co.finger.msgagent.layout;

import kr.co.finger.damoa.model.msg.MsgIF;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.beanio.internal.DefaultStreamFactory;
import org.beanio.internal.compiler.StreamCompiler;
import org.beanio.internal.parser.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class BeanIOMessageFactory {
    @Value("${message.beanio.format.fileName:NO}")
    private String messageFormatFileName;
    private DefaultStreamFactory factory;
    private TranTypeCodeResolver tranTypeCodeResolver;
    private Map<String, Unmarshaller> TEMP_UNMARSHALL = new HashMap<>();
    private Map<String, Marshaller> TEMP_MARSHALL = new HashMap<>();

    @PostConstruct
    public boolean initFactory() throws Exception {
        if ("NO".equals(messageFormatFileName) == false) {
            if (messageFormatFileName != null) {
                return initFactory(messageFormatFileName);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean initFactory(String messageFormatFileName) throws IOException {
        factory = (DefaultStreamFactory)StreamFactory.newInstance();
        ClassLoader classLoader = StreamFactory.class.getClassLoader();
        StreamCompiler streamCompiler = new StreamCompiler(classLoader);
        factory.setCompiler(streamCompiler);
        factory.load(classLoader.getResourceAsStream(messageFormatFileName));

        Collection<Stream> streams =streamCompiler.loadMapping(classLoader.getResourceAsStream(messageFormatFileName), null);

        for (Stream stream : streams) {
            String name = stream.getName();
            // 언마샬러는 한군데에서 사용하므로 쓰레드세이프하지 않아도 문제가 없다.
            // 마샬러는 동기화하도록 했다.
            TEMP_UNMARSHALL.put(name, factory.createUnmarshaller(name));
            TEMP_MARSHALL.put(name, factory.createMarshaller(name));
        }

        return false;
    }


    private String getTranTypeCode(String msg) {
        return this.tranTypeCodeResolver.getTranTypeCode(msg);
    }

    public void setTranTypeCodeResolver(TranTypeCodeResolver tranTypeCodeResolver) {
        this.tranTypeCodeResolver = tranTypeCodeResolver;
    }

    public void initializeMessage(MessageInitializer messageInitializer) {
        this.setTranTypeCodeResolver(messageInitializer.getTranTypeCodeResolver());
    }

    public MsgIF decode(String msg) {
        String name = getTranTypeCode(msg);
        if (TEMP_UNMARSHALL.containsKey(name)) {
            Unmarshaller unmarshaller = TEMP_UNMARSHALL.get(name);
            return (MsgIF)unmarshaller.unmarshal(msg);
        } else {
            return null;
        }
    }

    /**
     *
     * @param msgIF
     * @return
     */
    public synchronized String encode(MsgIF msgIF) {
        String type = msgIF.getType();
        if (TEMP_MARSHALL.containsKey(type)) {
            Marshaller marshaller = TEMP_MARSHALL.get(type);
            return marshaller.marshal(msgIF).toString();
        } else {
            return "";
        }
    }
}
