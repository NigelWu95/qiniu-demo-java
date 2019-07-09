package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.IOException;

public class ScopeUpload extends Basic {

    public Response Scope(String fileName) throws IOException {
        return uploadWithPolicy(
                getUploadManager(null, false),
                config.getFilepath() + fileName,
                new StringMap()
                        .put("scope", "first:test")
                        .put("isPrefixalScope", 1)
                        .putNotEmpty("isPrefixalScope", "1"),
                config.getFirstBucketName(),
                "a.jpg",
                "3600"
        );
    }

    public static void main(String[] args) {
//        try {
//            Response res = new ScopeUpload().Scope("8.jpg");
//            System.out.println(res.bodyString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            new ScopeUpload().upload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Config config = Config.getInstance();
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String accessKey = config.getAccesskey();
    String secretKey = config.getSecretKey();
    //要上传的空间
    String bucket = config.getFirstBucketName();
    //上传到七牛后保存的文件名
    String key = "12345.mp4";
    //上传文件的路径
    String filePath = "/Users/wubingheng/Downloads/1234.mp4";

    //密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);

    StringMap putPolicy = new StringMap().put("isPrefixalScope", 1);

    public void upload() throws IOException {
//        byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);

        String upToken = auth.uploadToken(bucket, key, 3600, putPolicy);

        try {
            Response response = uploadManager.put(filePath, key, upToken);

//            Response response = uploadManager.put(byteInputStream, "inputStreamTest", upToken, null, null);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            System.out.println(response.bodyString());
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
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}