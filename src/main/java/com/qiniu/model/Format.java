
package com.qiniu.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Format {

    private Integer nbStreams;
    private Integer nbPrograms;
    private String formatName;
    private String formatLongName;
    private String startTime;
    private String duration;
    private String size;
    private String bitRate;
    private Integer probeScore;
    private Tags_ tags;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getNbStreams() {
        return nbStreams;
    }

    public void setNbStreams(Integer nbStreams) {
        this.nbStreams = nbStreams;
    }

    public Integer getNbPrograms() {
        return nbPrograms;
    }

    public void setNbPrograms(Integer nbPrograms) {
        this.nbPrograms = nbPrograms;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public String getFormatLongName() {
        return formatLongName;
    }

    public void setFormatLongName(String formatLongName) {
        this.formatLongName = formatLongName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBitRate() {
        return bitRate;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public Integer getProbeScore() {
        return probeScore;
    }

    public void setProbeScore(Integer probeScore) {
        this.probeScore = probeScore;
    }

    public Tags_ getTags() {
        return tags;
    }

    public void setTags(Tags_ tags) {
        this.tags = tags;
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
        return new HashCodeBuilder().append(nbStreams).append(nbPrograms).append(formatName).append(formatLongName).append(startTime).append(duration).append(size).append(bitRate).append(probeScore).append(tags).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Format) == false) {
            return false;
        }
        Format rhs = ((Format) other);
        return new EqualsBuilder().append(nbStreams, rhs.nbStreams).append(nbPrograms, rhs.nbPrograms).append(formatName, rhs.formatName).append(formatLongName, rhs.formatLongName).append(startTime, rhs.startTime).append(duration, rhs.duration).append(size, rhs.size).append(bitRate, rhs.bitRate).append(probeScore, rhs.probeScore).append(tags, rhs.tags).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
