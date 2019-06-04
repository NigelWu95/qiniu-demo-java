package com.qiniu.examples;

import com.qiniu.datasource.IDataSource;
import com.qiniu.entry.CommonParams;
import com.qiniu.entry.QSuitsEntry;
import com.qiniu.interfaces.IEntryParam;
import com.qiniu.process.qoss.CopyFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class QSuitsDemo2 {

    public static void main(String[] args) throws Exception {
        QSuitsEntry qSuitsEntry = new QSuitsEntry(args);
        IEntryParam entryParam = qSuitsEntry.getEntryParam();
        Map<String, String> paramsMap = entryParam.getParamsMap();
        String savePath = paramsMap.get("save-path");
        CommonParams commonParams = qSuitsEntry.getCommonParams();
        CopyFile processor = (CopyFile) qSuitsEntry.getProcessor();
        IDataSource dataSource = qSuitsEntry.getDataSource();
        boolean saveTotal = commonParams.getSaveTotal();
        String saveFormat = commonParams.getSaveFormat();
        String saveSeparator = commonParams.getSaveSeparator();
        Set<String> rmFields = commonParams.getRmFields();
        String path = commonParams.getPath();

        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) throw new IOException("no files in the path.");
        String bucket;
        for (File f : files) {
            bucket = f.getName();
            commonParams.setPath(f.getAbsolutePath());
            commonParams.setBucket(bucket + "-src");
            commonParams.setSavePath(savePath + "/" + bucket);
            if (processor != null) {
                processor.updateCopy(bucket + "-src", bucket, null, null, null);
                processor.updateSavePath(savePath + "/" + bucket);
            }
            if (dataSource != null) {
                dataSource.setSaveOptions(savePath, saveTotal, saveFormat, saveSeparator, rmFields);
                dataSource.updateSettings(commonParams);
                dataSource.setProcessor(processor);
                dataSource.export();
            }
        }

        if (processor != null) processor.closeResource();
    }
}
