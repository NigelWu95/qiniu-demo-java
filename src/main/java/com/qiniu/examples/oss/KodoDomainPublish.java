package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.util.*;

public class KodoDomainPublish {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        String tbl = "7zmz4b";
        String url = "http://api.qiniu.com/publish/" + UrlSafeBase64.encodeToString("first.nigel.qiniuts.com")
                + "/from/" + tbl + "/domaintype/1";
        StringMap headers = auth.authorization(url, null, "application/json");
        Client client = new Client();
        com.qiniu.http.Response response = null;
        try {
            response = client.post(url, "", headers);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) response.close();
        }
    }
}