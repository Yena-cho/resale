package com.finger.shinhandamoa.utils;

import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.util.Base64;

public class PasswdUtils {

    final private static ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);

    public static String shaEncoding(String pwd) {
        return passwordEncoder.encodePassword(pwd, null);
    }

//    @Test
//    public static void main(String[] args) {
//
//        String ip = "insideBankTest:insideBankTest";
//        Base64.Encoder encoder = Base64.getEncoder();
//        String encoded = new String(encoder.encode(ip.getBytes()));
//        // TODO Auto-generated method stub
//        System.out.println(encoded);
//        System.out.println(PasswdUtils.shaEncoding("Finger2020!"));
//        //66d44baaf7d29de17f36b02150b24629
//        String pw = "66d44baaf7d29de17f36b02150b24629";
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decodedAuth = decoder.decode(pw.getBytes());
//        String decoded = new String(decodedAuth);
//        System.out.println(decoded);
//
//    }

}
