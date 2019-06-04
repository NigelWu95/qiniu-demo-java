package com.qiniu.examples.media;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qiniu.config.JsonFile;
import com.qiniu.process.Base;
import com.qiniu.sdk.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.PfopUtils;
import com.qiniu.util.StringMap;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class CustomProcess extends Base<Map<String, String>> {

    private StringMap pfopParams;
    private String fopsIndex;
    private String urlIndex;
    private ArrayList<JsonObject> pfopConfigs;
    private JsonArray jsonArray;
    private OperationManager operationManager;

    public CustomProcess(String accessKey, String secretKey, Configuration configuration, String bucket, String pipeline,
                         String fopsIndex, String urlIndex, String jsonPath, String jsonPath2, String savePath,
                         int saveIndex) throws IOException {
        super("pfop", accessKey, secretKey, configuration, bucket, savePath, saveIndex);
        set(pipeline, fopsIndex, urlIndex, jsonPath, jsonPath2);
        this.operationManager = new OperationManager(Auth.create(accessKey, secretKey), configuration.clone());
    }

    public void updateFop(String bucket, String pipeline, String fopsIndex, String jsonPath, String jsonPath2) throws IOException {
        this.bucket = bucket;
        set(pipeline, fopsIndex, urlIndex, jsonPath, jsonPath2);
    }

    private void set(String pipeline, String fopsIndex, String urlIndex, String jsonPath, String jsonPath2) throws IOException {
        this.pfopParams = new StringMap().putNotEmpty("pipeline", pipeline);
        if (jsonPath != null && !"".equals(jsonPath)) {
            this.pfopConfigs = new ArrayList<>();
            JsonFile jsonFile = new JsonFile(jsonPath);
            for (String key : jsonFile.getKeys()) {
                JsonObject jsonObject = PfopUtils.checkPfopJson(jsonFile.getElement(key).getAsJsonObject(), false);
                jsonObject.addProperty("name", key);
                this.pfopConfigs.add(jsonObject);
            }
        } else {
            if (fopsIndex == null || "".equals(fopsIndex)) throw new IOException("please set the fopsIndex or pfop-config.");
            else this.fopsIndex = fopsIndex;
        }
        this.urlIndex = urlIndex;
        jsonArray = new JsonFile(jsonPath2).getElement("routers").getAsJsonArray();
    }

    public CustomProcess(String accessKey, String secretKey, Configuration configuration, String bucket, String pipeline,
                     String fopsIndex, String urlIndex, String jsonPath, String jsonPath2, String savePath) throws IOException {
        this(accessKey, secretKey, configuration, bucket, pipeline, fopsIndex, urlIndex, jsonPath, jsonPath2, savePath, 0);
    }

    public CustomProcess clone() throws CloneNotSupportedException {
        CustomProcess customProcess = (CustomProcess)super.clone();
        customProcess.operationManager = new OperationManager(Auth.create(accessKey, secretKey), configuration.clone());
        return customProcess;
    }

    @Override
    public String resultInfo(Map<String, String> line) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : line.keySet()) {
            stringBuilder.append(line.get(key)).append("\t");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }

    @Override
    public boolean validCheck(Map<String, String> line) {
        return false;
    }

    @Override
    public void parseSingleResult(Map<String, String> line, String result) throws IOException {
    }

    @Override
    public String singleResult(Map<String, String> line) throws IOException {
//        String key = parseReplacedKey(line.get(urlIndex));
        String key = new URL(line.get(urlIndex)).getPath().substring(1);
        if (pfopConfigs != null) {
            for (JsonObject pfopConfig : pfopConfigs) {
                String cmd = PfopUtils.generateFopCmd(key, pfopConfig);
                fileSaveMapper.writeKeyFile(pfopConfig.get("name").getAsString(), resultInfo(line) + "\t" + key +
                        "\t" + operationManager.pfop(bucket, key, cmd, pfopParams), false);
            }
            return null;
        } else {
            fileSaveMapper.writeSuccess(resultInfo(line) + "\t" + key + "\t" +
                    operationManager.pfop(bucket, key, line.get(fopsIndex), pfopParams), false);
            return null;
        }
    }

    private String parseReplacedKey(String line) throws IOException {
        URL url = new URL(line);
        String pattern;
        String repl;
        for (JsonElement jsonElement : jsonArray) {
            pattern = jsonElement.getAsJsonObject().get("pattern").getAsString();
            if (url.getPath().matches(pattern)) {
                repl = jsonElement.getAsJsonObject().get("repl").getAsString().replaceAll("[{}]", "");
                return url.getPath().replaceAll(pattern, repl).substring(1);
            }
        }
        return url.getFile();
    }
}
