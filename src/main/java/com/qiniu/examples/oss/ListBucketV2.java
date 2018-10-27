package com.qiniu.examples.oss;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import net.sf.json.JSONNull;

import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
        String bucket = "kodo-z0-req-up";

//        fileList(auth, bucket, "", "", "", 1000);
//        fileList(auth, bucket, "在", "", "", 1000);
//        fileList(auth, bucket, "|", "", "", 1000);
        String marker = fileList(auth, bucket, "", "", "",10000);
        while (!StringUtils.isNullOrEmpty(marker)) {
            marker = fileList(auth, bucket, "", "", marker,10000);
        }
    }

    /*
    单次列举，可以传递 marker 和 limit 参数，通常采用此方法进行并发处理
     */
    public static String fileList(Auth auth, String bucket, String prefix, String delimiter, String marker, int limit) {

        StringMap map = new StringMap().put("bucket", bucket).putNotEmpty("marker", marker)
                .putNotEmpty("prefix", prefix).putNotEmpty("delimiter", delimiter).putWhen("limit", limit, limit > 0);
        String url = String.format("http://rsf.qbox.me/v2/list?%s", map.formString());
        System.out.println(url);
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Client client = new Client();
        Response response = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("marker", "");
        try {
            response = client.post(url, null, headers, null);
//            System.out.println(response.statusCode);
//            System.out.println(response.contentType());
            List<String> lines = Arrays.asList(response.bodyString().split("\n"));
//            InputStream inputStream = response.bodyStream();
//            Reader reader = new InputStreamReader(inputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("result.txt", true));
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            lines.forEach( p -> {
//                try {
//                    bufferedWriter.write(p);
//                    bufferedWriter.newLine();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            System.out.println(String.join("\n", lines));
            List<String> fileInfoList = lines.parallelStream()
                    .map(line -> {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject json = jsonParser.parse(line).getAsJsonObject();
                        return json.get("item") instanceof JsonNull ? null : json.get("item").getAsJsonObject().toString();
                    }).filter(Objects::nonNull).collect(Collectors.toList());

            if (fileInfoList != null && fileInfoList.size() > 0) {
                bufferedWriter.write(String.join("\n", fileInfoList));
                bufferedWriter.newLine();
            }
            String line = lines.parallelStream().max(String::compareTo).get();
            JsonParser jsonParser = new JsonParser();
            jsonObject = jsonParser.parse(line).getAsJsonObject();

//            bufferedReader.lines().forEach(System.out::println);
//            bufferedReader.close();
//            inputStream.close();
//            reader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return jsonObject.get("marker").getAsString();
    }

    public String calculateMarker(int type, String key) {
        return UrlSafeBase64.encodeToString("{\"c\":" + type + ",\"k\":\"" + key + "\"}");
    }
}
