package com.qiniu.examples.qos;

import com.google.gson.*;
import com.qiniu.common.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.common.SuitsException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QiniuLister {

    private BucketManager bucketManager;
    private String bucket;
    private String prefix;
    private String marker;
    private String endPrefix;
    private String delimiter;
    private int limit;
    private boolean straight;
    private List<FileInfo> fileInfoList;

    public QiniuLister(BucketManager bucketManager, String bucket, String prefix, String marker, String endPrefix,
                       String delimiter, int limit) throws SuitsException {
        this.bucketManager = bucketManager;
        this.bucket = bucket;
        this.prefix = prefix;
        this.marker = marker;
        this.endPrefix = endPrefix;
        this.delimiter = delimiter;
        this.limit = limit;
        doList();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setMarker(String marker) {
        this.marker = marker == null ? "" : marker;
    }

    public String getMarker() {
        return marker;
    }

    public void setEndPrefix(String endPrefix) {
        this.endPrefix = endPrefix;
        checkedListWithEnd();
    }

    public String getEndPrefix() {
        return endPrefix;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setStraight(boolean straight) {
        this.straight = straight;
    }

    public boolean canStraight() {
        return straight || !hasNext() || (endPrefix != null && !"".equals(endPrefix));
    }

    private List<FileInfo> getListResult(String prefix, String delimiter, String marker, int limit) throws QiniuException {
        Response response = bucketManager.listV2(bucket, prefix, marker, limit, delimiter);
        if (response.statusCode != 200) throw new QiniuException(response);
        InputStream inputStream = new BufferedInputStream(response.bodyStream());
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("../" + prefix + "-result.txt", true));
        } catch (IOException e) {
            throw new QiniuException(e, e.getMessage());
        }
        List<FileInfo> fileInfoList = new ArrayList<>();
        try {
            String line;
            JsonObject jsonObject = null;
            JsonObject item;
            while ((line = bufferedReader.readLine()) != null) {
                jsonObject = JsonUtils.toJsonObject(line);
                if (jsonObject.get("item") != null && !(jsonObject.get("item") instanceof JsonNull)) {
                    fileInfoList.add(JsonUtils.fromJson(jsonObject.get("item"), FileInfo.class));
                    item = jsonObject.get("item").getAsJsonObject();
                    writer.write(item.get("key").getAsString() + "\t" + (item.has("x-qn-meta") ?
                            item.get("x-qn-meta").getAsJsonObject().get("!X-CDO-Content-MD5").getAsString() : "no md5."));
                    writer.newLine();
                }
            }
            writer.close();
            if (jsonObject != null && jsonObject.get("marker") != null && !(jsonObject.get("marker") instanceof JsonNull)) {
                this.marker = jsonObject.get("marker").getAsString();
            } else {
                this.marker = null;
            }
            bufferedReader.close();
            reader.close();
            inputStream.close();
            response.close();
        } catch (IOException e) {
            throw new QiniuException(e, e.getMessage());
        }
        return fileInfoList;
    }

    private void checkedListWithEnd() {
        if (endPrefix != null && !"".equals(endPrefix)) {
            int size = fileInfoList.size();
            // SDK 中返回的是 ArrayList，使用 remove 操作性能一般较差，同时也为了避免 Collectors.toList() 的频繁 new 操作，根据返
            // 回的 list 为文件名有序的特性，直接从 end 的位置进行截断
            for (int i = 0; i < size; i++) {
                if (fileInfoList.get(i).key.compareTo(endPrefix) > 0) {
                    fileInfoList = fileInfoList.subList(0, i);
                    marker = null;
                    return;
                }
            }
            // 如果列表的所有元素文件名没有大于 endPrefix 的，则需要判断最后一个元素是否和 endPrefix 存在大于等于关系
            String lastKey = currentLastKey();
            if (lastKey == null || lastKey.compareTo(endPrefix) >= 0) marker = null;
        }
    }

    private void doList() throws SuitsException {
        try {
            fileInfoList = getListResult(prefix, delimiter, marker, limit);
            checkedListWithEnd();
        } catch (QiniuException e) {
            throw new SuitsException(e.code(), LogUtils.getMessage(e));
        } catch (NullPointerException e) {
            throw new SuitsException(400000, "lister maybe already closed, " + e.getMessage());
        } catch (Exception e) {
            throw new SuitsException(-1, "failed, " + e.getMessage());
        }
    }

    public void listForward() throws SuitsException {
        if (!hasNext()) return; doList();
    }

    public boolean hasNext() {
        return marker != null && !"".equals(marker);
    }

    public boolean hasFutureNext() throws SuitsException {
        int times = 50000 / limit;
        times = times > 10 ? 10 : times;
        List<FileInfo> futureList = fileInfoList;
        while (hasNext() && times > 0 && futureList.size() < 10001) {
            times--;
            doList();
            futureList.addAll(fileInfoList);
        }
        fileInfoList = futureList;
        return hasNext();
    }

    public List<FileInfo> currents() {
        return fileInfoList;
    }

    public FileInfo currentLast() {
        FileInfo last = fileInfoList.size() > 0 ? fileInfoList.get(fileInfoList.size() - 1) : null;
        if (last == null && hasNext()) {
            String decodedMarker = new String(Base64.decode(marker, Base64.URL_SAFE | Base64.NO_WRAP));
            JsonObject jsonObject = new JsonParser().parse(decodedMarker).getAsJsonObject();
            last = new FileInfo();
            last.key = jsonObject.get("k").getAsString();
        }
        return last;
    }

    public String currentLastKey() {
        FileInfo last = currentLast();
        return last != null ? last.key : null;
    }

    public void updateMarkerBy(FileInfo object) {
        if (object != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("k", object.key);
            marker = Base64.encodeToString(JsonUtils.toJson(jsonObject).getBytes(Constants.UTF_8),
                    Base64.URL_SAFE | Base64.NO_WRAP);
        } else {
            marker = null;
        }
    }

    public void close() {
        bucketManager = null;
        fileInfoList = null;
    }
}

