package com.finger.shinhandamoa.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * {@link ImageUtils} 테스트
 * 
 * @author wisehouse@finger.co.kr
 */
public class ImageUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtilsTest.class);
    
    @Test
    public void resizeJpeg() throws IOException {
        File inputFile = new ClassPathResource("image-utils/resize-input.jpg").getFile();
        File outputFile = new File("target/resize-output.jpg");
        ImageUtils.resizeJpeg(inputFile, outputFile, 1024);
    }
}
