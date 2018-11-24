
package com.qiniu.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Item {

    private String cmd;
    private Integer code;
    private String desc;
    private String hash;
    private String key;
    private Integer returnOld;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getReturnOld() {
        return returnOld;
    }

    public void setReturnOld(Integer returnOld) {
        this.returnOld = returnOld;
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
        return new HashCodeBuilder().append(cmd).append(code).append(desc).append(hash).append(key).append(returnOld).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Item) == false) {
            return false;
        }
        Item rhs = ((Item) other);
        return new EqualsBuilder().append(cmd, rhs.cmd).append(code, rhs.code).append(desc, rhs.desc).append(hash, rhs.hash).append(key, rhs.key).append(returnOld, rhs.returnOld).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
