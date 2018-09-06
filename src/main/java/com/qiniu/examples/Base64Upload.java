package com.qiniu.examples;
import java.io.File;
import java.io.FileInputStream;
import com.qiniu.util.*;
import okhttp3.*;

public class Base64Upload {

    public static void main(String[] args) throws Exception {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        String bucket = "temp";    //空间名
        String key = "base.jpg";    //上传的图片名
        String file = "xxx";//图片路径

        FileInputStream fis = null;
        int length = (int) (new File(file).length());
        byte[] src = new byte[length];
        fis = new FileInputStream(new File(file));
        fis.read(src);
        String file64 = Base64.encodeToString(src, 0);

        String url = "http://upload.qiniu.com/putb64/" + length + "/key/" + UrlSafeBase64.encodeToString(key);      //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + auth.uploadToken(bucket, null, 3600, new StringMap().put("insertOnly", 1)))
                .post(rb).build();

        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        System.out.println(response.headers());
        System.out.println(response.body().string());
        response.close();
    }
}