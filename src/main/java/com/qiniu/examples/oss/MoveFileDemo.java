package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class MoveFileDemo {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        String bucket = "bucket";
        String key1 = "key1";
        String key2 = "key2";
        Response response = null;

        //将文件从文件 key1 移动到文件 key2, 可以在不同 bucket 移动，同空间移动相当于重命名
        try {
            response = bucketManager.move(bucket, key1, bucket, key2);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}