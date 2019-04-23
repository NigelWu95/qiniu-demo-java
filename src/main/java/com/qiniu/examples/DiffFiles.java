package com.qiniu.examples;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DiffFiles {

    public static void main(String[] args) throws Exception {
        String path1 = args[0];
        String path2 = args[1];
        FileReader fileReader1 = new FileReader(new File(path1));
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
        Map<String, Integer> lineMap = new HashMap<>();
        String line1;
        while ((line1 = bufferedReader1.readLine()) != null) {
            lineMap.put(line1.split("\t")[0], 1);
        }
        bufferedReader1.close();
        fileReader1.close();

        FileWriter fileWriter = new FileWriter(new File(path2 + "1.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        FileReader fileReader2 = new FileReader(new File(path2));
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        String line2;
        while ((line2 = bufferedReader2.readLine()) != null) {
            if (!lineMap.containsKey(line2.split("\t")[0])) {
                bufferedWriter.write(line2);
                bufferedWriter.newLine();
            }
        }
        bufferedReader2.close();
        fileReader2.close();
        bufferedWriter.close();
        fileWriter.close();
    }
}
