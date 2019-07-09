package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import com.qiniu.storage.Configuration;
import com.qiniu.common.Zone;

public class UploadPfops {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        UploadManager uploadManager = new UploadManager(c);

        String bucket = "trans-test";
        String key = "4be4348730654b236da82c42a84146ff.apk";
        String filePath = config.getFilepath() + "4be4348730654b236da82c42a84146ff.apk";

        //设置转码操作参数
        String fops = "aparser";
//            "avthumb/mp4/ab/160k/ar/44100/acodec/libfaac/r/30/vb/2200k/vcodec/libx264/s/1280x720/autoscale/1/stripmeta/0";
//                "avwatermarks/mp4/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/meipian-log.gif") +
//                        "/wmGravity/Center/wmPos/0/wmDuration/1/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/logo_00036.png") +
//                        "/wmGravity/Center/wmPos/$(end)/wmDuration/0";
        String pipeline = "app";

        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString(bucket + ":job_" + key + ".txt");
        String pfops = fops + "|saveas/" + urlbase64;

        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, key, auth.uploadToken(bucket, key, 3600, new StringMap()
//                , true);
                    .putNotEmpty("persistentOps", pfops)
                    .putNotEmpty("persistentPipeline", pipeline)
                    .putNotEmpty("persistentNotifyUrl", "http://15f8030e.ngrok.io/qiniu-bs-demo/callback"), true));
            //打印返回的信息
            System.out.println(res.bodyString());
            System.out.println("http://api.qiniu.com/status/get/prefop?id=" + res.bodyString().substring(res.bodyString().length() - 29, res.bodyString().length() - 2));
        } catch (QiniuException e) {
            e.printStackTrace();
            Response r = e.response;
            System.out.println(r.reqId);
            System.out.println(r.statusCode);
            System.out.println(r.toString());
        }
    }

}
