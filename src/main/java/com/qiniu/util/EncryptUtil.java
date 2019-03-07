package com.qiniu.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class EncryptUtil {

    public static void main(String[] args) {
        List<String> domains = new ArrayList<String>(){{
        }};
//        String domain = "xxx.xxx.com";
        for (String domain : domains) {
            System.out.println(domain + "\tcname:" + DigestUtils.shaHex(domain) + ".xiangha.com");
        }
    }
}