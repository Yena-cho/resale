package com.finger.shinhandamoa.data.file.mapper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

/**
 * 기본 파일 매퍼
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class SimpleFileMapper implements FileMapper{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFileMapper.class);
    
    @Value("${simple-file-mapper.root}")
    private String fileRoot;

    @Override
    public void store(String bucket, String fileId, InputStream is) throws IOException {
        if(StringUtils.length(fileId) != 19) {
            throw new RuntimeException("파일 아이디 부적합");
        }
        
        /*if(!StringUtils.isNumeric(fileId)) {
            throw new RuntimeException("파일 아이디 부적합");
        }*/
        
        File file = new File(generateFilePath(bucket, fileId));
        if(file.exists()) {
            throw new FileAlreadyExistsException(file.getAbsolutePath());
        }
        
        file.getParentFile().mkdirs();

        IOUtils.copy(is, new FileOutputStream(file));
    }

    @Override
    public InputStream load(String bucket, String fileId) throws IOException {
        File file = new File(generateFilePath(bucket, fileId));
        if(!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        
        return FileUtils.openInputStream(file);
    }

    @Override
    public void delete(String bucket, String fileId) throws IOException {
        File file = new File(generateFilePath(bucket, fileId));
        if(!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        FileUtils.deleteQuietly(file);
    }
    
    private String generateFilePath(String bucket, String fileId) {
        List<String> filePathFragmentList = new ArrayList<>();
        filePathFragmentList.add(fileRoot);
        filePathFragmentList.add(bucket);
        filePathFragmentList.add(StringUtils.right(fileId, 4));
        filePathFragmentList.add(StringUtils.left(StringUtils.right(fileId, 8), 4));
        filePathFragmentList.add(fileId);
        
        return StringUtils.join(filePathFragmentList, File.separatorChar);
    }
}
