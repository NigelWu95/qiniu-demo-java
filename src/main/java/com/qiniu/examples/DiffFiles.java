package com.qiniu.examples;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DiffFiles {

    public static void main(String[] args) throws Exception {
        String path1 = args[0];
        String path2 = args[1];
        FileReader fileReader1 = new FileReader(new File(path1));
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
        List<String> lines = new ArrayList<>();
        String line1;
        while ((line1 = bufferedReader1.readLine()) != null) {
            if (line1.startsWith("\"\\\"/")) line1 = line1.substring(4).split("\\?")[0];
            else line1 = line1.split("\\?")[0];
            lines.add(line1);
        }
        bufferedReader1.close();
        fileReader1.close();

        FileWriter fileWriter = new FileWriter(new File(path2 + "1.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        FileReader fileReader2 = new FileReader(new File(path2));
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        String line2;
        while ((line2 = bufferedReader2.readLine()) != null) {
            if (!lines.contains(line2.split("\t")[0])) {
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
