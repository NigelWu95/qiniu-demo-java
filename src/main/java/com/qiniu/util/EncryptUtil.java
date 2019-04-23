package com.qiniu.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncryptUtil {

    public static void main(String[] args) {
        List<String> domains = new ArrayList<String>(){{
            add("xxx.com");
        }};
//        String domain = "xxx.xxx.com";
        for (String domain : domains) {
            System.out.println(domain + ":");
            System.out.println("cname 记录名:" + DigestUtils.shaHex(domain));
            System.out.println("cname 记录值:" + DigestUtils.shaHex(domain) + "." +
                    String.join(".", Arrays.asList(domain.split("\\."))
                            .subList(domain.split("\\.").length - 2, domain.split("\\.").length)));
        }
    }
}