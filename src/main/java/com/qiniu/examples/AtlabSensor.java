package com.qiniu.examples;

import com.google.gson.*;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class AtlabSensor {

    public static void main(String[] args) throws Exception {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);

        JsonObject dataValueJson = new JsonObject();
        JsonObject paramsValueJson = new JsonObject();
        JsonObject opValue = new JsonObject();
        JsonArray opsValueJsonArray = new JsonArray();
        dataValueJson.addProperty("uri", "http://xxx.com/test.mp4");
//        paramsValueJson.addProperty("", "");
        opValue.addProperty("op", "pulp");
        opsValueJsonArray.add(opValue);
        JsonObject bodyJson = new JsonObject();
        bodyJson.add("data", dataValueJson);
        bodyJson.add("params", paramsValueJson);
        bodyJson.add("ops", opsValueJsonArray);
        System.out.println(bodyJson.toString());
        byte[] bodyBytes = bodyJson.toString().getBytes();

        // 获取签名
        String url = "http://argus.atlab.ai/v1/video/" + "testvid";
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