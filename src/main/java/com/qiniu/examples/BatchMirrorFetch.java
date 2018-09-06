package com.qiniu.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * ClassName: BatchMirrorFetch
 * Description: 通过命令行调用 qshell 实现批量 镜像 fetch
 */
public class BatchMirrorFetch extends RecursiveTask<List<String>> {

    private static final int THREAD_HOLD = 1;

    private List<String> fileList;
    private String dirPath;

    public BatchMirrorFetch(List<String> fileList, String dirPath){
        this.fileList = fileList;
        this.dirPath = dirPath;
    }

    @Override
    protected List<String> compute() {
        List<String> stringList = new ArrayList<>();
        //如果任务足够小就计算
        boolean canCompute = fileList.size() <= THREAD_HOLD;

        if (canCompute) {
            String filePathName = fileList.get(0);
            String OS = System.getProperty("os.name").toLowerCase();
            String[] cmd = { "/bin/bash", "-c", "" };

            if (OS.indexOf("windows") >= 0) {
                cmd[0]="cmd";
                cmd[1]="/c";
            }

            Runtime runtime = Runtime.getRuntime();

            try (Stream<String> stream = Files.lines(Paths.get(dirPath + System.getProperty("file.separator") + filePathName))) {
                stream.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String UrlLine) {
                        Process process = null;
                        try {
                            cmd[2] = "qshell prefetch testtemp " + UrlLine.replace("\n", "");
                            process = runtime.exec(cmd);
                            int exitVal = process.waitFor();

                            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
                            String line = null;
                            StringBuilder sb = new StringBuilder();

                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }

                            System.out.println(cmd[2] + "" + sb.toString());

                            if (exitVal==0) {
                                System.out.println(process);
                            } else {
                                System.out.println(cmd[2] + "Failed");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            process.destroy();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<String> leftList = fileList.subList(0, 1);
            List<String> rightList = fileList.subList(1, fileList.size());
            BatchMirrorFetch left = new BatchMirrorFetch(leftList, dirPath);
            BatchMirrorFetch right = new BatchMirrorFetch(rightList, dirPath);
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

        if (args.length < 1) {
            System.out.println("Please add path param.");
            System.out.println("e.g.");
            System.out.println("java ConvertListBucket /home/ubuntu");
            return;
        } else {
            filePath = args[0];
        }

        List<String> pathNames = ListFilePath(filePath);
        ForkJoinPool pool = new ForkJoinPool();
        BatchMirrorFetch task = new BatchMirrorFetch(pathNames, filePath);
        Future<List<String>> result = pool.submit(task);

        try {
            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
