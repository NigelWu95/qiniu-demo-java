/**
 * Project Name: com.qiniu.sdkdemo
 * File Name: PfopsMain.java
 * Package Name: com.qiniu.sdkdemo
 * Date Time: 30/11/2017  4:13 PM
 * Copyright (c) 2017, xxx_xxx  All Rights Reserved.
 */
package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

/**
 * ClassName: PfopsMain
 * Description: TODO
 * Date Time: 30/11/2017  4:13 PM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.8
 * @see
 */
public class PfopsMain {

    public static void main(String[] args) throws QiniuException {
//
//        String bucket = "yinian";
//        String accessKey = "";
//        // 七牛云密钥
//        String secretKey = "";
//        Auth auth = Auth.create(accessKey, secretKey);
//        Configuration cfg = new Configuration(Zone.autoZone());
//        OperationManager operationManager = new OperationManager(auth, cfg);
//        String url1 = auth.privateDownloadUrl("http://photo.zhuiyinanian.com/50cf5dcb-5cb8-4aa7-a8ff-f092e17add89.jpeg");
//        String url2 = auth.privateDownloadUrl("http://photo.zhuiyinanian.com/34b7a2d7-2b6e-45fa-9843-bcb8858337e3.jpg");
//
//        System.out.println(url1);
//        System.out.println(url2);
//
//        String key = "50cf5dcb-5cb8-4aa7-a8ff-f092e17add89.jpeg";
//        String zipName = "yasuobao1.zip";
//        String encodedEntryURI = UrlSafeBase64.encodeToString(bucket + ":" + zipName);
//
//        //设置转码操作参数
//        String fops = "mkzip/2/url/" + UrlSafeBase64.encodeToString(url1);
//
//        //设置转码的队列
//        String pipeline = "testqueue";
//
//        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
//        String urlbase64 = UrlSafeBase64.encodeToString(bucket + ":3.zip");
//        String pfops = fops + "|saveas/" + urlbase64;
//
//        try {
//            String id = operationManager.pfop(bucket, key, pfops, null);
//            String purl = "http://api.qiniu.com/status/get/prefop?id=" + id;
//
//            System.out.println(purl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Config config = Config.getInstance();

        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();

        //设置要转码的空间和key，并且这个key在你空间中存在
        String bucket = "ts-work";
//        String key = "robots.txt";
//        String domain = "https://nigel.qiniuts.com";
//        String publicUrl = String.format("%s/%s", domain, key);

        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        String finalUrl = auth.privateDownloadUrl(publicUrl);
//        System.out.println(finalUrl);

        Configuration cfg = new Configuration(Zone.autoZone());

        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth, cfg);

//        //设置转码操作参数
//        String fops = "mkzip/2/url/" + UrlSafeBase64.encodeToString(finalUrl)
//                + "/alias/" + UrlSafeBase64.encodeToString("a.txt")
//                + "/url/" + UrlSafeBase64.encodeToString(auth.privateDownloadUrl(domain + "/test.mp4"))
//                + "/alias/" + UrlSafeBase64.encodeToString("b.mp4");
        //设置转码操作参数
        String fops = "mkzip/4/";

        //设置转码的队列
        String pipeline = "audio-video";
        String image_pipeline = "image";
        String document_pipeline = "document";

        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString(bucket + ":mkzip4-test.zip");
        String pfops = fops + "|saveas/" + urlbase64;

//        String encodedImageURL = UrlSafeBase64.encodeToString("http://nigel.qiniuts.com/so-w.jpg");

//        Client client = new Client();
//        Response response = null;
//        System.out.println(client.get("http://temp.nigel.qiniuts.com/test1.mp4?avinfo").bodyString());

