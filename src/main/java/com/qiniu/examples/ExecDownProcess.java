package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.process.Base;
import com.qiniu.storage.Configuration;

import java.io.IOException;
import java.util.Map;

public class ExecDownProcess extends Base<Map<String, String>> {

    private String domain;
    private String query;
    private String[] cmd = { "/bin/bash", "-c", "" };
    private String curlOther;
    private Runtime runtime = Runtime.getRuntime();

    public ExecDownProcess(Configuration configuration, String domain, String query, String curlOther, String savePath,
                           int saveIndex) throws IOException {
        super("execdown", "", "", configuration, "", savePath, saveIndex);
        this.domain = domain;
        this.curlOther = curlOther;
        this.query = query;
    }

    public ExecDownProcess(Configuration configuration, String domain, String query, String curlOther, String savePath)
            throws IOException {
        this(configuration, domain, query, curlOther, savePath, 0);
    }

    public ExecDownProcess clone() throws CloneNotSupportedException {
        return (ExecDownProcess)super.clone();
    }

    @Override
    public String resultInfo(Map<String, String> line) {
        return line.get("key");
    }

    @Override
    public boolean validCheck(Map<String, String> line) {
        return true;
    }

    @Override
    public String singleResult(Map<String, String> line) throws QiniuException {
        cmd[2] = "curl http://" + domain + "/" + line.get("key") + query + " " + curlOther;
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
    }
}
