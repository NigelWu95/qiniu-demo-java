package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

public class PfopTest {

    public static void main(String[] args) throws QiniuException {

        Config config = Config.getInstance();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Configuration cfg = new Configuration(Zone.autoZone());

        String bucket = "ts-work";
        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth, cfg);
        //设置转码的队列
        String pipeline = "audio-video";
        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String pfops = "avthumb/mp4/ab/160k/ar/44100/acodec/libfaac/r/30/vb/2200k/vcodec/libx264/s/1280x720/autoscale/1/stripmeta/0" +
                "|saveas/" + UrlSafeBase64.encodeToString("temp:" + "dtest-10-3.mp4");

        //设置pipeline参数
        StringMap params = new StringMap()
                .putWhen("force", 1, true)
                .putNotEmpty("pipeline", pipeline)
                .putNotEmpty("notifyURL", "http://xxx");
        try {
            String persistid = operater.pfop("temp", "dtest-10.mp4", pfops, params);
            //打印返回的persistid
            System.out.println("http://api.qiniu.com/status/get/prefop?id=" + persistid);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
}