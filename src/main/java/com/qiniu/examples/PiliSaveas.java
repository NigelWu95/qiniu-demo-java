package com.qiniu.examples;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.*;

public class PiliSaveas {

    public static void main(String[] args) throws QiniuException {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String hub = "test";
        String stream = "test";
        String fname = "test12345";
        String pipeline = "audio-video";
        int start = 1531182960;
        int end = 1531183440;

        String url = "http://pili.qiniuapi.com/v2/hubs/" + hub + "/streams/" + UrlSafeBase64.encodeToString(stream) + "/saveas";
        String contentType = "application/json";

        StringMap stringMap = new StringMap();
        stringMap.put("fname", fname);
        stringMap.put("start", start);
        stringMap.put("end", end);
        stringMap.put("pipeline", pipeline);

        System.out.println(Json.encode(stringMap));

        String qiniuToken = "Qiniu " + auth.signRequestV2(url, "POST", StringUtils.utf8Bytes(Json.encode(stringMap)), contentType);
        StringMap headers = new StringMap().put("Authorization", qiniuToken);
        headers.put("Content-Type", contentType);

        Client client = new Client();
        Response response = null;

        try {
            response = client.post(url, StringUtils.utf8Bytes(Json.encode(stringMap)), headers, contentType);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}