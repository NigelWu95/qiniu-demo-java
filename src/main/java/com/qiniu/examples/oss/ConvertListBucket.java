package com.qiniu.examples.oss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassName: ConvertListBucket
 * Description: 将 listbucket 的结果过滤并拼接出 url
 */
public class ConvertListBucket extends RecursiveTask<List<String>> {

    private static final int THREAD_HOLD = 1;

    private List<String> fileList;
    private String dirPath;
    private String domain;

    public ConvertListBucket(List<String> fileList, String dirPath, String domain){
        this.fileList = fileList;
        this.dirPath = dirPath;
        this.domain = domain;
    }

    @Override
    protected List<String> compute() {
        List<String> stringList = new ArrayList<>();
        //如果任务足够小就计算
        boolean canCompute = fileList.size() <= THREAD_HOLD;

        if (canCompute) {
            String filePathName = fileList.get(0);
            Path path = Paths.get(dirPath + System.getProperty("file.separator") + "re_" + filePathName);
            List<String> list = new ArrayList<>();
            stringList.add(path.toString());

            try (Stream<String> stream = Files.lines(Paths.get(dirPath + System.getProperty("file.separator") + filePathName))) {
                list = stream.filter(
                            line -> {
                            String lineKey = line.split("\t")[0];
                            boolean flag = lineKey.endsWith(".jpg") || lineKey.endsWith(".png") ||
                                    lineKey.endsWith(".gif") || lineKey.endsWith(".bmp") ||
                                    !lineKey.contains(".");
                            return flag;
                        })
                        .map(line -> "http://" + domain + "/" + line.split("\t")[0])
                        .collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                File newFile = path.toFile();
                while (!newFile.exists()) {
                    newFile.createNewFile();
                }
                BufferedWriter writer = Files.newBufferedWriter(path);

                for (String key : list) {
                    writer.write(key);
                    writer.newLine();
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<String> leftList = fileList.subList(0, 1);
            List<String> rightList = fileList.subList(1, fileList.size());
            ConvertListBucket left = new ConvertListBucket(leftList, dirPath, domain);
            ConvertListBucket right = new ConvertListBucket(rightList, dirPath, domain);
            //执行子任务
            left.fork();
            right.fork();
            //获取子任务结果
            List<String> lResult = left.join();
            List<String> rResult = right.join();
            stringList.addAll(lResult);
            stringList.addAll(rResult);
        }

        return stringList;
    }

    public static List<String> ListFilePath(String filePath) {

        List<String> pathNames = new ArrayList<String>();
        File or = new File(filePath);
        File[] files = or.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.getName().startsWith(".")) {
                        continue;
                    }
                    pathNames.add(file.getName());
                } else if (file.isDirectory()) {
                    ListFilePath(file.getAbsolutePath());
                }
            }
        }

        return pathNames;
    }

    public static void main(String[] args) {
        String filePath = "";
        String domain = "";

        if (args.length < 2) {
            System.out.println("Please add keys file path and domain params.");
            System.out.println("e.g.");
            System.out.println("java ConvertListBucket /home/ubuntu cdn.xxx.com");
            return;
        } else {
            filePath = args[0];
            domain = args[1];
        }

        List<String> pathNames = ListFilePath(filePath);

        ForkJoinPool pool = new ForkJoinPool();
        ConvertListBucket task = new ConvertListBucket(pathNames, filePath, domain);
        Future<List<String>> result = pool.submit(task);

        try {
            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
