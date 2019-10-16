package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.util.HashMap;

public class ChangeMime {

    public static void main(String[] args) throws QiniuException {
        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        bucketManager.changeMime("temp", "qiniu_success_1.txt", "text/test");
        bucketManager.changeHeaders("temp", "qiniu_success_1.txt", new HashMap<String, String>(){{
            put("key1", "value1");
            put("key2", "value2");
        }});

        Client client = new Client();
        String url = "http://rs.qiniu.com/chgm/dGVtcDpxaW5pdV9zdWNjZXNzXzEudHh0/x-qn-meta-key3/dmFsdWUx/x-qn-meta-key4/dmFsdWUy";
        StringMap headers = auth.authorization(url, null, Client.FormMime);
        Response response = client.post(url, null, headers, Client.FormMime);
        System.out.println(response.bodyString());
        System.out.println(response.statusCode);
    }
}
