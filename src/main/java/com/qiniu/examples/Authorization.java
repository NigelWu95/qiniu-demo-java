package com.qiniu.examples;

import com.qiniu.common.Config;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

public class Authorization {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration configuration = new Configuration(Zone.autoZone());

        String upToken = auth.uploadToken("n-america-test", null, 2000000000, new StringMap()
                .put("deleteAfterDays", 7)
                .put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fsize\":$(fsize),\"bucket\":\"$(bucket)\",\"name\":\"$(x:name)\"}"));
        System.out.println(upToken);

        String dropBucketAuthorization = "QBox " + auth.signRequest("http://rs.qiniu.com/drop/" + "bucket", null, null);
        System.out.println(dropBucketAuthorization);

        String fetchAuthorization = "QBox " + auth.signRequest("http://iovip.qbox.me/fetch/"
                + UrlSafeBase64.encodeToString("url") + "/to/"
                + UrlSafeBase64.encodeToString("bucket:key"), null, null);
        System.out.println(dropBucketAuthorization);

        String domainsOfBucketAuthorization = "QBox " + auth.signRequest("http://api.qiniu.com/v6/domain/list?tbl=" + "bucket", null, null);
        System.out.println(domainsOfBucketAuthorization);

        StringMap params = new StringMap().putNotEmpty("pipeline", "");
        params = params == null ? new StringMap() : params;
        params.put("bucket", "temp").put("key", "test.mp4").put("fops", "fops");
        byte[] data = StringUtils.utf8Bytes(params.formString());
        String url = configuration.apiHost(auth.accessKey, "bucket") + "/pfop/";
        StringMap headers = auth.authorization(url, data, Client.FormMime);
        System.out.println(params.formString());
        System.out.println(url);
        System.out.println(headers.get("Authorization"));
    }
}