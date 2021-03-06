package com.qiniu.examples.qos;

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
        String bucket = "";
        String domain = "";
        String url = "http://uc.qbox.me/publish?domain=" + domain
//        UrlSafeBase64.encodeToString(domain)
                + "&tbl=" + bucket + "&domaintype=1" + "&skipownership=true";
        System.out.println(url);
        StringMap headers = auth.authorization(url, null, "application/json");
        Client client = new Client();
        com.qiniu.http.Response response = null;
        try {
            response = client.post(url, null, headers, "application/json");
            System.out.println(response);
            System.out.println(response.statusCode);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) response.close();
        }
    }
}