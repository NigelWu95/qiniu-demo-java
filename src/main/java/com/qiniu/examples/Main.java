/**
 * Project Name: com.qiniu.wubingheng
 * File Name: Main.java
 * Package Name: com.qiniu.wubingheng
 * Date Time: 02/11/2017  6:26 PM
 * Copyright (c) 2017, xxx_xxx  All Rights Reserved.
 */
package com.qiniu.examples;

import com.qiniu.examples.Config;
import com.qiniu.util.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * ClassName: Main
 * Description: TODO
 * Date Time: 02/11/2017  6:26 PM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.8
 * @see
 */
public class Main {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();

//        //美篇
//        accessKey = "";
//        secretKey = "";
//
//        String bucket = "nigel-pili";
//        String key = "file key";
        Auth auth = Auth.create(accessKey, secretKey);
//
//        String authorization = "QBox " + auth.signRequest("http://rs.qiniu.com/drop/ivwen-hb", null, null);
//        System.out.println(authorization);

//        String authorization = "QBox " + auth.signRequest("http://iovip.qbox.me/fetch/aHR0cDovL2ltYWdlLmppbmhvbi5jb20uY24vODg4NjNEN0JBNjU0NEFFOUEwMjVDREI2NkMyMzFFMUMvRDU5Qzk1QzJGQjkwNDYwQTkyOUUzRUM1MEQwNkFGMzI=/to/dGVtcDpENTlDOTVDMkZCOTA0NjBBOTI5RTNFQzUwRDA2QUYzMg==", null, null);
//        System.out.println(authorization);

//        auth.privateDownloadUrl("");
//        String token = auth.sign(StringUtils.utf8Bytes("http://temp.nigel.qiniuts.com/1080P%E8%B6%85%E6%B8%85%E9%87%91%E6%B5%B7%E6%B9%96%E7%BE%8E%E6%99%AF.mp4?vframe/jpg/offset/7/w/480/h/360|saveas/dGVtcDp0ZXN0LmpwZw=="));
//        System.out.println(token);
        String fileName = "1080P/超清金海湖美景.mp4";
        String domainOfBucket = "http://temp.nigel.qiniuts.com";
        String encodedFileName = null;

        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String publicUrl = String.format("%s/%s?%s", domainOfBucket, encodedFileName, "vframe/jpg/offset/7/w/480/h/360");
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(encodedFileName);
        System.out.println(finalUrl);
        System.out.println(auth.privateDownloadUrl("http://temp.nigel.qiniuts.com/1080P%E8%B6%85%E6%B8%85%E9%87%91%E6%B5%B7%E6%B9%96%E7%BE%8E%E6%99%AF.mp4?vframe/jpg/offset/7/w/480/h/360"));

//        String authorization_url1 = "QBox " + auth.signRequest("http://api.qiniu.com/v6/domain/list?tbl=ycb-apk", null, null);
//        System.out.println(authorization_url1);
//        String authorization_url2 = "QBox " + auth.signRequest("http://api.qiniu.com/v6/domain/list?tbl=weixinxiaochengxushipin", null, null);
//        System.out.println(authorization_url2);
//
//        String upToken = auth.uploadToken("n-america-test", null, 2000000000, new StringMap()
//                .put("deleteAfterDays", 7));
//                .put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fsize\":$(fsize),\"bucket\":\"$(bucket)\",\"name\":\"$(x:name)\"}"));
//        System.out.println(upToken);
//
//        byte[] byteArray ="VkVyMZBTvY6f3Ayp".getBytes();
//        System.out.println(byteArray[0]);
//
//        StringBuilder hexString = new StringBuilder();
//
//        for (int i = 0; i < byteArray.length; i++) {
//            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
//                hexString.append("0");
//            hexString.append("0x" + Integer.toHexString(0xFF & byteArray[i]) + ",");
//        }
//
//        System.out.println(hexString.toString().toLowerCase());
//
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("thread...");
//                System.out.println(Thread.activeCount());
//            }
//        });

//        OkHttpClient client = new OkHttpClient();
//        String access_token2 = "uaFRcD1RtcOLNe-uQivs2IWkBlfaztHhSQNj-bJELY_WMfsTe9HqCxriVWOB0U16zXASVvqhVovkGFGjKYQ5kAQuuMKBEPu7mdxh_JODn0PmV-x2_IlBTFtIG_97H5FUtg14DzAyHbaBo2lpRjc1jIKcewOWIeVouJZNxgcC-EkYkUe_yUQ5hCwlZTKOgCptz1g1qQ5knFTzFhf2FU_sFA==";
//        String url = "https://acc.qbox.me//user/children?offset=0&limit=10";
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Content-Type","application/x-www-form-urlencoded")
//                .addHeader("Authorization", "Bearer "+ access_token2)
//                .build();
//        Response response = null;
//
//        try {
//            response = client.newCall(request).execute();
//
//            if (response.isSuccessful()) {
//                System.out.println(response.toString());
//                System.out.println(response.code());
//                System.out.println(response.body().string());
//            } else {
//                System.out.println(response.code());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//          byte[] bytes = UrlSafeBase64.decode("aGhoaAphYWFhYQ==");
////        byte[] bytes = UrlSafeBase64.decode(     "aGhoaAphYWFhYQ==");
//        System.out.println(new String(bytes));
//
//        System.out.println(UrlSafeBase64.encodeToString("hhhh\naaaaa"));
//        System.out.println(UrlSafeBase64.encodeToString("黑体"));
    }
}
