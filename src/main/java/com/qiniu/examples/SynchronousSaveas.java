package com.qiniu.examples;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

public class SynchronousSaveas {

    public static void tokendownload() throws NoSuchAlgorithmException, InvalidKeyException{

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();

        String urlbase64 = UrlSafeBase64.encodeToString("temp:123456aaa.jpg");
        String DownloadUrl = "7xrnxn.com1.z0.glb.clouddn.com/QQ20160402-2.png?imageMogr2/crop/!300x300a10a10|saveas/" + urlbase64;
        Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(StringUtils.utf8Bytes(secretKey), "HmacSHA1"));
        String t2 = UrlSafeBase64.encodeToString(mac.doFinal(StringUtils.utf8Bytes(DownloadUrl)));

        System.out.println("http://" + DownloadUrl + "/sign/" + accessKey + ":" + t2);
    }

    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException{
        tokendownload();
    }
}