package com.qiniu.util;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtil {

    public static void main(String[] args) {
        String domain = "xxx.xxx.com";
        System.out.println(DigestUtils.shaHex(domain));
    }
}