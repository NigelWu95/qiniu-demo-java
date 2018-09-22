package com.qiniu.examples.oss;

import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.BucketManager.*;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.UrlSafeBase64;

/**
 * Description: 从起始和结束位置列举文件
 */
public class ListBucketFIle {

    public static void main(String args[]) {

        Config config = Config.getInstance();
        String accesskey = config.getAccesskey();
        String secretKey = config.getSecretKey();
        Auth auth = Auth.create(accesskey, secretKey);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        String bucket = "bucket";

        fileList(bucket, "", "", 100, bucketManager);
        fileIteratorList(bucket, "", 100, bucketManager);
    }

    /*
    单次列举，可以传递 marker 和 limit 参数，通常采用此方法进行并发处理
     */
    public static void fileList(String bucket, String prefix, String marker, int limit, BucketManager bucketManager) {

        String endMarker = null;

        try {
            FileListing fileListing = bucketManager.listFiles(bucket, prefix, marker, limit, null);
            FileInfo[] items = fileListing.items;

            for (FileInfo fileInfo : items) {
                System.out.println(fileInfo.toString());
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    /*
    迭代器方式列举带 prefix 前缀的所有文件，直到列举完毕，limit 为单次列举的文件个数
     */
    public static void fileIteratorList(String bucket, String prefix, int limit, BucketManager bucketManager) {

        FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, null);

        loop:while (fileListIterator.hasNext()) {
            FileInfo[] items = fileListIterator.next();

            for (FileInfo fileInfo : items) {
                System.out.println(fileInfo.toString());
            }
        }
    }

    public String calculateMarker(int type, String key) {
        return UrlSafeBase64.encodeToString("{\"c\":" + type + ",\"k\":\"" + key + "\"}");
    }
}
