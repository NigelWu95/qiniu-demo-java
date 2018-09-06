package com.qiniu.examples;

import com.google.gson.Gson;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
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
 * Description: 断点续传 demo
 */
public class FileRecoderUpload {

    public static void main(String args[]) {

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

        String bucket = notNullCount[0] == 1 ? args[0] : "bucket";
        // 默认不指定 key 或为 null 的情况下，以文件内容的 hash 值作为文件名
        String key = notNullCount[1] == 1 ? args[1] : null;
        String domain = notNullCount[2] == 1 ? args[1] + ".com" : "upload.qiniu.com";

        Zone zone = (new Zone.Builder())
                .upHttp("http://" + domain)
                .upHttps("https://up.qbox.me")
                .upBackupHttp("http://up.qiniu.com")
                .upBackupHttps("https://upload.qbox.me")
                .iovipHttp("http://iovip.qbox.me")
                .iovipHttps("https://iovip.qbox.me")
                .rsHttp("http://rs.qiniu.com")
                .rsHttps("https://rs.qbox.me")
                .rsfHttp("http://rsf.qiniu.com")
                .rsfHttps("https://rsf.qbox.me")
                .apiHttp("http://api.qiniu.com")
                .apiHttps("https://api.qiniu.com")
                .build();

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String localFilePath = config.getFilepath() + "test.mp4";
        String localTempDir = "./temp";
        File directory = new File(".");//设定为当前文件夹
        System.out.println(directory.getAbsolutePath());

        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            Response response = null;

            try {
                response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(response);
                System.out.println(response.bodyString());
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
