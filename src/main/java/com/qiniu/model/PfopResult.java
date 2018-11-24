
package com.qiniu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PfopResult {

    private Integer code;
    private String desc;
    private String id;
    private String inputBucket;
    private String inputKey;
    private List<Item> items = new ArrayList<Item>();
    private String pipeline;
    private String reqid;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInputBucket() {
        return inputBucket;
    }

    public void setInputBucket(String inputBucket) {
        this.inputBucket = inputBucket;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(desc).append(id).append(inputBucket).append(inputKey).append(items).append(pipeline).append(reqid).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PfopResult) == false) {
            return false;
        }
        PfopResult rhs = ((PfopResult) other);
        return new EqualsBuilder().append(code, rhs.code).append(desc, rhs.desc).append(id, rhs.id).append(inputBucket, rhs.inputBucket).append(inputKey, rhs.inputKey).append(items, rhs.items).append(pipeline, rhs.pipeline).append(reqid, rhs.reqid).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
