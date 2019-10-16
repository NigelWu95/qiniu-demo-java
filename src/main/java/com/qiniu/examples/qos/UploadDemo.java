package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class UploadDemo {

    static private String proxyUser = "";
    static private String proxyPass = "";

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String bucket = "temp";
        String key = "react.txt";
        String filePath = "/Users/wubingheng/Downloads/react.txt";

        ///////////////////////指定上传的Zone的信息//////////////////
        //第一种方式: 指定具体的要上传的zone
        //注：该具体指定的方式和以下自动识别的方式选择其一即可
        //要上传的空间(bucket)的存储区域为华东时
        // Zone z = Zone.zone0();
        //要上传的空间(bucket)的存储区域为华北时
        // Zone z = Zone.zone1();
        //要上传的空间(bucket)的存储区域为华南时
        // Zone z = Zone.zone2();

        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        //自定义 zone
        Zone zone = new Zone.Builder().upHttp("http://upload.qiniup.com").upHttps("https://upload.qiniup.com").
                iovipHttp("http://iovip.qbox.me").iovipHttps("https://iovip.qbox.me").
                rsHttp("http://rs.qiniu.com").rsHttps("https://rs.qbox.me")
                .rsfHttp("http://rsf.qiniu.com").rsfHttps("https://rsf.qbox.me")
                .apiHttp("http://api.qiniu.com").apiHttps("https://api.qiniu.com").build();

        Configuration cfg = new Configuration(zone);
        cfg.useHttpsDomains = true;
//        cfg.proxy = new ProxyConfiguration("", 443, proxyUser, proxyPass, Proxy.Type.HTTP);

        //创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            while (true) {
                //调用put方法上传
                Response res = uploadManager.put(filePath, key, auth.uploadToken(bucket, key, 3600
                        , new StringMap()
//                            .put("insertOnly", 1)
//                            .put("detectMime", 1)
//                            .put("mimeLimit", "image/png;image/jpeg;text/plain;application/zip;application/octet-stream")
                ));
                //打印返回的信息
                System.out.println(res.bodyString());
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
}
