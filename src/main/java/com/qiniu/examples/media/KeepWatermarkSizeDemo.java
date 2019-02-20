package com.qiniu.examples.media;

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
        String pipeline = "audio-video";
        //设置要转码的空间和 key，并且这个 key 在你空间中存在
        String bucket = "temp";
        String domainOfBucket = "http://temp.xxx.com/";
        String sourceVideoKey = "1080x1920.mp4";
        String targetFileName = "1080x1920-vm1.mp4";
        String wmText = "ID:12345678";
        String wmImageUrl = "https://xxx/test/watermarks/logo/logo-white-1.png";
        String persistId;
        try {
            persistId = new KeepWatermarkSizeDemo().keepSizeWaterMark(auth, bucket, domainOfBucket, pipeline,
                    sourceVideoKey, targetFileName, wmText, wmImageUrl);
            System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistId);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    public String keepSizeWaterMark(Auth auth, String bucket, String domainOfBucket, String pipeline,
                                    String sourceVideoKey, String targetFileName, String wmText, String wmImageUrl)
            throws QiniuException {

        double[] wxh = getWidthAndHeight(domainOfBucket, sourceVideoKey);
        // 原视频的宽高
        double srcVideo_width = wxh[0];
        double srcVideo_height = wxh[1];
        // 目标水印的宽高
        double wm_width = 108.000000;
//        double wm_height = 300.000000;
        // 视频水印自适应类型，根据宽高比选择短边自适应还是长边自适应
        int wmScaleType = (int) (srcVideo_height/srcVideo_width);
        double wmScale = srcVideo_width >= srcVideo_height ? (wm_width/srcVideo_width) : (wm_width/srcVideo_height);

        // 控制图片大小的参数是 wmScale，这个使得图片跟随视频大小缩放，自适应的一边保持长度360，然后除以原视频的短边或者长边得到这个比例。
        String pfops = "avthumb/mp4/wmImage/" + UrlSafeBase64.encodeToString(wmImageUrl) +
                "/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30/wmConstant/1/wmScale/" + wmScale + "/wmScaleType/" +
                wmScaleType + "|saveas/" + UrlSafeBase64.encodeToString(bucket + ":" + targetFileName);

        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        String persistId = "";

        try {
            Configuration cfg = new Configuration(Zone.autoZone());
            OperationManager operationManager = new OperationManager(auth, cfg);
            persistId = operationManager.pfop(bucket, sourceVideoKey, pfops, params);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException ignored) {}
            r.close();
        }

        return persistId;
    }


    public double[] getWidthAndHeight(String domain, String sourceVideoKey) throws QiniuException {
        // 先获取视频元信息
        Client client = new Client();
        String responseJson = client.get(domain + sourceVideoKey + "?avinfo").bodyString();
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