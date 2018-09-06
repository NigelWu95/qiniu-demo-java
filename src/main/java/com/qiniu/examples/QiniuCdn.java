/**
 * Project Name: com.qiniu.wubingheng
 * File Name: QiniuCdn.java
 * Package Name: com.qiniu.wubingheng
 * Date Time: 2018/4/9  3:53 PM
 * Copyright (c) 2017, xxx  All Rights Reserved.
 */
package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.examples.Config;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * ClassName: QiniuCdn
 * Description: TODO
 * Date Time: 2018/4/9  3:53 PM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.7
 * @see
 */
public class QiniuCdn {

    public static void main(String[] args) {
        Config config = Config.getInstance();

        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = "";
        String SECRET_KEY = "";

        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        String url = "http://api.qiniu.com/domain?marker=null&limit=1000";
        String authorization = "QBox " + auth.signRequestV2(url, null, null, null);
        System.out.println(authorization);

        StringMap headers = new StringMap().put("Authorization", authorization);

        Client client = new Client();
        Response response = null;
        try {
            response = client.get(url, headers);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
