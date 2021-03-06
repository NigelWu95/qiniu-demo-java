package com.qiniu.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ClassName: Config
 * Description: 读取账号配置信息
 */
public class Config {

    private static Config config;

    private String accesskey;
    private String secretKey;
    private String firstBucketName;
    private String filepath;

    public Config() {
        Properties p = new Properties();
        InputStream inStream = this.getClass().getResourceAsStream("/.config.properties");

        try {
            p.load(inStream);
            this.accesskey = p.getProperty("qiniu.access_key");
            this.secretKey = p.getProperty("qiniu.secret_key");
            this.firstBucketName = p.getProperty("qiniu.bucket_name.first");
            this.filepath = p.getProperty("qiniu.filedir");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Config getInstance() {
        if (config == null) {
            config = new Config();
        }

        return config;
    }

    public String getAccesskey() {
        return this.accesskey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getFirstBucketName() {
        return firstBucketName;
    }

    public String getFilepath() { return filepath; }
}