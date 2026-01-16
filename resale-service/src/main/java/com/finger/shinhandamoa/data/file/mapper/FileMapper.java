package com.finger.shinhandamoa.data.file.mapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 파일 매퍼 인터페이스
 * 
 * @author wisehouse@finger.co.kr
 */
public interface FileMapper {
    void store(String bucket, String fileId, InputStream is) throws IOException;
    InputStream load(String bucket, String fileId) throws IOException;
    void delete(String bucket, String fileId) throws IOException;
}
