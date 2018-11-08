package com.qiniu.examples.media;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import java.util.ArrayList;
import java.util.List;

public class AvconcatDemo {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        OperationManager operationManager = new OperationManager(auth, c);
        String bucket = "temp";
        String key = "1536631954765.mp4";
        //设置转码操作参数
        String fopBaseCommand = "avconcat/2/format/mp4/";
        List<String> encodedVideoUrls = new ArrayList<String>(){
            {
                add(UrlSafeBase64.encodeToString("video url"));
//                add(UrlSafeBase64.encodeToString("video url"));
//                add(UrlSafeBase64.encodeToString("video url"));
            }
        };

        System.out.println(fopBaseCommand + String.join("/", encodedVideoUrls));
        //设置转码的队列
        String pipeline = "audio-video";
        //可以对转码后的文件进行使用 saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString("temp:avconcat-result.mp4");
        String pfops = fopBaseCommand + String.join("/", encodedVideoUrls) + "|saveas/" + urlbase64;
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);

        try {
            String persistId = operationManager.pfop(bucket, key, pfops, params);
            System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistId);
        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.reqId);
            System.out.println(r.statusCode);
            System.out.println(r.toString());
            r.close();
        }
    }

}
