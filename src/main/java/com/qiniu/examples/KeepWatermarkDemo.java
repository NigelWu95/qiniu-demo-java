package com.qiniu.examples;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

public class KeepWatermarkDemo {

    public static void main(String[] args) {
        Config config = Config.getInstance();

        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //设置要转码的空间和key，并且这个key在你空间中存在
        String bucket = "temp";
        //设置转码的队列
        String pipeline = "audio-video";
        String domainOfBucket = "http://temp.nigel.qiniuts.com/";
        String sourthVideoKey = "test1.mp4";
        String targetFileName = "test137.mp4";
        String wmText = "ID:12345678";
        String wmImageUrl = "https://qn.kaiheikeji.com/watermark_22.png";

        String persistid = new KeepWatermarkDemo().keepSizeWaterMark(auth, bucket, domainOfBucket, pipeline,
                sourthVideoKey, targetFileName, wmText, wmImageUrl);

        System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistid);
    }

    public String keepSizeWaterMark(Auth auth, String bucket, String domainOfBucket, String pipeline,
                                    String sourthVideoKey, String targetFileName, String wmText, String wmImageUrl) {

        double[] wxh = getAvinfo(domainOfBucket, sourthVideoKey);
        double width = wxh[0];
        double height = wxh[1];
        Configuration cfg = new Configuration(Zone.autoZone());
        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth, cfg);

        // 做两层水印处理，里面一层是在一个背景透明的图片上打上水印文字和 log 图片，采用同步处理，得到一个合成的水印图片 URL，这样可以动态调整文字和 log URL 参数，
        // 再对这个 URL 进行 base64 编码得到参数放在视频水印的图片路径参数中可以设置适当偏移。其中控制图片大小的参数是 wmScale，这个使得图片跟随视频大小缩放，但
        // 根据 360.000000/width 来设置这个参数就是表示，自适应的一边保持长度 360，然后除以原视频 width 得到这个比例。
        String pfops = "avthumb/mp4/wmImage/" +
                UrlSafeBase64.encodeToString("http://nigel.qiniuts.com/transparent200x200.png?watermark/3/image/" +
                        UrlSafeBase64.encodeToString(wmImageUrl) +
                        "/gravity/North/text/" +
                        UrlSafeBase64.encodeToString(wmText) +
                        "/fontsize/450/fill/I0ZGRkZGRg==/gravity/Center") +
                "/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30/wmConstant/1/wmScale/" + 360.000000/width + "/wmScaleType/0" +
                "|saveas/" + UrlSafeBase64.encodeToString(bucket + ":" + targetFileName);

        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        String persistid = "";

        try {
            persistid = operater.pfop(bucket, sourthVideoKey, pfops, params);
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

        return persistid;
    }


    public double[] getAvinfo(String domain, String sourthVideoKey) {
        // 先获取视频元信息
        Client client = new Client();
        String responseJson = null;

        try {
            responseJson = client.get(domain + sourthVideoKey + "?avinfo").bodyString();
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        JsonArray jsonArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(responseJson);

        try {
            JsonObject jsonObj = element.getAsJsonObject();
            jsonArray = jsonObj.get("streams").getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int width = 0;
        int height = 0;

        for ( int i = 0; i < jsonArray.size(); i++ ) {
            JsonObject streamJson = jsonArray.get(i).getAsJsonObject();

            if ("video".equals(streamJson.get("codec_type").getAsString())) {
                width = Integer.valueOf(streamJson.get("width").toString());
                height = Integer.valueOf(streamJson.get("height").toString());
            }
        }

        return new double[]{width, height};
    }
}