        pfops =
//              "avthumb/mp4/ab/160k/ar/44100/acodec/libfaac/r/30/vb/2200k/vcodec/libx264/s/1280x720/autoscale/1/stripmeta/0" +
//            "avthumb/mp4/ab/160k/ar/44100/acodec/libfaac/r/30/vb/5400k/vcodec/libx264/s/1920x1080/autoscale/1/strpmeta/0" +
//            "avthumb/mp4/ss/0/t/357" +
//            "|saveas/" + UrlSafeBase64.encodeToString(bucket + ":" + "n4vgQ7hE8qBbVNM0lLlYUiJ5dMTmmj0Oksqh0Zs5--.mp4");
//            "avthumb/m3u8/noDomain/1/aq/0/pattern/djEtbTN1OC1hcTAvdHMwYmQ4NWNkNmI3YTY3MWJmYjUyNGI3ODI5MWZjNjYzMS1wYXRzbmFwJChjb3VudCkudHM=" +
//            "avthumb/mp4/vcodec/copy/acodec/copy/wmImage/aHR0cHM6Ly90Mi5jZG4uZHViYW9zaGVuZy5jb20vZGVmYXVsdC8yMDE4LzAxLzA1L0ZOZENaRjY2c0k2ZGNSV3pWY0dKcnlTYnJmVVhUZVQ3YlhHS2lFck8ucG5n" +
//            "avthumb/mp4/vcodec/copy" +
//                "avthumb/mp4/ab/128k/ar/44100/acodec/libfaac/r/30/vb/1200k/vcodec/libx264/s/854x480/autoscale/1/stripmeta/0" +
//                "watermark/1/image/" + encodedImageURL +
//                "avthumb/mp4/wmImage/" + UrlSafeBase64.encodeToString("http://nigel.qiniuts.com/meipian-logo.gif") +
//              "avwatermarks/mp4/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/meipian-log-2.gif") + "/wmGravity/Center/wmPos/1/wmDuration/14.2" +
//                      "/wmGravity/Center/wmPos/0/wmDuration/0.1/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-1.ico") +
//                      "/wmGravity/Center/wmPos/0.1/wmDuration/0.2/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-2.ico") +
//                      "/wmGravity/Center/wmPos/0.2/wmDuration/0.3/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-3.ico") +
//                      "/wmGravity/Center/wmPos/0.3/wmDuration/0.4/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon.ico") +
//                      "/wmGravity/Center/wmPos/0.4/wmDuration/0.5/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-1.ico") +
//                      "/wmGravity/Center/wmPos/0.5/wmDuration/0.6/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-2.ico") +
//                      "/wmGravity/Center/wmPos/0.6/wmDuration/0.7/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-3.ico") +
//                      "/wmGravity/Center/wmPos/0.7/wmDuration/0.8/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon.ico") +
//                      "/wmGravity/Center/wmPos/0.8/wmDuration/0.9/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-1.ico") +
//                      "/wmGravity/Center/wmPos/0.9/wmDuration/1/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-2.ico") +
//                      "/wmGravity/Center/wmPos/1/wmDuration/1.1/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-3.ico") +
//                      "/wmGravity/Center/wmPos/1.1/wmDuration/1.2/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon.ico") +
//                      "/wmGravity/Center/wmPos/1.2/wmDuration/1.3/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-1.ico") +
//                      "/wmGravity/Center/wmPos/1.3/wmDuration/1.4/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-2.ico") +
//                      "/wmGravity/Center/wmPos/1.4/wmDuration/1.5/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/favicon-3.ico") +
//                      "/wmGravity/Center/wmPos/0/wmDuration/1/wmImage/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/logo_00036.png") +
//                      "/wmGravity/Center/wmPos/1/wmDuration/29" +
//                "avthumb/m3u8/noDomain/1/segtime/60/vb/240k/hlsKey/" +
//                        UrlSafeBase64.encodeToString("1234567890123456") +
//                        "/hlsKeyUrl/" +
//                        UrlSafeBase64.encodeToString("12345678") +
//                        "/hlsMethod/qiniu-protection-10" +
//                "avthumb/mp4" +
//                        "/vcodec/libx264/r/50" +
//                "avthumb/m3u8/segtime/10" +
//                "avthumb/m3u8/ss/60/t/300" +
//                "avthumb/mp4/noDomain/1/vcodec/libx265/vb/1000k" +
//                "avthumb/mp4/vb/5737640/s/1280x720/aspect/16:9/gop/250" +
//                "avthumb/mp4/vb/2m/autoscale/1/wmImage/aHR0cDovL3d3dy5kYXhpYW5ndi5jb20vZC9maWxlL3dhdGVybWFyay5wbmc=/wmOffsetX/-40/wmOffsetY/40/wmGravity/NorthEast" +
//                "avthumb/mp4/vb/1m/autoscale/1/wmImage/aHR0cDovL3d3dy5kYXhpYW5ndi5jb20vZC9maWxlL3dhdGVybWFyay5wbmc=/wmOffsetX/-40/wmOffsetY/40/wmGravity/NorthEast" +
//                "avthumb/m3u8/noDomain/1/segtime/60/vb/240k/pattern/cm9vbV8wMi9jc2MkKGNvdW50KQ==" +
//                "/hlsKey/Y2luZTEwN3Fpbml1MjEwMg==/hlsKeyUrl/aHR0cHM6Ly8xMDdjaW5lLmNvbS9xaW5pdS9rZXl1cmw=" +
//                "/hlsMethod/qiniu-protection-10" +
//                "adapt/m3u8/multiResolution/320:240,640:480,1280:720,1920:1080/envBandWidth/200000,800000,1700000,2400000/multiVb/200k,1200k,6500k,8500k/hlstime/10" +
//              "imageMogr2/strip/1" +
//              "avthumb/m3u8/hlsKey/ZEg4anFTaHhXQ0pOcHdseA==/hlsKeyUrl/d3Nq/noDomain/1/hlsMethod/qiniu-protection-10/vb/3m" +
//                "avthumb/m3u8/wmImage/aHR0cHM6Ly9zczIubWVpcGlhbi5tZS92aWRlby93YXRlci92aWRlb19sb2dvLmdpZg==/wmOffsetX/-20/wmOffsetY/20/wmPos/1/wmDuration/6" +
//                "/wmImage/aHR0cHM6Ly9zczIubWVpcGlhbi5tZS92aWRlby93YXRlci92aWRlb19sb2dvLmdpZg==/wmPos/1/wmDuration/6" +
//                "avconcat/2/format/mp4/aHR0cDovL25pZ2VsLnFpbml1dHMuY29tL21laXBpYW5fZW5kdmlkZW9fMTI4MHg3MjAubXA0" +
//                "avthumb/mp4/ss/0/t/7|avthumb/mp4/vn/1" +
//                "avthumb/mp4|avthumb/gif/r/5" +
//                "avthumb/mp4/multiArep/aHR0cDovL3RlbXAubmlnZWwucWluaXV0cy5jb20vZHRlc3QtYW9kaW8ubXAz" +
//                "vframe/jpg/offset/7/w/480/h/360|saveas/dGVtcDp0ZXN0LmpwZw==";
//                "vsample/jpg/ss/7/t/180/s/480x360/interval/60/pattern/dmZyYW1lLSQoY291bnQp" +
//                "avwatermarks/mp4/wmImage/aHR0cHM6Ly9zczIubWVpcGlhbi5tZS92aWRlby93YXRlci92aWRlb19sb2dvLmdpZg==/wmGravity/NorthWest" +
//                "/wmImage/aHR0cHM6Ly9zczIubWVpcGlhbi5tZS92aWRlby93YXRlci92aWRlb19sb2dvLmdpZg==/wmGravity/NorthEast" +
//                "avthumb/gif/ss/3/t/1/r/4/s/180x320/autoscale/1" +
//                "avwatermarks/mp4/wmImage/aHR0cHM6Ly95b3UyLmF1dG9pbWcuY24vX2F1dG9ob21lY2FyX196aG91eW91amkvRjI3M0RBNTQ2OEFCMDk0MTRCQ0E1NDdBQkZGMjIzNENGRTZELmpwZw==/wmGravity/SouthEast/wmOffsetX/-5/wmOffsetY/-60/wmText/QOadqOW_l-m5jw==/wmGravityText/SouthEast/wmFontSize/18/wmFontColor/I0NDQ0NDQw==/wmFont/5b6u6L2v6ZuF6buR/wmOffsetX/-5/wmOffsetY/-10" +
//                "watermark/3/image/aHR0cHM6Ly9xbi5rYWloZWlrZWppLmNvbS8lRTclOUYlQTIlRTklODclOEYlRTYlOTklQkElRTglODMlQkQlRTUlQUYlQjklRTglQjElQTFAMngucG5n/gravity/North/dy/100/text/SUQ6MTIzNDU2Nzg=/fontsize/420/fill/I0ZGRkZGRg==/gravity/Center/dx/14" +
//                "watermark/2/text/SUQ6MTIzNDU2Nzg=/fontsize/360/fill/I0ZGRkZGRg==/gravity/Center" +
//                "avthumb/mp4/wmImage/aHR0cDovL3RlbXAubmlnZWwucWluaXV0cy5jb20vdHJhbnNwYXJlbnQtYmFja2dyb3VuZC05LmpwZw==/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30" +
//                "avthumb/m3u8" +
//                "avthumb/aac/ab/96k/ar/44100" +

//                "avthumb/mp4/wmImage/" +
//                    UrlSafeBase64.encodeToString("http://nigel.qiniuts.com/transparent.jpg?imageView2/1/w/200/h/150|watermark/3/image/" +
//                        UrlSafeBase64.encodeToString("https://qn.kaiheikeji.com/watermark_22.png") +
//                        "/gravity/North/text/" +
//                        UrlSafeBase64.encodeToString("ID:12345678") +
//                        "/fontsize/420/fill/I0ZGRkZGRg==/gravity/South|imageView2/1/w/500/h/500") +
//                "/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30/wmConstant/1" +

//                "imageView2/1/w/900/h/900" +
//                "avthumb/mp4/wmImage/" +
//                    UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/watermark-2.jpg") +
//                "/wmGravity/NorthWest/vmOffsetX/-30/vmOffsetY/-30/wmConstant/1/wmScale/1/wmScaleType/0" +
//                "/wmGravity/NorthWest/wmConstant/1/wmScale/1/wmScaleType/0" +
//                "watermark/3/image/" + UrlSafeBase64.encodeToString("https://qn.kaiheikeji.com/watermark_22.png") +
//                "/gravity/North/text/" + UrlSafeBase64.encodeToString("ID:12345678") + "/fontsize/420/fill/I0ZGRkZGRg==/gravity/Center" +
//                "avthumb/mp4/r/30/vb/1.25m/s/1024x768/autoscale/1/aspect/4:3" +
//                "avthumb/m3u8/segtime/10/ab/60k/ar/44100/acodec/libfaac/r/30/h264Crf/28/vcodec/libx264/s/640x320/an/1" +
//                "/pattern/" + UrlSafeBase64.encodeToString( "EP02-01$(count)") +
//                "avthumb/mp4/s/1320x720/autoscale/1/aspect/16:9" +
//                "avwatermarks/m3u8/wmImage/aHR0cHM6Ly9vanBibHkxdW4ucW5zc2wuY29tL2xvZ28ucG5n/wmPos/$(end)/wmDuration/-5" +
//                "|" + "avwatermarks/mp4/wmImage/aHR0cHM6Ly9vanBibHkxdW4ucW5zc2wuY29tL2xvZ28ucG5n/wmPos/0/wmDuration/$(lte)" +
//                "avthumb/mp4/s/640x480/scodec/dvdsub/vb/1.25m" +

//                "avthumb/mp4/wmImage/aHR0cHM6Ly9xbi5rYWloZWlrZWppLmNvbS8lRTclOUYlQTIlRTklODclOEYlRTYlOTklQkElRTglODMlQkQlRTUlQUYlQjklRTglQjElQTFAMngucG5n/wmConstant/1" +
//                "|saveas/" + UrlSafeBase64.encodeToString( "temp:" + "10.mp4");
//                "avthumb/mp4/s/960x540/autoscale/1/wmImage/aHR0cHM6Ly9xbi5rYWloZWlrZWppLmNvbS8lRTclOUYlQTIlRTklODclOEYlRTYlOTklQkElRTglODMlQkQlRTUlQUYlQjklRTglQjElQTFAMngucG5n/wmGravity/NorthWest/vmOffsetX/60/vmOffsetY/25|avthumb/mp4/s/1000x1000/autoscale/1/wmText/SUQ6MTIzNDU2Nzg=/wmFontSize/36/wmFontColor/I0ZGRkZGRg==/wmGravityText/NorthWest/wmOffsetY/125" +
//                "|saveas/" + UrlSafeBase64.encodeToString( "temp:" + "2593faaf76e2650352dfab75356e908b-7.mp4");

//                "avconcat/2/format/mp4/" + UrlSafeBase64.encodeToString("http://temp.nigel.qiniuts.com/douguo-live15270448612503c1384s1898.mp4");
//                "avthumb/mp4/wmImage/aHR0cDovL3BjNXIyYjN6by5ia3QuY2xvdWRkbi5jb20vd2F0ZXIucG5nP2ltYWdlVmlldzIvMC93LzU0/wmOffsetX/-50/wmOffsetY/125" +
//                "avthumb/mp4/wmImage/aHR0cDovL3BjNXIyYjN6by5ia3QuY2xvdWRkbi5jb20vd2F0ZXIucG5nP2ltYWdlVmlldzIvMC93LzU0" +
//                "avthumb/mp3/ab/96k/ar/44100/acodec/libmp3lame" +
//                "|saveas/dGVtcDpkb3VndW8tbGl2ZTIwMTgwNTI0MTExMTExLm1wNA==";
//                "avthumb/mp4/s/960x720/scodec/dvdsub" +
//                "avthumb/mp4/vcodec/libx264/s/847x480/ar/22050/r/30/vb/1.25m" +
//                "avthumb/m3u8/ab/64k/ar/22050/vn/1" +
//                "avthumb/mp4/vn/1" +
                "avwatermarks/mp4/wmImage/" + UrlSafeBase64.encodeToString("https://ss2.meipian.me/video/water/video_logo.gif") + "/wmOffsetX/-20/wmOffsetY/20/wmPos/0/wmDuration/$(gte)" +
                "|saveas/" + UrlSafeBase64.encodeToString( "temp:" + "1536112882703587-1.mp4");
//                "avthumb/mp4/s/320x240/vb/200k|saveas/" + UrlSafeBase64.encodeToString( "temp:" + "1111111222211-240p.mp4") + ";" +
//                "avthumb/mp4/s/640x480/vb/800k|saveas/" + UrlSafeBase64.encodeToString( "temp:" + "1111111222211-480p.mp4") + ";" +
//                "avthumb/mp4/s/1280x720/vb/1700k|saveas/" + UrlSafeBase64.encodeToString( "temp:" + "1111111222211-720p.mp4");

