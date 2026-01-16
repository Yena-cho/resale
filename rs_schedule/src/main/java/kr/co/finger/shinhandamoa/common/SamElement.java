package kr.co.finger.shinhandamoa.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * SAM 파일 요소
 * 
 * @author wisehouse@finger.co.kr
 */
public class SamElement {
    private byte[] data;
    
    public SamElement(int length) {
        data = new byte[length];
    }

    public SamElement(int length, char defaultFiller) {
        data = new byte[length];
        
        for(int i=0; i<length; i++) {
            data[i] = (byte) defaultFiller;
        }
    }

    protected void setBytes(int offset, int limit, byte[] bytes) {
        for(int i=0; i<limit; i++) {
            int actualIndex = offset + i;
            if(actualIndex >= data.length) {
                break;
            }
            if(i >= bytes.length) {
                break;
            }

            data[actualIndex] = bytes[i];
        }
    }

    protected String getString(int offset, int limit) {
        return new String(Arrays.copyOfRange(data, offset, offset + limit));
    }

    protected final void setString(int offset, int limit, String string) {
        byte[] bytes = StringUtils.defaultString(string).getBytes(Charset.forName("KSC5601"));
        for(int i=0; i<bytes.length; i++) {
            if(i >= limit) {
                break;
            }

            int targetIndex = i+offset;
            if(targetIndex >= data.length) {
                break;
            }

            data[targetIndex] = bytes[i];
        }
    }

    protected final int getInt(int offset, int limit) {
        final String intString = getString(offset, limit);
        return NumberUtils.toInt(intString,0);
    }

    protected final void setInt(int offset, int limit, int value) {
        String string = StringUtils.leftPad(String.valueOf(value), limit, '0');
        setString(offset, limit, string);
    }

    protected final long getLong(int offset, int limit) {
        final String intString = getString(offset, limit);
        return NumberUtils.toLong(intString,0);
    }

    protected final void setLong(int offset, int limit, long value) {
        String string = StringUtils.leftPad(String.valueOf(value), limit, '0');
        setString(offset, limit, string);
    }

    public final void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(data);
    }
}

