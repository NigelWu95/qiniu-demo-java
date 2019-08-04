package com.qiniu.examples;

import sun.misc.SignalHandler;

import java.util.*;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        List<String> test = new ArrayList<>();
        test.add("1");
        test.add("2");
        System.out.println(test.subList(0, test.size()));

        String testKey = "123456";
        System.out.println(testKey.substring(3,testKey.length()));

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

        System.out.println(Arrays.toString("abcde".split("")));

        System.out.println(Arrays.toString(" ".getBytes()));
        System.out.println(new String(new byte[]{31}) + "a");

        Map<String, String> map1 = new HashMap<String, String>(){{
            put("1", "a");
            put("2", "b");
            put("3", "c");
        }};
        Map<String, String> map2 = new HashMap<String, String>(){{
            put("1", "d");
            put("5", "e");
            put("6", "f");
        }};
        map2.putAll(map1);
        System.out.println(map2);
        map2.put("1", "g");
        System.out.println(map2);

        SignalHandler handler = signal -> {
            System.out.println("test");
        };
        // 设置INT信号(Ctrl+C中断执行)交给指定的信号处理器处理，废掉系统自带的功能
        sun.misc.Signal.handle(new sun.misc.Signal("INT"), handler);
        Thread.sleep(100000);
    }
}
