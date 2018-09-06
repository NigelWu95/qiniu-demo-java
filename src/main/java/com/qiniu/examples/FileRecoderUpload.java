/**
 * Project Name: com.qiniu.sdkdemo
 * File Name: FileRecoderUpload.java
 * Package Name: com.qiniu.sdkdemo
 * Date Time: 21/12/2017  7:13 PM
 * Copyright (c) 2017, xxx_xxx  All Rights Reserved.
 */
package com.qiniu.examples;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * ClassName: FileRecoderUpload
 * Description: TODO
 * Date Time: 21/12/2017  7:13 PM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.8
 * @see
 */
public class FileRecoderUpload {

    public static void main(String args[]) throws IOException {

        int[] notNullCount = new int[3];

        if (null == args) {
            System.out.println("args is null.");
        } else if (args.length == 1) {
            if (!StringUtils.isNullOrEmpty(args[0])) {
                notNullCount[0] = 1;
            }
        }  else if (args.length == 2) {
            if (!StringUtils.isNullOrEmpty(args[1])) {
                notNullCount[1] = 1;
            }
        }  else if (args.length == 3) {
            if (!StringUtils.isNullOrEmpty(args[2])) {
                notNullCount[2] = 1;
            }
        }

        String bucketName = notNullCount[0] == 1 ? args[0] : "two-bucket";
        String keyName = notNullCount[1] == 1 ? args[1] : null;
        String domain = notNullCount[2] == 1 ? args[1] + ".com" : "upload.qiniu.com";

        Zone zone = (new Zone.Builder()).upHttp("http://" + domain).upHttps("https://up.qbox.me").upBackupHttp("http://up.qiniu.com").upBackupHttps("https://upload.qbox.me").iovipHttp("http://iovip.qbox.me").iovipHttps("https://iovip.qbox.me").rsHttp("http://rs.qiniu.com").rsHttps("https://rs.qbox.me").rsfHttp("http://rsf.qiniu.com").rsfHttps("https://rsf.qbox.me").apiHttp("http://api.qiniu.com").apiHttps("https://api.qiniu.com").build();

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        //...其他参数参考类注释
        Config config = Config.getInstance();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        //要上传的空间
        String bucket = "two-bucket";

        String key = keyName;
        //上传文件的路径
        String filePath = config.getFilepath() + key;
        File file = new File(filePath);

        String localFilePath = "java-test-web.mp4";
        //默认不指定key的情况下，以文件内容的hash值作为文件名

        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        String upToken = auth.uploadToken(bucket);

        String localTempDir = "./temp";
        File directory = new File(".");//设定为当前文件夹
        System.out.println(directory.getAbsolutePath());

        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
//            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                System.out.println(response);
                System.out.println(response.bodyString());
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
//            } catch (QiniuException ex) {
//                Response r = ex.response;
//                System.err.println(r.toString());
//
//                try {
//                    System.err.println(r.bodyString());
//                } catch (QiniuException ex2) {
//                    //ignore
//                }
//            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
