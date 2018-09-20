package com.qiniu.examples.oss;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * ClassName: BatchOperateFileByQshell
 * Description: 通过命令行调用 qshell 实现批量 镜像 fetch
 */
public class BatchOperateFileByQshell extends RecursiveTask<List<String>> {

    private static final int THREAD_HOLD = 1;

    private List<String> fileList;
    private String dirPath;
    private String domain;
    private String command;

    public BatchOperateFileByQshell(List<String> fileList, String dirPath, String domain, String command){
        this.fileList = fileList;
        this.dirPath = dirPath;
        this.domain = domain;
        this.command = command.endsWith(" ") ? command : command + " ";
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
            String OS = System.getProperty("os.name").toLowerCase();
            String[] cmd = { "/bin/bash", "-c", "" };

            if (OS.indexOf("windows") >= 0) {
                cmd[0]="cmd";
                cmd[1]="/c";
            }

            Runtime runtime = Runtime.getRuntime();

            try (Stream<String> stream = Files.lines(Paths.get(dirPath + System.getProperty("file.separator") + filePathName))) {
                stream.filter(line -> {
                            String lineKey = line.split("\t")[0];
                            boolean flag = lineKey.endsWith(".jpg") || lineKey.endsWith(".png") ||
                                    lineKey.endsWith(".gif") || lineKey.endsWith(".bmp") ||
                                    !lineKey.contains(".");
                            return flag;
                        })
                    .map(line -> {
                        if (command.contains(" fetch")) {
                            return "http://" + domain + "/" + line.split("\t")[0];
                        } else {
                            return line.split("\t")[0];
                        }
                    })
                    .forEach(new Consumer<String>() {
                        @Override
                        public void accept(String keyLine) {
                            Process process = null;

                            try {
                                cmd[2] = command + keyLine.replace("\n", "");
                                process = runtime.exec(cmd);
                                int exitVal = process.waitFor();

                                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
                                String line = null;
                                StringBuilder sb = new StringBuilder();

                                while ((line = br.readLine()) != null) {
                                    sb.append(line + "\n");
                                }

                                System.out.println(keyLine + "    " + sb.toString());
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
            BatchOperateFileByQshell left = new BatchOperateFileByQshell(leftList, dirPath, domain, command);
            BatchOperateFileByQshell right = new BatchOperateFileByQshell(rightList, dirPath, domain, command);
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

    public static void main(String[] args) {
        String filePath = "";
        String domain = "";
        String command = "";

        if (args.length < 3) {
            System.out.println("Please add path domain and command params.");
            System.out.println("e.g.");
            System.out.println("java BucketListResult /home/ubuntu cdn.nigel.qiniuts.com 'qshell prefetch temp'");
            return;
        } else {
            filePath = args[0];
            domain = args[1];
            command = args[2];
        }

        List<String> pathNames = ListFilePath(filePath);
        ForkJoinPool pool = new ForkJoinPool();
        BatchOperateFileByQshell task = new BatchOperateFileByQshell(pathNames, filePath, domain, command);
        Future<List<String>> result = pool.submit(task);

        try {
            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
