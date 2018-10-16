/**
 * Project Name: com.qiniu.sdkdemo
 * File Name: QiniuImagePulp.java
 * Package Name: com.qiniu.sdkdemo
 * Date Time: 05/02/2018  6:19 PM
 * Copyright (c) 2017, xxx  All Rights Reserved.
 */
package com.qiniu.examples.ai;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.model.PulpResult;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: QiniuImagePulp
 * Description: 七牛图片审核 demo
 */
public class QiniuImagePulp {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accessKey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);

        JSONObject jsonData = new JSONObject();
        Map imageAddress = new HashMap();
        imageAddress.put("uri", "http://7xlv47.com1.z0.glb.clouddn.com/pulpsexy.jpg");
        jsonData.put("data", JSONObject.fromObject(imageAddress));
        String s = Json.encode(jsonData);
        String url = "http://argus.atlab.ai/v1/pulp";
        byte[] data = StringUtils.utf8Bytes(s);
        String contentType = "application/json";
        String authorization = "Qiniu " + auth.signRequestV2(url, "POST", data, contentType);
        StringMap headers = new StringMap().put("AuthorizationUtil", authorization);
        System.out.println(headers.map());

        Client client = new Client();
        Response response = null;

        try {
            response = client.post(url, data, headers, contentType);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }


        // get 请求方式
        String url2 = String.format("http://7xlv47.com1.z0.glb.clouddn.com/pulpsexy.jpg?qpulp");
        Response response2 = null;

        try {
            response2 = new Client().get(url2);
            PulpResult pulpResult = response2.jsonToObject(PulpResult.class);
            System.out.println(pulpResult.toString());
            StringMap resultJson = Json.decode(response2.bodyString());
            System.out.println(resultJson.get("result"));
            resultJson.put("url", url2);
            System.out.println(Json.encode(resultJson).toString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }
}
