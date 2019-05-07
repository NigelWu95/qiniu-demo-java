package com.qiniu.examples;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DiffFiles2 {

    public static void main(String[] args) throws Exception {
        String path1 = args[0];
        String path2 = args[1];
        FileReader fileReader1 = new FileReader(new File(path1));
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
        Map<String, String> lineMap = new HashMap<>();
        String line1;
        String[] items;
        while ((line1 = bufferedReader1.readLine()) != null) {
//            if (line1.startsWith("/")) line1 = line1.substring(1);
            items = line1.split("([ \t])");
            try {
                lineMap.put(items[0], items[1]);
            } catch (Exception e) {
                System.out.println("1: " + line1 + "\terror: " + e.getCause());
//                e.printStackTrace();
            }
        }
        bufferedReader1.close();
        fileReader1.close();

        String name = path2.split("\\.")[0];
        FileWriter fileWriter1 = new FileWriter(new File(name + "-1.txt"));
        BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
        FileWriter fileWriter2 = new FileWriter(new File(name + "-2.txt"));
        BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
        FileWriter fileWriter3 = new FileWriter(new File(name + "-3.txt"));
        BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter3);
        FileReader fileReader2 = new FileReader(new File(path2));
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        String line2;
        String item2;
        while ((line2 = bufferedReader2.readLine()) != null) {
            items = line2.split("([ \t])");
            try {
//                System.out.println("line: " + items[0] + "---" + items[1]);
                item2 = lineMap.get(items[0].substring(items[0].indexOf("/")));
                if (item2 != null) {
                    bufferedWriter1.write(line2);
                    bufferedWriter1.newLine();
                    if (item2.equals(items[1])) {
                        bufferedWriter2.write(line2);
                        bufferedWriter2.newLine();
                    } else {
                        bufferedWriter3.write(line2);
                        bufferedWriter3.newLine();
                    }
                }
            } catch (Exception e) {
                System.out.println("2: " + line2 + "\terror: " + e.getCause());
            }
        }
        bufferedReader2.close();
        fileReader2.close();
        bufferedWriter1.close();
        fileWriter1.close();
        bufferedWriter2.close();
        fileWriter2.close();
        bufferedWriter3.close();
        fileWriter3.close();
    }
}
