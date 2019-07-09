package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class BucketIOCount {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String bucket = "temp";
        String domain = "";
        String fileType = "0";
        String region = "z0";
        String select = "flow";
        String particleSize = "month";
        String beginTime = "20190301000000";
        String endTime = "20190331000000";
        String source = "cdn_inner";
//        String url = "http://api.qiniu.com/v6/blob_io?$bucket=" + bucket + "&$ftype=" + fileType + "&$region=" + region
//                + "&select=" + select + "&g=" + particleSize + "&begin=" + beginTime + "&end=" + endTime + "&$src=" + source;
//        String url = "http://api.qiniu.com/v6/blob_io?$ftype=" + fileType + "&begin=" + beginTime + "&end=" + endTime
//                + "&g=" + particleSize + "&select=" + select;
//        String url = "http://api.qiniu.com/v6/blob_io?begin=" + beginTime + "&end=" + endTime
//                + "&g=" + particleSize + "&select=" + select;
        String url = "http://api.qiniu.com/v6/blob_io?begin=20190301000000&end=20190331000000";
        String authorization = "QBox " + auth.signRequest(url, null, null);
        System.out.println(authorization);
        StringMap headers = new StringMap().put("Authorization", authorization);
//        System.out.println(headers.formString());

        Client client = new Client();
        Response response = null;

        try {
            response = client.get(url, headers);
            System.out.println(response.statusCode);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}