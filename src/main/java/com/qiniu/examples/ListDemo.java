package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.UrlSafeBase64;

public class ListDemo {

    public static void main(String args[]) {
        //设置需要操作的账号的AK和SK
        Config config = Config.getInstance();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = config.getAccesskey();
        String SECRET_KEY = config.getSecretKey();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);

        //要列举文件的空间名
        String bucket = "ts-work";

        try {
            String startFileKey = "149102912";
            String endFileKey = "7bNOdFMmkSAixm2ID2IhIsrF5yM=/lhnx9QzsVcDYScDaxzl_L3_m3Alc/000030.ts";
            String startJsonStr = "{\"c\":0,\"k\":\"" + startFileKey + "\"}";
            String startMarker = UrlSafeBase64.encodeToString(startJsonStr);
            System.out.println(startMarker);

            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
//            FileListing fileListing = bucketManager.listFiles(bucket, null, null, 10, null);

            FileListing fileListing = null;
            String marker = startMarker;
            FileInfo[] items = {};

            fileListing = bucketManager.listFiles(bucket, "7bNOdFMmkSAixm2ID2IhIsrF5yM=/", null, 1000, "/");
            items = fileListing.items;
            String[] commonPrefixes = fileListing.commonPrefixes;

            for (String commonPrefixe : commonPrefixes) {
                System.out.println(commonPrefixe);
            }

            System.out.println("\n\n");

            for (FileInfo fileInfo : items) {
//                        if (endFileKey.equals(fileInfo.key)) {
//                            break loop;
//                        }

                System.out.println(fileInfo.key);
            }

//            String marker = null;
//            if (!startFileKey.equals(endFileKey)) {
//                loop:while (marker != null) {
//                    fileListing = bucketManager.listFiles(bucket, null, marker, 5, "");
//                    marker = fileListing.marker;
//                    items = fileListing.items;
//
//                    for (FileInfo fileInfo : items) {
////                        if (endFileKey.equals(fileInfo.key)) {
////                            break loop;
////                        }
//
//                        System.out.println(fileInfo.key);
//                    }
//
//                }
//            }

//            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, null, 1, null);
//            while (fileListIterator.hasNext()) {
//                //处理获取的file list结果
//                FileInfo[] iitems = fileListIterator.next();
//                for (FileInfo item : iitems) {
//                    System.out.println(item.key);
//                }
//            }
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
