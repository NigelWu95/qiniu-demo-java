package com.qiniu.examples.media;

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
//        String urlBase64 = UrlSafeBase64.encodeToString("temp:test.jpg");
//        String DownloadUrl = "xxx.com/test.png?imageMogr2/crop/!300x300a10a10|saveas/" + urlBase64;
//        Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
//        mac.init(new SecretKeySpec(StringUtils.utf8Bytes(secretKey), "HmacSHA1"));
//        String sign = accessKey + ":" + UrlSafeBase64.encodeToString(mac.doFinal(StringUtils.utf8Bytes(DownloadUrl)));
//        System.out.println("http://" + DownloadUrl + "/sign/" + sign);

        String urlBase64 = UrlSafeBase64.encodeToString("rrmj:test.jpg");
        String DownloadUrl = "img.rr.tv/annual2018/bg/bg_share_2.png?imageView2/0/interlace/1/q/75|imageslim|saveas/" + urlBase64;
        Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(com.qiniu.util.StringUtils.utf8Bytes(secretKey), "HmacSHA1"));
        String sign = accessKey + ":" + UrlSafeBase64.encodeToString(mac.doFinal(com.qiniu.util.StringUtils.utf8Bytes(DownloadUrl)));
        System.out.println("http://" + DownloadUrl + "/sign/" + sign);
    }
}