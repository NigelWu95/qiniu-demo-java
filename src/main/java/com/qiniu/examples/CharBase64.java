package com.qiniu.examples;

import com.qiniu.util.*;

public class CharBase64 {

    public static void main(String[] args) {

        byte[] bytes1 = UrlSafeBase64.decode("aGhoaAphYWFhYQ==");
        byte[] bytes2 = UrlSafeBase64.decode(     "aGhoaAphYWFhYQ==");
        System.out.println(new String(bytes1));
        System.out.println(new String(bytes2));
        System.out.println(UrlSafeBase64.encodeToString("hhhh\naaaaa"));
        System.out.println(UrlSafeBase64.encodeToString("黑体"));
    }
}
