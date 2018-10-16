package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

public class ChangeFileStatus {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);

        String bucket = "temp";
        String key = "test1.jpg";
        int status = 1; // 0 表示启用，1 表示禁用
        String url = "http://rs.qiniu.com/chstatus/" + UrlSafeBase64.encodeToString(bucket + ":" + key) + "/status/" + status;
        String accessToken = "QBox " + auth.signRequest(url, null, Client.FormMime);
        StringMap headers = new StringMap();
        headers.put("AuthorizationUtil", accessToken);
        Client client = new Client();
        Response resp = null;

        try {
            resp = client.post(url, null, headers, Client.FormMime);
            System.out.println(resp.statusCode);
            System.out.println(resp.reqId);
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }
}