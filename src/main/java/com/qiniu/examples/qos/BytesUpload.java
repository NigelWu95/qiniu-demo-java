/**
 * Project Name: com.qiniu.sdkdemo
 * File Name: BytesUpload.java
 * Package Name: com.qiniu.sdkdemo
 * Date Time: 21/12/2017  9:37 AM
 * Copyright (c) 2017, xxx_xxx  All Rights Reserved.
 */
package com.qiniu.examples.qos;

import com.google.gson.Gson;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.*;

/**
 * ClassName: BytesUpload
 * Description: TODO
 * Date Time: 21/12/2017  9:37 AM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.8
 * @see
 */
public class BytesUpload {

    Config config = Config.getInstance();
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String accessKey = config.getAccesskey();
    String secretKey = config.getSecretKey();
    //要上传的空间
    String bucket = "huabei-test";
    //上传到七牛后保存的文件名
    String key = "yuanh.jpg";
    //上传文件的路径
    String filePath = config.getFilepath() + key;
    File file = new File(filePath);

    //密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //设置转码的队列
    String pipeline = "testqueue";

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);

    String fops = "imageView2/0/q/75|watermark/2/text/5Lit5aSn57q657uH5Z-O/" +
            "font/5a6L5L2T/fontsize/1360/fill/I0ZGRkZGRg==/dissolve/41/" +
            "gravity/Center/dx/10/dy/10|imageslim|saveas/Zmlyc3Q6MTB3YXRlcm1hcmsuanBn";

    StringMap putPolicy = new StringMap()
            .putNotEmpty("persistentOps", fops)
            .putNotEmpty("persistentPipeline", pipeline);


    public static void main(String args[]) throws IOException {
        new BytesUpload().upload();
    }

    public void upload() throws IOException {

        InputStream in= null;
        byte[] bytes= null;

        in = new FileInputStream(file);    //真正要用到的是FileInputStream类的read()方法
        bytes = new byte[in.available()];    //in.available()是得到文件的字节数
        in.read(bytes);    //把文件的字节一个一个地填到bytes数组中
        in.close();    //记得要关闭in

//        byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);

        String upToken = auth.uploadToken(bucket, "", 3600, null);

        System.out.println(upToken);

        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            Response response = uploadManager.put(fileInputStream, key, upToken, null, null);

            Response response = uploadManager.put(byteInputStream, "", upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            System.out.println(response);
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
