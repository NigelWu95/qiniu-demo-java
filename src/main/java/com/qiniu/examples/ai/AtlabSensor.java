package com.qiniu.examples.ai;

import com.google.gson.*;
import com.qiniu.common.Config;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;

import java.util.HashMap;
import java.util.Map;

public class AtlabSensor {

    public static void main(String[] args) throws Exception {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);

        JsonObject bodyJson = new JsonObject();

        JsonObject dataValueJson = new JsonObject();
        dataValueJson.addProperty("uri", "http://zb.xksquare.com/20180929204815_1_5baf748f1e18a.mp4");
        bodyJson.add("data", dataValueJson);

        JsonArray opsValueJsonArray = new JsonArray();
        JsonObject opValue = new JsonObject();
        opValue.addProperty("op", "pulp");
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("label", "1");
        jsonObject.addProperty("select", 2);
        jsonObject.addProperty("score", 0);
        jsonArray.add(jsonObject);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.add("labels", jsonArray);
        opValue.add("params", jsonObject1);
        opsValueJsonArray.add(opValue);
        bodyJson.add("ops", opsValueJsonArray);

        JsonObject paramsValueJson = new JsonObject();
        paramsValueJson.addProperty("async", false);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("mode", 1);
        jsonObject2.addProperty("interval", 5);
        paramsValueJson.add("vframe", jsonObject1);
        bodyJson.add("params", paramsValueJson);

        System.out.println(bodyJson.toString());
        byte[] bodyBytes = bodyJson.toString().getBytes();

        // 获取签名
        String url = "http://argus.atlab.ai/v1/video/" + "12345678";
        String qiniuToken = "Qiniu " + auth.signRequestV2(url, "POST", bodyBytes, "application/json");
        Client client = new Client();
        StringMap headers = new StringMap();
        headers.put("Authorization", qiniuToken);
        Response resp = null;

        try {
            resp = client.post(url, bodyBytes, headers, Client.JsonMime);
            System.out.println(resp.toString());
            System.out.println(resp.reqId);
            System.out.println(resp.bodyString());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }
}