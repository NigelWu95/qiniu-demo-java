package com.qiniu.examples;

import com.qiniu.common.Config;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DownloadUrlDemo {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String url = "http://bucket_domain/key";

        //调用 privateDownloadUrl 方法生成下载链接,第二个参数可以设置 Token 的过期时间
        String downloadRUL = auth.privateDownloadUrl(url, 3600);
        System.out.println(downloadRUL);


        // 带处理参数和带 "/" 文件名的 url 得到私有链接
        String fileName = "xxx/xxx.mp4";
        String encodedFileName = null;

        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String publicUrl = String.format("%s/%s?%s", "http://xxx.com", encodedFileName, "vframe/jpg/offset/7/w/480/h/360");
        long expireInSeconds = 3600;
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(encodedFileName);
        System.out.println(finalUrl);
    }
}