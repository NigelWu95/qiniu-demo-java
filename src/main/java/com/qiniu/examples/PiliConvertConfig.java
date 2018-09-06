package com.qiniu.examples;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.*;

import java.util.ArrayList;
import java.util.List;

public class PiliConvertConfig {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        ListWatertemplate(auth);

    }

    public static void ListWatertemplate(Auth auth) {
        String hub = "test";
        String encodedStreamTitle = UrlSafeBase64.encodeToString("test");

        String url = "http://pili.qiniuapi.com/v2/hubs/" + hub + "/streams/" + encodedStreamTitle + "/converts";
        String contentType = "application/json";
        StringMap stringMap = new StringMap();
        List<String> list = new ArrayList<String>();
        list.add("720p");
        list.add("480p");
        list.add("wmt_test");
        stringMap.put("converts", list);

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