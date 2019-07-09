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

public class KodoMirror {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        //设置好客户账号的ACCESS_KEY和SECRET_KEY
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();

        //密钥配置
        Auth auth = Auth.create(accessKey, secretKey);
        KodoMirror kodoMirrorProcessor = new KodoMirror();
        Map<String, Object> configMap;

        // 修改镜像回源重试状态码配置
//        configMap = kodoMirrorProcessor.mirrorCodeConfig(auth, "400;403;404");
//        String url = (String) configMap.get("url");
//        StringMap headers = (StringMap) configMap.get("headers");

        configMap = kodoMirrorProcessor.mirrorQueryConfig(auth, 1);
        String url = (String) configMap.get("url");
        StringMap headers = (StringMap) configMap.get("headers");

        Client client = new Client();
        Response response = null;
        try {
            response = client.post(url, null, headers, null);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        response.close();
    }

    public Map<String, Object> mirrorCodeConfig(Auth auth, String statusCode) {
        String bucket = "img-bbs";
        String host = "uc.qbox.me";
        String contentType = "application/json";
        String url = "http://" + host + "/image/" + bucket + "/from/"
                + UrlSafeBase64.encodeToString("http://img5.hefei.cc/;http://img4.hefei.cc/;http://img7.hefei.cc/")
                + "/retryCodes/" + UrlSafeBase64.encodeToString(statusCode);
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Map<String, Object> configMap = new HashMap<String, Object>();
        configMap.put("url", url);
        configMap.put("headers", headers);
        return configMap;
    }

    public Map<String, Object> mirrorQueryConfig(Auth auth, int flag) {
        String bucket = "vb-wting";
        String host = "uc.qbox.me";
        // 设置允许带参数回源，flag 为 1 表示允许，0 为不允许
        String url = "http://" + host + "/mirrorRawQuerySupport?bucket=" + bucket + "&opt=" + flag;
        String authorization = "QBox " + auth.signRequest(url, null, null);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Map<String, Object> configMap = new HashMap<String, Object>();
        configMap.put("url", url);
        configMap.put("headers", headers);
        return configMap;
    }
}