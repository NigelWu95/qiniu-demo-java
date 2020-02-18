package com.qiniu.examples.qos;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;

public class RestoreArchive {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String url = "http://rs.qbox.me/restoreAr";
        JsonObject bodyJson = new JsonObject();
        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("bucket", "temp");
        entry.addProperty("key", "CR1904660812F720.mp4");
        entry.addProperty("freeze_after_days", 1);
//        jsonObject.addProperty("cond", "1");
        entries.add(entry);
        bodyJson.add("entries", entries);
        byte[] body = bodyJson.toString().getBytes();

        Client client = new Client();
        Response response = null;
        try {
            response = client.post(url, body, auth.authorizationV2(url, "POST", body, Client.JsonMime), Client.JsonMime);
            System.out.println(response.statusCode);
            System.out.println(response);
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}