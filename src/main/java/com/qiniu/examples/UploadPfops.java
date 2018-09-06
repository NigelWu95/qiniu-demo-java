package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import java.io.IOException;

import com.qiniu.storage.Configuration;
import com.qiniu.common.Zone;

public class UploadPfops {

    Config config = Config.getInstance();
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = config.getAccesskey();
    String SECRET_KEY = config.getSecretKey();
    //要上传的空间
    String bucketname = "temp";
    //上传到七牛后保存的文件名
    String key = "JMY.mp4";
//    String key = "JMY.mp4";
    //上传文件的路径
    String filePath = "/Users/wubingheng/Downloads/JMY.mp4";

    //设置转码操作参数
    String fops =
//            "avthumb/mp4/ab/160k/ar/44100/acodec/libfaac/r/30/vb/2200k/vcodec/libx264/s/1280x720/autoscale/1/stripmeta/0";
            "avwatermarks/mp4/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/meipian-log.gif") +
            "/wmGravity/Center/wmPos/0/wmDuration/1/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/logo_00036.png") +
            "/wmGravity/Center/wmPos/$(end)/wmDuration/0";
    //设置转码的队列
    String pipeline = "audio-video";

    //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
    String urlbase64 = UrlSafeBase64.encodeToString(bucketname + ":job_" + key);
    String pfops = fops + "|saveas/" + urlbase64;

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);

    //上传策略中设置persistentOps字段和persistentPipeline字段
    public String getUpToken() {
        return auth.uploadToken(bucketname, key, 3600, new StringMap()
//                , true);
                .putNotEmpty("persistentOps", pfops)
                .putNotEmpty("persistentPipeline", pipeline), true);
    }

    public void upload() throws IOException {
        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
            System.out.println("http://api.qiniu.com/status/get/prefop?id=" + res.bodyString().charAt(2));
        } catch (QiniuException e) {
            e.printStackTrace();
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

    public static void main(String args[]) throws IOException {
        new UploadPfops().upload();
    }

}
