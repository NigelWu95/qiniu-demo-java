package com.qiniu.examples.oss;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class FileStatDemo {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Zone z = Zone.autoZone();
        Configuration configuration = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, configuration);
        String bucket = "temp";
        String key = "key/adjlsha";

        try {
            // 调用 stat() 方法获取文件的信息
            FileInfo info = bucketManager.stat(bucket, key);
            System.out.println(info.hash);
            System.out.println(info.key);
        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.reqId);
            System.out.println(r.statusCode);
            System.out.println(r.toString());
        }
    }
}