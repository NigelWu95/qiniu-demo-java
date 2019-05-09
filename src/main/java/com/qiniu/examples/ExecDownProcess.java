package com.qiniu.examples;

import com.qiniu.common.QiniuException;
import com.qiniu.process.Base;
import com.qiniu.storage.Configuration;

import java.io.IOException;
import java.util.Map;

public class ExecDownProcess extends Base {

    private String domain;
    private String[] cmd = { "/bin/bash", "-c", "" };
    private String curlOther;
    private Runtime runtime = Runtime.getRuntime();

    public ExecDownProcess(Configuration configuration, String domain, String curlOther, String savePath, int saveIndex) throws IOException {
        super("execdown", "", "", configuration, "", savePath, saveIndex);
        this.domain = domain;
        this.curlOther = curlOther;
    }

    public ExecDownProcess(Configuration configuration, String domain, String curlOther, String savePath)
            throws IOException {
        this(configuration, domain, curlOther, savePath, 0);
    }

    public ExecDownProcess clone() throws CloneNotSupportedException {
        return (ExecDownProcess)super.clone();
    }

    @Override
    protected String singleResult(Map<String, String> line) throws QiniuException {
        cmd[2] = "curl http://" + domain + line.get("key") + " " + curlOther;
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
