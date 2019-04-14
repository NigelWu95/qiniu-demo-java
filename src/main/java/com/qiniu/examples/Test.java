package com.qiniu.examples;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("1");
        test.add("2");
        System.out.println(test.subList(0, test.size()));

        String testKey = "123456";
        System.out.println(testKey.substring(3,testKey.length()));
    }
}
