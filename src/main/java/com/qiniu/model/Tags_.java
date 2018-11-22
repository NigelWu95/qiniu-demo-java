
package com.qiniu.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Tags_ {

    private String majorBrand;
    private String minorVersion;
    private String compatibleBrands;
    private String encoder;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getMajorBrand() {
        return majorBrand;
    }

    public void setMajorBrand(String majorBrand) {
        this.majorBrand = majorBrand;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public String getCompatibleBrands() {
        return compatibleBrands;
    }

    public void setCompatibleBrands(String compatibleBrands) {
        this.compatibleBrands = compatibleBrands;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
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
        return new HashCodeBuilder().append(majorBrand).append(minorVersion).append(compatibleBrands).append(encoder).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tags_) == false) {
            return false;
        }
        Tags_ rhs = ((Tags_) other);
        return new EqualsBuilder().append(majorBrand, rhs.majorBrand).append(minorVersion, rhs.minorVersion).append(compatibleBrands, rhs.compatibleBrands).append(encoder, rhs.encoder).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
