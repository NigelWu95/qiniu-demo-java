package com.qiniu.examples.oss;
import java.io.File;
import java.io.FileInputStream;

import com.qiniu.common.Config;
import com.qiniu.util.*;
import okhttp3.*;

public class Base64Upload {

    public static void main(String[] args) throws Exception {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        String bucket = "temp";    //空间名
        String key = "base1.jpg";    //上传的图片名
        String path = "/Users/wubingheng/Documents/表情包/AA421AD353D53EBEFF9923E9A5401DC6.png";//图片路径

        File file = new File(path);
        int length = (int) (file.length());
        byte[] src = new byte[length];
        FileInputStream fis = new FileInputStream(file);
        fis.read(src);
        String file64 = Base64.encodeToString(src, 0);

        //非华东空间需要根据注意事项 1 修改上传域名
        String url = "http://upload.qiniu.com/putb64/" + length + "/key/" + UrlSafeBase64.encodeToString(key);
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + auth.uploadToken(bucket, null, 3600,
                        new StringMap().put("insertOnly", 1)))
                .post(rb).build();

        System.out.println(request.headers());
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        System.out.println(response.code());
        System.out.println(response.body().string());

        response.close();
    }
}