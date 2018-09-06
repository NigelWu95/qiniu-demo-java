package com.qiniu.examples;

import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonProcess {

    public static void main(String[] args) {

        JSONObject hubJson = new JSONObject();
        StringMap itemsJson = Json.decode(hubJson.get("items").toString());
        System.out.println(itemsJson);

        JSONArray itemList = JSONArray.fromObject("");

        for (int i = 0; i < itemList.size(); i++) {
            JSONObject itemJson = (JSONObject) itemList.get(i);
            System.out.println(itemJson.get("domains"));
        }
    }
}