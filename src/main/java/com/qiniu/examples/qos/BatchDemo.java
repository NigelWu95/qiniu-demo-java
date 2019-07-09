package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class BatchDemo {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        //创建Batch类型的operations对象
        BucketManager.BatchOperations operations = new BucketManager.BatchOperations();

        //第一组源空间名、原文件名，目的空间名、目的文件名
        String bucketFrom1 = "bucket1";
        String keyFrom1 = "src_key1";
        String bucketTo1 = "your_bucket";
        String keyTo1 = "dest_key1";
        //第二组源空间名、原文件名，目的空间名、目的文件名
        String bucketFrom2 = "bucket2";
        String keyFrom2 = "src_key2";
        String bucketTo2 = "bucket2";
        String keyTo2 = "dest_key2";
        Response res = null;

        try {
            //调用批量操作的batch方法
            res = bucketManager.batch(operations.addMoveOp(bucketFrom1, keyFrom1, bucketTo1, keyTo1)
                    .addMoveOp(bucketFrom2, keyFrom2, bucketTo2, keyTo2));

            System.out.println(res.reqId);
            System.out.println(res.bodyString());

        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        } finally {
            if (res != null) {
                res.close();
            }
        }
    }
}
