/**
 * Project Name: com.qiniu.sdkdemo
 * File Name: QpulpMain.java
 * Package Name: com.qiniu.sdkdemo
 * Date Time: 05/02/2018  6:19 PM
 * Copyright (c) 2017, xxx  All Rights Reserved.
 */
package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: QpulpMain
 * Description: TODO
 * Date Time: 05/02/2018  6:19 PM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.7
 * @seex
 */
public class QpulpMain {

    public static void main(String[] args) {

        Config config = Config.getInstance();

        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();

        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        JSONObject jsonData = new JSONObject();
        Map imageAddress = new HashMap();
        imageAddress.put("uri", "http://7xlv47.com1.z0.glb.clouddn.com/pulpsexy.jpg");

        jsonData.put("data", JSONObject.fromObject(imageAddress));

        String s = Json.encode(jsonData);
//        System.out.println(s);
        String url = "http://argus.atlab.ai/v1/pulp";
        byte[] data = StringUtils.utf8Bytes(s);
        String contentType = "application/json";
        String authorization = "Qiniu " + auth.signRequestV2(url, "POST", data, contentType);
//
        StringMap headers = new StringMap().put("Authorization", authorization);
//        headers = new StringMap().put("Authorization", "Qiniu 2-4Tf_7Gaa91wtpGGHCZF6C3Dp1xZbzvnhGw6NDJ:BesLGc62jQ1shVvgz17Z_8cX1io=");
        System.out.println(headers.map());

        Client client = new Client();
        Response response = null;
        try {
            response = client.post(url, data, headers, contentType);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        response.close();

//        String url2 = String.format("http://7xlv47.com1.z0.glb.clouddn.com/pulpsexy.jpg?qpulp");
//        Response response2 = null;
//        try {
//            response2 = new Client().get(url2);
//            PulpAndTerrorResult pulpAndterrorResult = response2.jsonToObject(PulpAndTerrorResult.class);
//            pulpAndterrorResult.getResult().setUrl(url2);
//            System.out.println(pulpAndterrorResult.toString());
//
//            StringMap resultJson = Json.decode(response2.bodyString());
//            System.out.println(resultJson.get("result"));
//            resultJson.put("url", url2);
//            System.out.println(Json.encode(resultJson).toString());
//        } catch (QiniuException e) {
//            e.printStackTrace();
//        }

    }
}
