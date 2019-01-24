package com.qiniu.examples.oss;

import com.google.gson.Gson;
import com.qiniu.common.Config;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AsyncFetch {

    public static void main(String[] args) throws Exception {
        //参考api文档 https://developer.qiniu.com/kodo/api/4097/asynch-fetch
        //设置好账号的ACCESS_KEY和SECRET_KEY
        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String url = "http://xxx.com/test1.jpg";
        String host = ""; // 回源访问时需要的 host
        String bucket = "";
        String key = url.replaceAll("(https?://[^\\s/]+\\.[^\\s/\\\\.]{1,3}/)|(\\?.*)", "");
        String md5 = "";
        String callbackUrl = "";
        String callbackBody = "";
        String callbackBodyType = "";
        String bucketZone = "z0";
        String fileType = "";
        Gson gson = new Gson();
        String apiUrl = ("z0".equals(bucketZone) ? "http://api.qiniu.com" : "http://api-" + bucketZone + ".qiniu.com") + "/sisyphus/fetch";
        Map<String, String> bodyMap = new HashMap();
        bodyMap.put("url", url);
        bodyMap.put("bucket", bucket);
//        bodyMap.put("md5", md5);
//        bodyMap.put("callbackurl", callbackUrl);
//        bodyMap.put("callbackbody", callbackBody);
//        bodyMap.put("callbackbodytype", callbackBodyType);
//        bodyMap.put("file_type", fileType);
        bodyMap.put("key", key);
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