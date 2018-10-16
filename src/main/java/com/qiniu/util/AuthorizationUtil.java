package com.qiniu.util;

import com.qiniu.common.Config;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.storage.Configuration;

public class AuthorizationUtil {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration configuration = new Configuration(Zone.autoZone());
        upToken(auth, "bucket", null, 2000000000, 7);
        dropBucketAuthorization(auth, "bucket");
        fetchAuthorization(auth, "", "bucket", "");
        domainsOfBucketAuthorization(auth, "", "bucket");
        pfopAuthorization(auth, configuration, "", "bucket", "", "avthumb/mp4...");
    }

    public static String upToken(Auth auth, String bucket, String scopeFileKey, long expires, int deleteAfterDays) {
        String upToken = auth.uploadToken(bucket, scopeFileKey, expires, new StringMap()
                .put("deleteAfterDays", deleteAfterDays)
                .put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fsize\":$(fsize),\"bucket\":\"$(bucket)\",\"name\":\"$(x:name)\"}"));
        return upToken;
    }

    public static String upTokenWithPfops(Auth auth, String bucket, String scopeFileKey, long expires, String pfops, String pipeline) {
        String upToken = auth.uploadToken(bucket, scopeFileKey, expires, new StringMap()
                .putNotEmpty("persistentOps", pfops)
                .putNotEmpty("persistentPipeline", pipeline), true);
        return upToken;
    }

    public static String dropBucketAuthorization(Auth auth, String bucket) {
        String dropBucketAuthorization = "QBox " + auth.signRequest("http://rs.qiniu.com/drop/" + bucket, null, null);
        return dropBucketAuthorization;
    }

    public static String fetchAuthorization(Auth auth, String url, String bucket, String customKey) {
        String fetchAuthorization = "QBox " + auth.signRequest("http://iovip.qbox.me/fetch/"
                + UrlSafeBase64.encodeToString(url) + "/to/"
                + UrlSafeBase64.encodeToString(bucket + ":" + customKey), null, null);
        return fetchAuthorization;
    }

    public static String domainsOfBucketAuthorization(Auth auth, String url, String bucket) {
        String domainsOfBucketAuthorization = "QBox " + auth.signRequest("http://api.qiniu.com/v6/domain/list?tbl=" + bucket, null, null);
        return domainsOfBucketAuthorization;
    }

    public static String pfopAuthorization(Auth auth, Configuration configuration, String pipeline, String saveBucket, String saveKey, String fopsCommand) {
        StringMap params = new StringMap().putNotEmpty("pipeline", pipeline);
        params = params == null ? new StringMap() : params;
        params.put("bucket", saveBucket).put("key", saveKey).put("fops", fopsCommand);
        byte[] data = StringUtils.utf8Bytes(params.formString());
        String url = configuration.apiHost(auth.accessKey, "bucket") + "/pfop/";
        String domainsOfBucketAuthorization = "QBox " + auth.signRequest(url, data, Client.FormMime);
        return domainsOfBucketAuthorization;
    }
}