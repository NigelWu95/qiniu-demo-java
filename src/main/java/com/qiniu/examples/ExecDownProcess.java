package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.process.Base;
import com.qiniu.storage.Configuration;

import java.io.IOException;
import java.util.Map;

public class ExecDownProcess extends Base {

    private Client client;
    private String urlIndex;
    private String[] cmd = { "/bin/bash", "-c", "" };
    private Runtime runtime = Runtime.getRuntime();

    public ExecDownProcess(Configuration configuration, String urlIndex, String savePath, int saveIndex) throws IOException {
        super("execdown", "", "", configuration, "", savePath, saveIndex);
//        this.client = new Client(configuration.clone());
        this.urlIndex = urlIndex;
    }

    public ExecDownProcess(Configuration configuration, String urlIndex, String savePath)
            throws IOException {
        this(configuration, urlIndex, savePath, 0);
    }

    public ExecDownProcess clone() throws CloneNotSupportedException {
        ExecDownProcess execDownProcess = (ExecDownProcess)super.clone();
//        execDownProcess.client = new Client(configuration.clone());
        return execDownProcess;
    }

    @Override
    protected String singleResult(Map<String, String> line) throws QiniuException {
        cmd[2] = "curl -o /dev/null " + line.get("key") + " -x chapter2.zhuishushenqi.com.qiniudns.com:80";
        Process process = null;
        int exitVal;
        try {
            process = runtime.exec(cmd);
            exitVal = process.waitFor();
        } catch (Exception e) {
            throw new QiniuException(e, e.getMessage());
        } finally {
            if (process != null) process.destroy();
        }
        if (exitVal != 0) {
            throw new QiniuException(null, exitVal + " exec down failed.");
        } else {
            return "0";
        }
//        Response response = null;
//        try {
//            response = client.get(line.get("key"));
//        } finally {
//            if (response != null) response.close();
//        }
//        return String.valueOf(response.statusCode);
    }
}
