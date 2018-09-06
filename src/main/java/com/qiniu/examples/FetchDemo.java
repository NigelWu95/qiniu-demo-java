package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class FetchDemo {

    public static void main(String args[]) {
        // 设置需要操作的账号的AK和SK
        Config config = Config.getInstance();
        // 设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);

        //文件保存的空间名和文件名
        String bucket = "temp";
        String key = "96c49f8cb6b7d6cecf15364533520b8e69bed960.png";

        // 要fetch的url
        String url = "http://xxx.com/test.png";

        try {
            //调用fetch方法抓取文件
            bucketManager.fetch(url, bucket, key);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }

}
