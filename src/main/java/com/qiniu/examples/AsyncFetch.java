package com.qiniu.examples;

import com.google.gson.Gson;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.util.HashMap;
import java.util.Map;

public class AsyncFetch {

    public static void main(String[] args) throws Exception {
        //参考api文档 https://developer.qiniu.com/kodo/api/4097/asynch-fetch
        //设置好账号的ACCESS_KEY和SECRET_KEY
        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);

        Gson gson = new Gson();
        String url = "http://xxx.com/test.jpg";
        String apiUrl = "http://api.qiniu.com/sisyphus/fetch";
        Map<String, String> bodyMap = new HashMap();
        bodyMap.put("url", url);
        bodyMap.put("bucket", "Bucket");
        bodyMap.put("key", "自定义文件名");
        String jsonBody = gson.toJson(bodyMap);
        byte[] bodyBytes = jsonBody.getBytes();
        String accessToken = "Qiniu " + auth.signRequestV2(apiUrl, "POST", bodyBytes, "application/json");
        StringMap headers = new StringMap();
        headers.put("Authorization", accessToken);
        Client client = new Client();
        Response resp = null;

        try {
            resp = client.post(url, bodyBytes, headers, Client.JsonMime);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }
}