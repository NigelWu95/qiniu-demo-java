package com.qiniu.examples;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.qiniu.common.Config;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

public class SynchronousSaveAs {

    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException{

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        String urlBase64 = UrlSafeBase64.encodeToString("temp:test.jpg");
        String DownloadUrl = "xxx.com/test.png?imageMogr2/crop/!300x300a10a10|saveas/" + urlBase64;
        Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(StringUtils.utf8Bytes(secretKey), "HmacSHA1"));
        String sign = accessKey + ":" + UrlSafeBase64.encodeToString(mac.doFinal(StringUtils.utf8Bytes(DownloadUrl)));
        System.out.println("http://" + DownloadUrl + "/sign/" + sign);
    }
}