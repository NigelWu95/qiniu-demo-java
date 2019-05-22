package com.qiniu.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("1");
        test.add("2");
        System.out.println(test.subList(0, test.size()));

        String testKey = "123456";
        System.out.println(testKey.substring(3,testKey.length()));

        Scanner scanner = new Scanner(System.in);
        String line;
        line = scanner.nextLine();
        System.out.println(line);
        System.out.println(scanner.hasNext());
        line = scanner.nextLine();
//        System.out.println(line);
        System.out.println(scanner.hasNext());
        System.out.println(line == null);
        System.out.println("".equals(line));
        System.out.println(line);

        System.out.println(Arrays.toString("abcde".split("")));
    }
}
