package com.qiniu.examples.live;

import com.qiniu.common.Config;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;

public class Authorization {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String hub = "hub";
        String stream = "test";
        String url = "http://pili.qiniuapi.com/v2/hubs/" + hub + "/streams/" + UrlSafeBase64.encodeToString(stream) + "/live";
        String authorization = "Qiniu " + auth.signRequestV2(url, "GET", null, "application/x-www-form-urlencoded");
        System.out.println(url);
        System.out.println(authorization);
    }
}