package com.qiniu.examples;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class KeepWatermarkSizeDemo {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);

        //设置转码的队列名称
        String pipeline = "xxx";
        //设置要转码的空间和 key，并且这个 key 在你空间中存在
        String bucket = "temp";
        String domainOfBucket = "http://xxx.com/";
        String sourceVideoKey = "test1.mp4";
        String targetFileName = "test2.mp4";
        String wmText = "ID:12345678";
        String wmImageUrl = "https://xxx.com/watermark.png";

        String persistId = new KeepWatermarkSizeDemo().keepSizeWaterMark(auth, bucket, domainOfBucket, pipeline,
                sourceVideoKey, targetFileName, wmText, wmImageUrl);

        System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistId);
    }

    public String keepSizeWaterMark(Auth auth, String bucket, String domainOfBucket, String pipeline,
                                    String sourceVideoKey, String targetFileName, String wmText, String wmImageUrl) {

        double[] wxh = getWidthAndHeight(domainOfBucket, sourceVideoKey);
        double width = wxh[0];
        double height = wxh[1];
        Configuration cfg = new Configuration(Zone.autoZone());
        //新建一个OperationManager对象
        OperationManager operationManager = new OperationManager(auth, cfg);

        // 控制图片大小的参数是 wmScale，这个使得图片跟随视频大小缩放，根据 360.000000/width 来设置这个参数就是表示，自适应的一边保持长度 360，然后除以原视频 width 得到这个比例。
        String pfops = "avthumb/mp4/wmImage/" + UrlSafeBase64.encodeToString(wmImageUrl) +
                "/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30/wmConstant/1/wmScale/" + 360.000000/width + "/wmScaleType/0" +
                "|saveas/" + UrlSafeBase64.encodeToString(bucket + ":" + targetFileName);

        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        String persistId = "";

        try {
            persistId = operationManager.pfop(bucket, sourceVideoKey, pfops, params);
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


    public double[] getWidthAndHeight(String domain, String sourceVideoKey) {
        // 先获取视频元信息
        Client client = new Client();
        String responseJson = null;

        try {
            responseJson = client.get(domain + sourceVideoKey + "?avinfo").bodyString();
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