package com.qiniu.examples.cdn;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

public class DomainListDemo {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        String bucket = "bucket";

        try {
            String[] domainLists = bucketManager.domainList(bucket);

            for( String domain : domainLists ) {
                System.out.print(domain);
            }
        } catch (QiniuException e) {
            System.out.printf(e.response.reqId);
            System.out.printf(String.valueOf(e.response.statusCode));
            System.out.printf(e.response.error);
        }
    }
}
