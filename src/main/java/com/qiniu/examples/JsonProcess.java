package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;

public class JsonProcess {

    public static void main(String[] args) {

        try {
            // read file content from file
            StringBuffer sb= new StringBuffer("");

            FileReader reader = new FileReader("/Users/wubingheng/Downloads/part-00000.txt");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while((str = br.readLine()) != null) {
                sb.append(str+"\r\n");

                System.out.println(str);
            }

            br.close();
            reader.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

//        try {
            JSONArray itemList = JSONArray.fromObject("");
//            hubJson.get("items");
//            StringMap itemsJson = Json.decode(hubJson.get("items").toString());
//            System.out.println(itemList);

            for (int i = 0; i < itemList.size(); i++) {
                JSONObject itemJson = (JSONObject) itemList.get(i);
                System.out.println(itemJson.get("domains"));
            }

//        } catch (QiniuException e) {
//            e.printStackTrace();
//        }
    }
}