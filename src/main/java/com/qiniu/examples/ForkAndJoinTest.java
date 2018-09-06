/**
 * Project Name: com.nigel
 * File Name: ForkAndJoinTest.java
 * Package Name: com.nigel
 * Date Time: 21/03/2018  1:25 AM
 * Copyright (c) 2017, xxx  All Rights Reserved.
 */
package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * ClassName: ForkAndJoinTest
 * Description: TODO
 * Date Time: 21/03/2018  1:25 AM
 * @author Nigel Wu  wubinghengajw@outlook.com
 * @version V1.0
 * @since V1.0
 * @jdk 1.7
 * @see
 */
public class ForkAndJoinTest extends RecursiveTask<Integer> {

    private static final int THREAD_HOLD = 2;

    private List<String> fileKeyList;
    private BucketManager bucketManager;
    private String bucket;

    public ForkAndJoinTest(List<String> fileKeyList, BucketManager bucketManager, String bucket){
        this.fileKeyList = fileKeyList;
        this.bucketManager = bucketManager;
        this.bucket = bucket;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        //如果任务足够小就计算
        boolean canCompute = fileKeyList.size() <= THREAD_HOLD;

        if (canCompute) {
            String startFileKey = fileKeyList.size() == 0 ? "" : fileKeyList.get(0);
            String endFileKey = fileKeyList.size() <= 1 ? "" : fileKeyList.get(1);
            String startJsonStr = "{\"c\":0,\"k\":\"" + startFileKey + "\"}";
            String startMarker = UrlSafeBase64.encodeToString(startJsonStr);
            FileListing fileListing = null;
            String marker = StringUtils.isEmpty(startFileKey) ? "" : startMarker;
            FileInfo[] items = {};

            if (!startFileKey.equals(endFileKey)) {
                loop:while (marker != null) {
                    try {
                        fileListing = bucketManager.listFiles(bucket, null, StringUtils.isEmpty(marker) ? null : marker, 5, null);
                    } catch (QiniuException e) {
                        e.printStackTrace();
                    }

                    marker = fileListing.marker;
                    items = fileListing.items;

                    for (FileInfo fileInfo : items) {
                        sum++;
                        System.out.println(fileInfo.key);

                        if (endFileKey.equals(fileInfo.key)) {
                            break loop;
                        }
                    }
                }
            }
        } else {
            int middleindex = fileKeyList.size() / 2;
            List<String> leftList = fileKeyList.subList(0, middleindex + 1);
            List<String> rightList = fileKeyList.subList(middleindex, fileKeyList.size());
            ForkAndJoinTest left = new ForkAndJoinTest(leftList, bucketManager, bucket);
            ForkAndJoinTest right = new ForkAndJoinTest(rightList, bucketManager, bucket);
            //执行子任务
            left.fork();
            right.fork();
            //获取子任务结果
            int lResult = left.join();
            int rResult = right.join();
            sum = lResult + rResult;
        }

        return sum;
    }

    public static void main(String[] args){
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
        List<String> fileKeyList = new ArrayList<>();
        fileKeyList.add(0, "");
        fileKeyList.add(1, "2965583-r15gop30.m3u8");
        fileKeyList.add(2, "7bNOdFMmkSAixm2ID2IhIsrF5yM=/lhnx9QzsVcDYScDaxzl_L3_m3Alc/000003.ts");
        fileKeyList.add(3, "7bNOdFMmkSAixm2ID2IhIsrF5yM=/lhnx9QzsVcDYScDaxzl_L3_m3Alc/000019.ts");
        fileKeyList.add(4, "7bNOdFMmkSAixm2ID2IhIsrF5yM=/lhnx9QzsVcDYScDaxzl_L3_m3Alc/000031.ts");
        fileKeyList.add(5, "");
        ForkJoinPool pool = new ForkJoinPool();
        ForkAndJoinTest task = new ForkAndJoinTest(fileKeyList, bucketManager, bucket);
        Future<Integer> result = pool.submit(task);

        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}