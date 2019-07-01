package com.qiniu.examples.cdn;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qiniu.common.Config;
import com.qiniu.common.Constants;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;

import java.util.HashMap;

public class CdnLogAnalyze {

    public static void main(String[] args) throws Exception {
        String domain = "";
        String startDate = "2019-05-28";
        String endDate = "2019-05-28";
        String freq = "5min";

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(domain);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("domains", jsonArray);
        jsonObject.addProperty("freq", freq);
        jsonObject.addProperty("region", "china");
        jsonObject.addProperty("startDate", startDate);
        jsonObject.addProperty("endDate", endDate);

        System.out.println(Json.encode(jsonObject));

        byte[] body = Json.encode(jsonObject).getBytes(Constants.UTF_8);
        String url = "https://fusion.qiniuapi.com/v2/tune/loganalyze/reqcount";

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        StringMap headers = auth.authorizationV2(url, "POST", body, Client.JsonMime);
        Client client = new Client();
        Response response = client.post(url, body, headers, Client.JsonMime);
        String result = response.bodyString();
        System.out.println(result);
        response.close();
    }
}
