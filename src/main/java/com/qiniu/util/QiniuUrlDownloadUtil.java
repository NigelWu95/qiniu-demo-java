package com.qiniu.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QiniuUrlDownloadUtil {

    public static void main(String[] args) {
        test3();
    }

    public static void test3() {

        try {
            String url = "https://ss2.meipian.me/book/img/sku/calendar/paper-hor-1.jpg";
            String fop = "watermark/1/image/aHR0cDovL3N0YXRpYzIuaXZ3ZW4uY29tL3VzZXJzLzE2NTc0MDc0LzYyOGU2MTU4ZDNmMTQyYTM" +
                    "5ODVmMjE2NTNkNDlmMjA3LmpwZz9pbWFnZU1vZ3IyL2RlbnNpdHkvNDUwL3JvdGF0ZS8wL2Nyb3AvITE0NDB4NzkwYTBhMTMxL3" +
                    "RodW1ibmFpbC8zOTY0eDIxNzU=/gravity/North/dx/0/dy/278%7CimageMogr2/density/450/quality/99/strip";
            boolean result = checkDownload(url + "?" + fop, "/Users/wubingheng/Downloads", false);
            System.out.println(result);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void test2() {

        try {
            urlListDownload("/Users/wubingheng/Downloads/image-url2.txt",
                    "/Users/wubingheng/Downloads/images", true, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void test1() {

        try {
            boolean result = checkDownload("http://temp.nigel.qiniuts.com/test-  2.jpg",
                    "/Users/wubingheng/Downloads", false);
            System.out.println(result);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static final Client client = new Client();

    public static void urlListDownload(String urlListFile, String downloadPath, boolean deleteIfFalse,
                                       boolean tryStripMeta) throws IOException {

        File file = new File(urlListFile);
        if (!(file.exists() && file.isFile() && file.canRead())) {
            throw new IOException("Error: no such file");
        }

        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileReader fileReader = new FileReader(urlListFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.matches("https?://[^\\s/]+\\.[^\\s/\\\\.]{1,3}/.*")) {
                try {
//                    checkDownload(line, downloadPath, deleteIfFalse);
                    if (tryStripMeta) {
                        boolean result = checkDownload(line, downloadPath, deleteIfFalse);

                        if (!result && line.matches(".*\\?.*$"))
                            line += "|imageMogr2/strip";
                            result = checkDownload(line, downloadPath, deleteIfFalse);

                        System.out.println(line + " : " + result);
                    } else {
                        System.out.println(line + " : " + checkDownload(line, downloadPath, deleteIfFalse));
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }

        bufferedReader.readLine();
    }

    public static boolean checkDownload(String url, String downloadPath, boolean deleteIfFalse) throws IOException,
            NoSuchAlgorithmException {

        JsonObject respJson = getQHash(url);
        String responseHash = respJson.get("hash").getAsString();
        long fileSize = respJson.get("fsize").getAsLong();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd-HH-mm-ss-SSS");
        String fileKey = ft.format(new Date()) + url.replaceAll("(https?://[^\\s/]+\\.[^\\s/\\\\.]{1,3}/)|(\\?.*)",
                "").replace("/", "-");
        File file = new File(downloadPath, fileKey);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Response response = null;
        long ioLength = 0;

        try {
            response = client.get(url);
            InputStream inputStream = response.bodyStream();
            ioLength = IOUtils.copyLarge(inputStream, fileOutputStream);
        } catch (QiniuException e) {
            throw e;
        } finally {
            if (response != null)
                response.close();
        }

        boolean fileRight = ioLength == fileSize && HashCheckUtil.isEqualWithHash(file, responseHash);
        if (deleteIfFalse && !fileRight)
            file.delete();

        return fileRight;
    }

    public static JsonObject getQHash(String url) throws QiniuException {

        String host = url.replaceAll("(https?://)|(/[^\\\\?]*)|(\\?.*)", "");
        String srcIOUrl = url.replaceAll("https?://[^\\s/]+\\.[^\\s/\\\\.]{1,3}/", "https://iovip.qbox.me/");

        if (srcIOUrl.matches("https://iovip.qbox.me/[^\\\\?]*\\?.*"))
            srcIOUrl += "|qhash/sha1";
        else
            srcIOUrl += "?qhash/sha1";

        Map<String, Object> map = new HashMap<>();
        map.put("Host", host);
        StringMap stringMap = new StringMap(map);
        Response response = null;
        Gson gson = new Gson();
        JsonObject jsonObject;

        try {
            response = client.get(srcIOUrl, stringMap);
            jsonObject = gson.fromJson(response.bodyString(), JsonObject.class);
        } catch (QiniuException e) {
            throw e;
        } finally {
            if (response != null)
                response.close();
        }

        Set<String> keySet = jsonObject.keySet();
        if (!keySet.contains("hash") || !keySet.contains("fsize"))
            throw new QiniuException(null, "not qhash response");

        return jsonObject;
    }
}