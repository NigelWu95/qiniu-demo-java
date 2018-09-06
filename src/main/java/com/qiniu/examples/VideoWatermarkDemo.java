package com.qiniu.examples;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

public class VideoWatermarkDemo {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        //设置转码的队列
        String pipeline = "xxx";

        String bucket = "temp";
        String domainOfBucket = "http://xxx.com/";
        String sourceVideoKey = "test1.mp4";
        String targetFileName = "test2.mp4";
        String wmText = "ID:12345678";
        String wmImageUrl = "https://xxx.com/watermark.png";
        String persistId = new VideoWatermarkDemo().keepSizeWaterMark(auth, bucket, domainOfBucket, pipeline,
                sourceVideoKey, targetFileName, wmText, wmImageUrl, 0.6);

        System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistId);
    }

    public String keepSizeWaterMark(Auth auth, String bucket, String domainOfBucket, String pipeline, String sourceVideoKey,
                                    String targetFileName, String wmText, String wmImageUrl, double imageRate) {

        Configuration cfg = new Configuration(Zone.autoZone());
        OperationManager operationManager = new OperationManager(auth, cfg);

        // 做两层水印处理，里面一层是在一个背景透明的图片上打上水印文字和 log 图片，采用同步处理，得到一个合成的水印图片 URL，这样可以动态调整文字和 log URL 参数，
        // 再对这个 URL 进行 base64 编码得到参数放在视频水印的图片路径参数中可以设置适当偏移。
        String pfOps = "avthumb/mp4/wmImage/" +
                UrlSafeBase64.encodeToString("http://nigel.qiniuts.com/transparent200x200.png?watermark/3/image/" +
                        UrlSafeBase64.encodeToString(wmImageUrl) +
                        "/gravity/North/text/" +
                        UrlSafeBase64.encodeToString(wmText) +
                        "/fontsize/450/fill/I0ZGRkZGRg==/gravity/Center") +
                "/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30/wmConstant/1/wmScale/" + imageRate + "/wmScaleType/0" +
                "|saveas/" + UrlSafeBase64.encodeToString(bucket + ":" + targetFileName);

        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        String persistId = "";

        try {
            persistId = operationManager.pfop(bucket, sourceVideoKey, pfOps, params);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }

        return persistId;
    }


    public String getAvInfo(String domain, String sourthVideoKey) {

        Client client = new Client();
        String responseJson = null;

        try {
            responseJson = client.get(domain + sourthVideoKey + "?avinfo").bodyString();
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        return responseJson;
    }
}