        //设置pipeline参数
//        StringMap params = new StringMap()
//                .putWhen("force", 1, true)
//                .putNotEmpty("pipeline", pipeline)
//                .putNotEmpty("notifyURL", "http://753637b3.ngrok.io/QiniuDemo/callback");

//        params = params == null ? new StringMap() : params;
//        params.put("bucket", "temp").put("key", "1111111222211.mp4").put("fops", pfops);
//        byte[] data = StringUtils.utf8Bytes(params.formString());
//        String url = cfg.apiHost(auth.accessKey, bucket) + "/pfop/";
//        StringMap headers = auth.authorization(url, data, Client.FormMime);
//        System.out.println(params.formString());
//        System.out.println(url);
//        System.out.println(headers.get("Authorization"));


        //设置pipeline参数
        StringMap params = new StringMap()
                .putWhen("force", 1, true)
                .putNotEmpty("pipeline", pipeline);
        try {
//            String persistid = operater.pfop("ts-work", "4EC3342A-608D-4CB7-BD37-BCCB967F0F00.mp4", pfops, params);
            String persistid = operater.pfop("temp", "1536112882703587.mp4", pfops, params);
            //打印返回的persistid
            System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistid);
//            System.out.println(operater.prefop(persistid));

//            StringMap params2 = new StringMap().put("id", persistid);
//            byte[] data = StringUtils.utf8Bytes(params2.formString());
//
//            String url = String.format("http://api.qiniu.com/status/get/prefop");
//            Response response = new Client().post(url, data, null, Client.FormMime);
//            System.out.println(response.jsonToObject(OperationStatus.class).code);
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
    }
}