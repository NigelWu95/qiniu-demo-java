package com.qiniu.examples;

import com.qiniu.common.Zone;
import com.qiniu.datasource.FileInput;
import com.qiniu.entry.CommonParams;
import com.qiniu.entry.QSuitsEntry;
import com.qiniu.examples.media.CustomProcess;
import com.qiniu.examples.media.CustomProcess2;
import com.qiniu.interfaces.IEntryParam;
import com.qiniu.storage.Configuration;

import java.util.List;
import java.util.Map;

public class QSuitsDemo3 {

    public static void main(String[] args) throws Exception {
        args = new String[]{"-config=src/main/resources/.qinu.properties"};
        QSuitsEntry qSuitsEntry = new QSuitsEntry(args);
        Configuration configuration = new Configuration(Zone.autoZone());
        configuration.connectTimeout = 60;
        configuration.readTimeout = 120;
        configuration.writeTimeout = 60;
        IEntryParam entryParam = qSuitsEntry.getEntryParam();
        CommonParams commonParams = qSuitsEntry.getCommonParams();
//        String accessKey = commonParams.getQiniuAccessKey();
//        String secretKey = commonParams.getQiniuSecretKey();
//        String bucket = commonParams.getBucket();
//        String pipeline = entryParam.getValue("pipeline");
//        String fopsIndex = entryParam.getValue("fops-index", null);
////        String urlIndex = entryParam.getValue("url-index");
//        String jsonPath = entryParam.getValue("pfop-config");
//        String jsonPath2 = entryParam.getValue("regex-config");
        String savePath = commonParams.getSavePath();
//        CustomProcess customProcess = new CustomProcess(accessKey, secretKey, configuration, bucket, pipeline,
//                fopsIndex, "url", jsonPath, jsonPath2, savePath);

        CustomProcess2 customProcess = new CustomProcess2(configuration, savePath);

        boolean saveTotal = commonParams.getSaveTotal();
        String saveFormat = commonParams.getSaveFormat();
        String saveSeparator = commonParams.getSaveSeparator();
        List<String> rmFields = commonParams.getRmFields();
        String filePath = commonParams.getPath();
        String parseType = commonParams.getParse();
        String separator = commonParams.getSeparator();
        String rmKeyPrefix = commonParams.getRmKeyPrefix();
        Map<String, String> indexMap = commonParams.getIndexMap();
//        indexMap.put("0", "0");
        FileInput fileInput = new FileInput(filePath, parseType, separator, rmKeyPrefix, indexMap,
                commonParams.getUnitLen(), commonParams.getThreads());
        fileInput.setSaveOptions(savePath, saveTotal, saveFormat, saveSeparator, rmFields);
        fileInput.setRetryTimes(commonParams.getRetryTimes());
        fileInput.setSaveOptions(savePath, saveTotal, saveFormat, saveSeparator, rmFields);
        fileInput.setProcessor(customProcess);
        fileInput.export();
        customProcess.closeResource();
    }
}
