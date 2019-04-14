package com.qiniu.examples.media;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

public class OperateDemo {

    public static void main(String[] args) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        OperationManager operationManager = new OperationManager(auth, c);
        String bucket = "pfoptest";
        String key = "/vbv1.mp4";
        //设置转码操作参数
        String fops = "avthumb/mp4/vb/650k/vcodec/libx264/s/480x270";
        //设置转码的队列
        String pipeline = "ts-admin-pipleline";
        //可以对转码后的文件进行使用 saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString("pfoptest:vbv1-t1.mp4");
        String pfops = fops + "|saveas/" + urlbase64;
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);

        try {
            String persistId = operationManager.pfop(bucket, key, pfops, params);
            System.out.println(persistId);
        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.reqId);
            System.out.println(r.statusCode);
            System.out.println(r.toString());
        }
    }
}
