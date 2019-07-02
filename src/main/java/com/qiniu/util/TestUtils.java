package com.qiniu.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtils {

    private static int index = 0;
    private static int index_main = 0;
    private static AtomicInteger atomIndex = new AtomicInteger(0);
    private static ConcurrentMap<Integer, Integer> indexMap = new ConcurrentHashMap<>();

    public static void increasePrint() {
        System.out.println(index++);
        System.out.println(atomIndex.addAndGet(1));
        indexMap.put(index, index);
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorPool = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 1000; i++) {
            System.out.println("main thread: " + index_main++);
            executorPool.execute(TestUtils::increasePrint);
        }
        executorPool.shutdown();
        while (!executorPool.isTerminated()) Thread.sleep(10);
        System.out.println("atom size: " + atomIndex.get());
        System.out.println("size: " + indexMap.size());
    }
}
