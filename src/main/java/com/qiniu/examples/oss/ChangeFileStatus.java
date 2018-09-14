package com.qiniu.examples.oss;

import com.qiniu.common.Config;
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

        String bucket = "";
        String key = "";
        String url = "http://rs.qiniu.com/chstatus/" + UrlSafeBase64.encodeToString(bucket + ":" + key) + "/status/" + 1;
        String accessToken = "Qbox " + auth.signRequestV2(url, "POST", null, Client.FormMime);
        StringMap headers = new StringMap();
        headers.put("Authorization", accessToken);
        Client client = new Client();
        Response resp = null;

        try {
            resp = client.post(url, null, headers, Client.FormMime);
            System.out.println(resp.statusCode);
            System.out.println(resp.reqId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }
}