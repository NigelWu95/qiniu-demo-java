package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class BucketStatCount {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        String bucket = "temp";
        String beginTime = "20180820000000";
        String endTime = "20180825235959";
        String region = "z0";
        String url = "http://api.qiniu.com/v6/bucket_net_stat?begin=" + beginTime + "&end="
                + endTime + "&bucket=" + bucket + "&region=" + region;
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("Authorization", authorization);

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