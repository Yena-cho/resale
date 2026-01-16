package com.finger.shinhandamoa.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyStatusLookupAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyStatusLookupAPI.class);

    /**
     * 미확인/오류
     */
    public static final String CODE_ERROR = "00";
    
    /**
     * 일반
     */
    public static final String CODE_NORMAL = "11";

    /**
     * 간이
     */
    public static final String CODE_SIMPLE = "12";

    /**
     * 면세
     */
    public static final String CODE_DUTYFREE = "21";

    /**
     * 비영리
     */
    public static final String CODE_NGO = "22";

    /**
     * 휴업
     */
    public static final String CODE_REST = "31";

    /**
     * 폐업
     */
    public static final String CODE_CLOSE = "32";

    /**
     * 기타
     */
    public static final String CODE_ETC = "99";

    private String host;
    private int port;

    private String file = "/IFX1002";

    public CompanyStatusLookupAPI(final String host, final int port) {
        LOGGER.debug("CompanyStatusLookupAPI.host: ", host);  	
        LOGGER.debug("CompanyStatusLookupAPI.port: ", port);  	
        this.host = host;
        this.port = port;
    }

    public IFX1002 lookup(String registerationNo) {
        String htsId = String.valueOf(System.currentTimeMillis());
        String htsPwd = String.valueOf(System.currentTimeMillis());
        String coRegNb = registerationNo;

        Map<String, String> param = new HashMap<String, String>();
        param.put("hts_id", htsId);
        param.put("hts_pwd", htsPwd);
        param.put("co_reg_nb", coRegNb);

        List<String> queryList = new ArrayList<String>();
        for (Map.Entry<String, String> each : param.entrySet()) {
            queryList.add(each.getKey() + "=" + each.getValue());
        }

        String queryString = StringUtils.join(queryList, "&");

        StringWriter stringWriter = new StringWriter();
        try {
            String urlString = "http://" + host + ":" + port + file;
            LOGGER.debug("urlString: {}", urlString);
            URL url = new URL(urlString + "?" + queryString);
            LOGGER.debug("url: {}", url);
            InputStream in = url.openStream();
            IOUtils.copy(new InputStreamReader(in, "UTF-8"), stringWriter);
            LOGGER.debug("response: {}", stringWriter.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IFX1002 result;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IFX1002.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            result = (IFX1002) unmarshaller.unmarshal(new StringReader(stringWriter.toString()));
            LOGGER.debug("IFX1002 : {}", result);
        } catch (JAXBException e) {
            LOGGER.error("Failed to parse IFX1002", e);
            result = new IFX1002();
        }

        return result;

    }


    @XmlRootElement(name = "IFX1002")
    public static class IFX1002 {
        @XmlElement(name = "RCODE")
        private Code code;

        @XmlElement(name = "RMSG")
        private Code message;

        @XmlElement(name = "RTAX_GBN")
        private Code taxGbn;

        @XmlElement(name = "RUPDATE_DATE")
        private Code updateDate;

        @XmlElement(name = "RCLOSE_DATE")
        private Code closeDate;

        public String getCode() {
            return code.getValue();
        }

        public String getMessage() {
            return message.getValue();
        }

        public String getTaxGbn() {
            return taxGbn.getValue();
        }

        public String getUpdateDate() {
            return updateDate.getValue();
        }

        public String getCloseDate() {
            return closeDate.getValue();
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
    
    public static class Code {
        @XmlAttribute(name = "value")
        private String value;

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

}
