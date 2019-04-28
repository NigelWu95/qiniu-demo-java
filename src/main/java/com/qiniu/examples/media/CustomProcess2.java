package com.qiniu.examples.media;

import com.google.gson.JsonParseException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.model.qdora.Item;
import com.qiniu.model.qdora.PfopResult;
import com.qiniu.process.Base;
import com.qiniu.process.qdora.MediaManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.JsonConvertUtils;

import java.io.IOException;
import java.util.Map;

public class CustomProcess2 extends Base {

    private Client client;
    private MediaManager mediaManager;

    public CustomProcess2(Configuration configuration, String savePath, int saveIndex) throws IOException {
        super("covert", "", "", configuration, null, savePath, saveIndex);
        client = new Client(configuration.clone());
        mediaManager = new MediaManager(configuration.clone(), "http");
    }

    public CustomProcess2(Configuration configuration, String savePath)
            throws IOException {
        this(configuration, savePath, 0);
    }

    public CustomProcess2 clone() throws CloneNotSupportedException {
        CustomProcess2 customProcess2 = (CustomProcess2)super.clone();
        customProcess2.client = new Client(configuration.clone());
        customProcess2.mediaManager = new MediaManager(configuration.clone(), "http");
        return customProcess2;
    }

    @Override
    protected String resultInfo(Map<String, String> line) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : line.keySet()) {
            if (!key.equals("mimeType")) stringBuilder.append(line.get(key)).append("\t");
        }
//        String ret = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }

    @Override
    protected void parseSingleResult(Map<String, String> line, String result) throws IOException {}

    @Override
    protected String singleResult(Map<String, String> line) throws IOException {
        String result = mediaManager.getPfopResultBodyById(line.get("mimeType"));
        if (result != null && !"".equals(result)) {
            PfopResult pfopResult;
            try {
                pfopResult = JsonConvertUtils.fromJson(result, PfopResult.class);
            } catch (JsonParseException e) {
                throw new QiniuException(e, e.getMessage());
            }
            // 可能有多条转码指令
            for (Item item : pfopResult.items) {
                if (item.code == 0) {
                    Response response = client.get("http://pqhutim18.bkt.clouddn.com/" + item.key);
                    String json = response.bodyString();
                    response.close();
                    fileSaveMapper.writeSuccess(resultInfo(line) + "\t" + json, false);
                } else if (item.code == 3)
                    fileSaveMapper.writeError(resultInfo(line) + "\t" + item.error, false);
                else if (item.code == 4)
                    fileSaveMapper.writeKeyFile("waiting", resultInfo(line) + "\t" +
                            JsonConvertUtils.toJsonWithoutUrlEscape(item), false);
                else
                    fileSaveMapper.writeKeyFile("notify_failed", resultInfo(line) + "\t" +
                            JsonConvertUtils.toJsonWithoutUrlEscape(item), false);
            }
            return null;
        } else {
            throw new QiniuException(null, "empty_result");
        }
    }
}
