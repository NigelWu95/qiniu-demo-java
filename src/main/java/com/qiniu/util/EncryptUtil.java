package com.qiniu.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class EncryptUtil {

    public static void main(String[] args) {
        List<String> domains = new ArrayList<String>(){{
            add("xiangha-dsf1-rsa.cdn.xiangha.com");
            add("xiangha-f1-rsa.cdn.xiangha.com");
            add("xiangha-forcoo-rsa.cdn.xiangha.com");
            add("xiangha-img-rsa.cdn.xiangha.com");
            add("xiangha-m3u8video-rsa.cdn.xiangha.com");
            add("xiangha-static-rsa.cdn.xiangha.com");
            add("xiangha-temp-rsa.cdn.xiangha.com");
            add("xiangha-video-rsa.cdn.xiangha.com");
        }};
//        String domain = "xxx.xxx.com";
        for (String domain : domains) {
            System.out.println(domain + "\tcname:" + DigestUtils.shaHex(domain) + ".xiangha.com");
        }
    }
}