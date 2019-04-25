package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.model.Stream;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KodoHttps {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        List<String> domains = new ArrayList<String>(){{
            add("bricoletest.xiaohongshu.com");
        }};

        for (String domain : domains) {
            JSONObject jsonData = new JSONObject();
            jsonData.put("domain", domain);
            jsonData.put("certid", "5c750350c7d76277fd00013b");
            String data = Json.encode(jsonData);
//            System.out.println(data);
            byte[] body = StringUtils.utf8Bytes(data);
            String url = "http://api.qiniu.com/cert/bind";
            StringMap headers = auth.authorization(url, body, "application/json");
            Client client = new Client();
            Response response = null;
            try {
                response = client.post(url, body, headers);
                System.out.println(response.statusCode);
                System.out.println(response.bodyString());
            } catch (QiniuException e) {
                e.printStackTrace();
            }

            response.close();
        }
//        JSONObject jsonData = new JSONObject();
//        jsonData.put("domain", "xxx.xxx.com");
//        jsonData.put("certid", "certification id of domain on qiniu");
//        String data = Json.encode(jsonData);
//        System.out.println(data);
//        byte[] body = StringUtils.utf8Bytes(data);
//        String url = "http://api.qiniu.com/cert/bind";
//        StringMap headers = auth.authorization(url, body, "application/json");
//        Client client = new Client();
//        Response response = null;
//        try {
//            response = client.post(url, body, headers);
//            System.out.println(response.bodyString());
//        } catch (QiniuException e) {
//            e.printStackTrace();
//        }
//        response.close();
    }
}