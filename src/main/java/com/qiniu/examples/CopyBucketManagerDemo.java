package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class CopyBucketManagerDemo {

    public static void main(String args[]) {
        //设置需要操作的账号的AK和SK
        Config config = Config.getInstance();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = "work-dora";
        String bucket2 = "temp";
        String key = "1.jpg";
        //将文件从文件key 复制到文件key2。 可以在不同bucket复制
        String key2 = "1.jpg";

        try {
            //调用copy方法移动文件
            bucketManager.copy(bucket, key, bucket2, key2);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}