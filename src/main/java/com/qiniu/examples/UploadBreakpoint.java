package com.qiniu.examples;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

import java.io.IOException;

public class UploadBreakpoint {

    public static void main(String args[]) throws IOException {

        Config config = Config.getInstance();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        //要上传的空间
        String bucketname = "temp";
        //上传到七牛后保存的文件名
        String key = "20180508.mp4";
        //上传文件的路径
        String filePath = config.getFilepath() + key;

        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        //设置断点记录文件保存在指定文件夹或的File对象
        String recordPath = config.getFilepath() + "temp";
        //实例化recorder对象
        Recorder recorder = new FileRecorder(recordPath);
        //实例化上传对象，并且传入一个recorder对象
        UploadManager uploadManager = new UploadManager(c, recorder);

        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, key, auth.uploadToken(bucketname));
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
//            Response r = e.response;
//            // 请求失败时打印的异常的信息
//            System.out.println(e);
//            try {
//                //响应的文本信息
//                System.out.println(r.bodyString());
//            } catch (QiniuException e1) {
//                //ignore
//            }
            System.out.println("error");
        }
    }

}