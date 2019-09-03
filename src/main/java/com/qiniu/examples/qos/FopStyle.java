package com.qiniu.examples.qos;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import java.util.HashMap;
import java.util.Map;

public class FopStyle {

    public static void main(String[] args) {
        Config config = Config.getInstance();
        //设置好客户账号的ACCESS_KEY和SECRET_KEY
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        KodoMirror kodoMirrorProcessor = new KodoMirror();
        Map<String, Object> configMap = new FopStyle().setStyle(auth, "temp", "w_300", "imageView2/0/w/300");
        String url = (String) configMap.get("url");
        StringMap headers = (StringMap) configMap.get("headers");
        Client client = new Client();
        try {
            Response response = client.post(url, null, headers, null);
            System.out.println(response.bodyString());
            response.close();
        } catch (QiniuException e) {
            e.printStackTrace();
            if (e.response != null) e.response.close();
        }
    }

    public Map<String, Object> setStyle(Auth auth, String bucket, String name, String style) {
        String host = "uc.qbox.me";
        String url = "http://" + host + "/style/" + bucket + "/name/" + UrlSafeBase64.encodeToString(name) + "/style/"
                + UrlSafeBase64.encodeToString(style);
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("url", url);
        configMap.put("headers", headers);
        return configMap;
    }
}
