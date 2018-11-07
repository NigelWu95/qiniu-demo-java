package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import net.sf.json.JSONObject;

public class KodoHttps {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
//        StringMap stringMap = new StringMap();
//        stringMap.put("domain", "image.diyidan.net");
//        stringMap.put("certid", "5b88b3da340597388f00195d");

        JSONObject jsonData = new JSONObject();
        jsonData.put("domain", "image.diyidan.net");
        jsonData.put("certid", "5b88b3da340597388f00195d");
        String data = Json.encode(jsonData);
        System.out.println(data);
        byte[] body = StringUtils.utf8Bytes(data);
        String url = "http://api.qiniu.com/cert/bind";
        StringMap headers = auth.authorization(url, body, "application/json");


        Client client = new Client();
        com.qiniu.http.Response response = null;
        try {
            response = client.post(url, body, headers);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        response.close();
    }
}