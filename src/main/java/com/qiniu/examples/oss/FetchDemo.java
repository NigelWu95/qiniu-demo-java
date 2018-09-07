package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class FetchDemo {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        String bucket = "temp";
        String key = "96c49f8cb6b7d6cecf15364533520b8e69bed960.png";
        // 要fetch的url
        String url = "http://xxx.com/test.png";
        FetchRet fetchRet = null;

        try {
            //调用fetch方法抓取文件
            fetchRet = bucketManager.fetch(url, bucket, key);
            System.out.printf(fetchRet.hash);
            System.out.printf(fetchRet.key);
            System.out.printf(fetchRet.mimeType);
            System.out.printf(String.valueOf(fetchRet.fsize));
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.reqId);
            System.out.println(r.statusCode);
            System.out.println(r.toString());
        }
    }

}
