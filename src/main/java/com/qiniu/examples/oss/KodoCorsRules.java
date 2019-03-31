package com.qiniu.examples.oss;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.*;

public class KodoCorsRules {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        //设置好客户账号的ACCESS_KEY和SECRET_KEY
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();

        //密钥配置
        Auth auth = Auth.create(accessKey, secretKey);

        String bucket = "veer-slice";
        String contentType = "application/json";

        JsonObject jsonData = new JsonObject();
        JsonArray exposedHeaders = new JsonArray();
        exposedHeaders.add("Date");
        exposedHeaders.add("Content-Length");
        exposedHeaders.add("Etag");
        exposedHeaders.add("X-Log");
        exposedHeaders.add("X-Reqid");
        jsonData.add("exposed_header", exposedHeaders);
        JsonArray allowedOrigin = new JsonArray();
        allowedOrigin.add("*");
        jsonData.add("allowed_origin", allowedOrigin);
        JsonArray allowedHeader = new JsonArray();
        allowedHeader.add("*");
        jsonData.add("allowed_header", allowedHeader);
        JsonArray allowedMethod = new JsonArray();
        allowedMethod.add("GET");
        allowedMethod.add("HEAD");
        allowedMethod.add("OPTIONS");
        jsonData.add("allowed_method", allowedMethod);
        jsonData.addProperty("max_age", 2592000);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonData);
        System.out.println(jsonArray);
        String data = Json.encode(jsonArray);
        byte[] body = StringUtils.utf8Bytes(data);
        String url = "http://uc.qbox.me/corsRules/set/" + bucket;
        StringMap headers = auth.authorization(url, body, "application/json");
        Client client = new Client();
        Response response = null;
        try {
            response = client.post(url, body, headers);
            System.out.println(response);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        response.close();
    }
}