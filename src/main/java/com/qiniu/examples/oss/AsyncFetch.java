package com.qiniu.examples.oss;

import com.google.gson.Gson;
import com.qiniu.common.Config;
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
        String url = "http://temp.nigel.qiniuts.com/test1.jpg";
        url = "https://nagame.oss-cn-hangzhou.aliyuncs.com/upload/1484614302452.jpg";
        String bucketZone = "z0";
        String apiUrl = ("z0".equals(bucketZone) ? "http://api.qiniu.com" : "http://api-" + bucketZone + ".qiniu.com") + "/sisyphus/fetch";
        Map<String, String> bodyMap = new HashMap();
        bodyMap.put("url", url);
        bodyMap.put("bucket", "temp");
//        bodyMap.put("host", bucketZone);
        bodyMap.put("key", "upload/1484614302452.jpg");
        String jsonBody = gson.toJson(bodyMap);
        byte[] bodyBytes = jsonBody.getBytes();
        String accessToken = "Qiniu " + auth.signRequestV2(apiUrl, "POST", bodyBytes, "application/json");
        StringMap headers = new StringMap();
        headers.put("Authorization", accessToken);
        Client client = new Client();
        Response resp = null;

        try {
            resp = client.post(apiUrl, bodyBytes, headers, Client.JsonMime);
            System.out.println(resp.statusCode);
            System.out.println(resp.reqId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }
}