package com.qiniu.util;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtil {

    public static void main(String[] args) {
        System.out.println(DigestUtils.shaHex("first.nigel.qiniuts.com"));
    }
}