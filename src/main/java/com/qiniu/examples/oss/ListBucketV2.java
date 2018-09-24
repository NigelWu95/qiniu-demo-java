package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.BucketManager.FileListIterator;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Description: 从起始和结束位置列举文件
 */
public class ListBucketV2 {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        String bucket = "temp";

        fileList(auth, bucket, "abc", "/", "", 1000);
    }

    /*
    单次列举，可以传递 marker 和 limit 参数，通常采用此方法进行并发处理
     */
    public static void fileList(Auth auth, String bucket, String prefix, String delimiter, String marker, int limit) {

        String url = "http://rsf.qbox.me/v2/list?bucket=" + bucket + "&prefix=" + prefix + "&delimiter=" + delimiter
                + "&limit=" + limit + "&marker=" + marker;
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Client client = new Client();
        Response response = null;

        try {
            response = client.post(url, null, headers, null);
            System.out.println(response.statusCode);
            System.out.println(response.contentType());
//            System.out.println(response.bodyString().equals(""));
            System.out.println(response.bodyStream());
            InputStream inputStream = new BufferedInputStream(response.bodyStream());
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.lines().forEach(p -> System.out.println(p));
            inputStream.close();
            reader.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public String calculateMarker(int type, String key) {
        return UrlSafeBase64.encodeToString("{\"c\":" + type + ",\"k\":\"" + key + "\"}");
    }
}
