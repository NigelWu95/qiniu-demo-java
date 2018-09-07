package com.qiniu.examples.cdn;

import com.google.gson.JsonObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.common.Config;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuCdn {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        QiniuCdn qiniuCdn = new QiniuCdn();
        qiniuCdn.listDomains(auth, null, 10);
        qiniuCdn.queryCdnFlux(auth, "2018-08-27", "2018-08-31", "day", "xxx.com");
    }

    public void listDomains(Auth auth, String marker, int limit) {

        String url = "http://api.qiniu.com/domain?marker=" + marker + "&limit=" + limit;
        String authorization = "QBox " + auth.signRequestV2(url, null, null, null);
        System.out.println(authorization);
        StringMap headers = new StringMap().put("Authorization", authorization);

        Client client = new Client();
        Response response = null;

        try {
            response = client.get(url, headers);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public void queryCdnFlux(Auth auth, String startDate, String endDate, String granularity, String domains) {

        String url = "http://fusion.qiniuapi.com/v2/tune/flux";
        //String url = "http://fusion.qiniuapi.com/v2/tune/bandwidth";
        JsonObject object = new JsonObject();
        object.addProperty("StartDate", startDate);
        object.addProperty("EndDate", endDate);
        object.addProperty("Granularity", granularity);
        object.addProperty("Domains", domains);
        System.out.println( object.toString());
        StringMap headers = auth.authorization(url, object.toString().getBytes(), Client.JsonMime);
        System.out.println(headers.get("Authorization"));
        Client client = new Client();
        Response response = null;

        try {
            response = client.post(url, object.toString().getBytes(), headers,Client.JsonMime);
            System.out.println(response.getInfo());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
