package kr.co.finger.damoa.scheduler.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Service
public class FileService {
    @Autowired
    private Charset charSet;

    public void write(String content, String fuleFilePath) throws IOException {
        System.out.println(fuleFilePath);
        FileUtils.write(new File(fuleFilePath), content, charSet, false);
    }


    public void write(byte[] msg, String fullFilePath) throws IOException {
        System.out.println(fullFilePath);
        FileUtils.writeByteArrayToFile(new File(fullFilePath), msg);
    }

    public byte[] readBinary(String filePath) throws IOException {
        return FileUtils.readFileToByteArray(new File(filePath));
    }

    public String read(String filePath) throws IOException {
        return FileUtils.readFileToString(new File(filePath),charSet);
    }
}
