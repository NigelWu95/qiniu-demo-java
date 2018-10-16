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
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 * Description: 从起始和结束位置列举文件
 */
public class ListBucketV2 {

    public static void main(String args[]) throws UnsupportedEncodingException {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        String bucket = "qiniu-suits-test";

//        fileList(auth, bucket, "", "", "", 1000);
//        fileList(auth, bucket, "在", "", "", 1000);
//        fileList(auth, bucket, "|", "", "", 1000);
        fileList(auth, bucket, "", "", "",1);
    }

    /*
    单次列举，可以传递 marker 和 limit 参数，通常采用此方法进行并发处理
     */
    public static void fileList(Auth auth, String bucket, String prefix, String delimiter, String marker, int limit) {

        StringMap map = new StringMap().put("bucket", bucket).putNotEmpty("marker", marker)
                .putNotEmpty("prefix", prefix).putNotEmpty("delimiter", delimiter).putWhen("limit", limit, limit > 0);
        String url = String.format("http://rsf.qbox.me/v2/list?%s", map.formString());
        System.out.println(url);
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("AuthorizationUtil", authorization);
        Client client = new Client();
        Response response = null;

        try {
            response = client.post(url, null, headers, null);
            System.out.println(response.statusCode);
            System.out.println(response.contentType());
            System.out.println(response.bodyString());
//            InputStream inputStream = response.bodyStream();
//            Reader reader = new InputStreamReader(inputStream);
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("result.txt"));
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            bufferedReader.lines().forEach( p -> {
//                try {
//                    bufferedWriter.write(p);
//                    bufferedWriter.newLine();
//                    System.out.println(p);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });

//            bufferedReader.lines().forEach(System.out::println);
//            bufferedReader.close();
//            inputStream.close();
//            reader.close();
//            bufferedWriter.close();
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
