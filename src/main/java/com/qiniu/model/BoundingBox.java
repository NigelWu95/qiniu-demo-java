
package com.qiniu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BoundingBox {

    private List<List<Integer>> pts = new ArrayList<List<Integer>>();
    private Double score;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<List<Integer>> getPts() {
        return pts;
    }

    public void setPts(List<List<Integer>> pts) {
        this.pts = pts;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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
        return new HashCodeBuilder().append(pts).append(score).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BoundingBox) == false) {
            return false;
        }
        BoundingBox rhs = ((BoundingBox) other);
        return new EqualsBuilder().append(pts, rhs.pts).append(score, rhs.score).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
