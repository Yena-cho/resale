package com.finger.shinhandamoa.common;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.finger.msgio.layout.LayoutFactory;
import kr.co.finger.msgio.msg.AutoWithdrawalHelper;

@Configuration
public class LayoutBean {
	
	@Value("${messageFilePath}")
	private String messageFilePath;
	
	private static final Logger logger = LoggerFactory.getLogger(LayoutBean.class);

	@Bean
	public LayoutFactory withdrawalLayoutFactory() throws Exception {
		
		LayoutFactory layoutFactory = new LayoutFactory();
		layoutFactory.initFactory(messageFilePath, Charset.forName("EUC-KR"));
		
		AutoWithdrawalHelper.setLayoutFactory(layoutFactory);
		return layoutFactory;
   }
}
