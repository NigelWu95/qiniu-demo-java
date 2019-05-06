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
            items = line1.split("([ \t])");
            try {
                lineMap.put(items[0], items[1]);
            } catch (Exception e) {
                System.out.println(line1);
                e.printStackTrace();
            }
        }
        bufferedReader1.close();
        fileReader1.close();

        FileWriter fileWriter = new FileWriter(new File(path2 + "2.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        FileReader fileReader2 = new FileReader(new File(path2));
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        String line2;
        String item2;
        while ((line2 = bufferedReader2.readLine()) != null) {
            items = line2.split("([ \t])");
            try {
                item2 = lineMap.get(items[0]);
                if (item2 != null && !item2.equals(items[1])) {
                    bufferedWriter.write(line2);
                    bufferedWriter.newLine();
                }
            } catch (Exception e) {
                System.out.println(line2);
                e.printStackTrace();
            }
        }
        bufferedReader2.close();
        fileReader2.close();
        bufferedWriter.close();
        fileWriter.close();
    }
}
