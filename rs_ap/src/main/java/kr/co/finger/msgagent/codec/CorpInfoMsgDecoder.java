package kr.co.finger.msgagent.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

import static kr.co.finger.msgagent.codec.DecodingState.*;

public class CorpInfoMsgDecoder extends ReplayingDecoder<DecodingState> {

    private Charset charset;
    private Bean bean;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public CorpInfoMsgDecoder(Charset charset) {
        super(READ_HEADER);
        this.charset = charset;
        bean = new Bean();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case READ_HEADER:
                LOG.info("READ_HEADER "+in);
                state(readHeader(in));
                checkpoint(state());
                break;
            case SKIP_HEADER:
                LOG.info("SKIP_HEADER "+in);
                skipHeader(in);
                checkpoint(PAYLOAD);
                break;
            case PAYLOAD:
                try {
                    LOG.info("PAYLOAD "+in);
                    readPayload(in,out);
                } catch (Exception e){
                    e.printStackTrace();
                }finally {
                    this.reset();
                }
                break;
            default:
                throw new Exception("Unknown decoding state: ");
        }
    }


    private void readLength(ByteBuf in) {
        String header = read4Bytes(in);
        bean.setLength(header);

    }

    private void readPayload(ByteBuf in, List<Object> out) {

        if ("POLLING".equals(bean.getType())) {
            String content = getString(in, Integer.valueOf(bean.getLength()));
            out.add(content);
            bean.setOk(true);
        } else {
            String content = getString(in, Integer.valueOf(bean.getLengthFromHeader()));
            out.add(content);
            bean.setOk(true);
        }
    }

    private void skipHeader(ByteBuf in) {
        byte[] bytes = new byte[26];
        in.readBytes(bytes);
        bean.parseSkipHeader(bytes,charset);
    }

    private DecodingState readHeader(ByteBuf buffer) {
        String header = read4Bytes(buffer);
        if ("DHRT".equals(header)) {
            bean.setType("DHRT");
            return DecodingState.SKIP_HEADER;
        } else {
            bean.setType("POLLING");
            this.bean.setLength(header);
            return DecodingState.PAYLOAD;
        }
    }

    private String getString(ByteBuf byteBuf, int length) {
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return new String(bytes, this.charset);
    }

    private void reset() {
        if (bean.isOk()) {
            checkpoint(READ_HEADER);
            this.bean = new Bean();
        } else {

        }

    }

    private String read4Bytes(ByteBuf byteBuf) {
        byte[] bytes = new byte[4];
        byteBuf.readBytes(bytes);
        return new String(bytes, charset);
    }

}
