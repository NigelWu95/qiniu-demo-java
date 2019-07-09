package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.SuitsException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

public class QiniuListerDemo {

    public static void main(String[] args) throws SuitsException {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        String bucket = config.getFirstBucketName();
//        String prefix = "fragments/z1.aiwei-video.H50";
        String marker;
        BucketManager bucketManager = new BucketManager(Auth.create(accessKey, secretKey), new Configuration());
        QiniuLister lister = new QiniuLister(bucketManager, bucket, "",
                "eyJjIjowLCJrIjoiZnJhZ21lbnRzL3oxLmFpd2VpLXZpZGVvLkgzODg3ODU4MS8xNTIwNTU1NjkwODc0LTE1MjA1NTU2OTg4NjIudHMifQ==",
                null, null, 10000);
        while (lister.hasNext()) {
            if (lister.currents().size() > 0)
                System.out.println("pause.");
            lister.listForward();
        }
    }
}
