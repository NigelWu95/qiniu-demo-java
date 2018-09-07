package com.qiniu.examples.live;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.common.Config;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LiveHubs {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        String hubName = "hub";
        String url = "http://pili.qiniuapi.com/v1/hubs";
        String token = "Qiniu " + auth.signRequestV2(url, "GET", null, null);
        StringMap headers = new StringMap().put("Authorization", token);
        System.out.println(headers.get("Authorization"));
        Client client = new Client();
        Response response = null;

        try {
            response = client.get(url, headers);
            System.out.println(response.bodyString());
            StringMap hubJson = Json.decode(response.bodyString());
            JSONArray itemList = JSONArray.fromObject(hubJson.get("items"));

            for (int i = 0; i < itemList.size(); i++) {
                JSONObject itemJson = (JSONObject) itemList.get(i);
                System.out.println(itemJson.get("domains"));
            }

        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}