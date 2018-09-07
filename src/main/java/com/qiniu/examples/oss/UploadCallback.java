package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.IOException;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class UploadCallback {

    Config config = Config.getInstance();
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = config.getAccesskey();
    String SECRET_KEY = config.getSecretKey();
    //要上传的空间
    String bucketname = "temp";
    //上传到七牛后保存的文件名
    String key = "react.txt";
    //上传文件的路径
    String filePath = config.getFilepath() + key;

    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);
    UploadManager uploadManager = new UploadManager(c);

    //设置callbackUrl以及callbackBody,七牛将文件名和文件大小回调给业务服务器
    public String getUpToken() {
        return auth.uploadToken(bucketname, key, 3600,
                new StringMap()
                .put("callbackUrl", "http://bf091726.ngrok.io/QiniuDemo/callback")
//                .put("callbackBody", "filename=$(fname)&filesize=$(fsize)")
                .put("callbackBodyType", "application/json")
                .put("callbackBody", "{filename:$(fname),filesize:$(fsize)}")
//                null
        );
    }

    public void upload() throws IOException {
        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        new UploadCallback().upload();
    }

}