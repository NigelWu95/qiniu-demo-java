package com.qiniu.examples.oss;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.BucketManager.*;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Description: 从起始和结束位置列举文件
 */
public class ListBucketFIle {

    public static void main(String args[]) throws IOException {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        String resultPath = config.getFilepath();
        Auth auth = Auth.create(accesskey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        String bucket = "qiniu-suits-test";

//        Response response = list(auth, "ydb-video", "c2V6aWduOjIwMTblubTlgLzlvpfmjqjojZDnmoQyMOasvkFuZHJvaWQgQXBwLm1wNA==_q00040001_0000", "", 1000, "");
//        String resultBody = response.bodyString();
//        System.out.println(resultBody);

//        fileList(bucket, "", "", 100, bucketManager);
//        fileList(bucket, "", "eyJjIjowLCJrIjoibWVpcGlhbi8wODE0L3VzZXJzLzMyNTgwMzkyL2NiNTdlZGRlMGZmZDRlZTZiNTFjOWQ1Y2E2NGNlNWQ5LmpwZyJ9", 1000, bucketManager);
//        fileIteratorList(bucket, "", 100, bucketManager);
        listWrite(auth, bucket, resultPath);

    }

    public static void listWrite(Auth auth, String bucket, String resultPath) throws IOException {

        Response response = list(auth, bucket, "", "", 1000, "");
        String resultBody = response.bodyString();
        JsonObject jsonObject = new Gson().fromJson(resultBody, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        String result = jsonArray.toString().replaceAll("[\\[\\]]", "").replaceAll("\\},\\{", "\\}\n\\{");
        System.out.println(result);
//        FileWriter fileWriter = new FileWriter(resultPath + "result.txt");
//        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//        fileWriter.write(result);
//        bufferedWriter.write(result);
//        bufferedWriter.newLine();
//        bufferedWriter.close();
//        fileWriter.close();
        response.close();
    }

    /*
    单次列举，可以传递 marker 和 limit 参数，通常采用此方法进行并发处理
     */
    public static void fileList(String bucket, String prefix, String marker, int limit, BucketManager bucketManager) {

        String endMarker = null;

        try {
            FileListing fileListing = bucketManager.listFiles(bucket, prefix, marker, limit, null);
            FileInfo[] items = fileListing.items;

            System.out.println(items.length);
            for (FileInfo fileInfo : items) {
                System.out.println(fileInfo.key);
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    public static Response list(Auth auth, String bucket, String prefix, String marker, int limit, String delimiter) throws QiniuException {
        StringMap map = new StringMap().put("bucket", bucket).putNotEmpty("marker", marker)
                .putNotEmpty("prefix", prefix).putNotEmpty("delimiter", delimiter).putWhen("limit", limit, limit > 0);

        String url = String.format("http://rsf.qbox.me/list?%s", map.formString());
        return new Client().get(url, auth.authorization(url));
    }

    /*
    迭代器方式列举带 prefix 前缀的所有文件，直到列举完毕，limit 为单次列举的文件个数
     */
    public static void fileIteratorList(String bucket, String prefix, int limit, BucketManager bucketManager) {

        FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, null);

        loop:while (fileListIterator.hasNext()) {
            FileInfo[] items = fileListIterator.next();

            for (FileInfo fileInfo : items) {
                System.out.println(fileInfo.toString());
            }
        }
    }

    public String calculateMarker(int type, String key) {
        return UrlSafeBase64.encodeToString("{\"c\":" + type + ",\"k\":\"" + key + "\"}");
    }
}
