package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.StringMap;

import java.io.IOException;

public class UploadOverwrite {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        UploadManager uploadManager = new UploadManager(c);
        String bucket = config.getFirstBucketName();
        String key = "my.png";
        String filePath = config.getFilepath() + "63X58PICDJ.jpg";

        try {
            //调用put方法上传，这里指定的key和上传策略中的key要一致
            Response res = uploadManager.put(filePath, key, auth.uploadToken(bucket, key, 3600, new StringMap().put("insertOnly", 1)));
            //打印返回的信息
            System.out.println(res.bodyString());
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