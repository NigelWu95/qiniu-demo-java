package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

public class BucketMain {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        try {
            bucketManager.deleteBucket("");
        } catch (QiniuException e) {
            e.printStackTrace();
            System.out.println(e.code());
            System.out.println(e.error());
        }
    }
}