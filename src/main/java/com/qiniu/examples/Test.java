package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import sun.misc.SignalHandler;

import java.io.File;
import java.util.*;

public class Test {

    public static void main(String[] args) throws InterruptedException {
//        List<String> test = new ArrayList<>();
//        test.add("1");
//        test.add("2");
//        System.out.println(test.subList(0, test.size()));
//
//        String testKey = "123456";
//        System.out.println(testKey.substring(3,testKey.length()));

//        Scanner scanner = new Scanner(System.in);
//        String line;
//        line = scanner.nextLine();
//        System.out.println(line);
//        System.out.println(scanner.hasNext());
//        line = scanner.nextLine();
//        System.out.println(line);
//        System.out.println(scanner.hasNext());
//        System.out.println(line == null);
//        System.out.println("".equals(line));
//        System.out.println(line);

//        System.out.println(Arrays.toString("abcde".split("")));
//
//        System.out.println(Arrays.toString(" ".getBytes()));
//        System.out.println(new String(new byte[]{31}) + "a");
//
//        Map<String, String> map1 = new HashMap<String, String>(){{
//            put("1", "a");
//            put("2", "b");
//            put("3", "c");
//        }};
        Map<String, String> map2 = new HashMap<String, String>(){{
            put("1", "d");
            put("5", "e");
            put("6", "f");
        }};
        System.out.println("\r".compareTo("") > 0);
//        map2.putAll(map1);
//        System.out.println(map2);
//        map2.put("1", "g");
//        System.out.println(map2);
//
//        SignalHandler handler = signal -> {
//            System.out.println("test");
//        };
        // 设置INT信号(Ctrl+C中断执行)交给指定的信号处理器处理，废掉系统自带的功能
//        sun.misc.Signal.handle(new sun.misc.Signal("INT"), handler);
//        Thread.sleep(100000);

//        String s1 = "1";
//        List<String> list = new ArrayList<>();
//        list.add("1");
//        list.add("2");
//        System.out.println(list.stream().anyMatch(s1::contains));
//        list.clear();
//        System.out.println(list.stream().anyMatch(s1::contains));
//        StringBuilder stringBuilder = new StringBuilder("abc");
//        System.out.println(stringBuilder.deleteCharAt(2).toString());
//
//        System.out.println(("a" == null ? "\t" : "a" + "\t") + "abc");

//        BucketManager bucketManager = new BucketManager(
//                Auth.create("TzrY5--HJSrBMkG09Z", ""),
//                new Configuration());
//        try {
//            bucketManager.buckets();
//        } catch (QiniuException e) {
//            e.printStackTrace();
//        }
//        File file = new File("..", "");
//        System.out.println(file.exists());
//        File[] files = file.listFiles();
//        for (File file1 : files) {
//            System.out.println(file1.getName() + "---" + file1.getPath());
//        }

        int a = 100;
        while (a < 1000) {
            a += a >> 1;
            System.out.println(a);
        }
    }
}
