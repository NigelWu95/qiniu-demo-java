package com.qiniu.examples.util;

import com.qiniu.util.*;

public class CharBase64 {

    public static void main(String[] args) {

        System.out.println(new String(UrlSafeBase64.decode("aGhoaAphYWFhYQ==")));
        System.out.println(UrlSafeBase64.encodeToString("hhhh"));
        System.out.println(UrlSafeBase64.encodeToString("aaaaa"));
        System.out.println(UrlSafeBase64.encodeToString("hhhh\naaaaa"));
        System.out.println(UrlSafeBase64.encodeToString("黑体"));
    }
}
