
package com.qiniu.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Stream {

    private Integer index;
    private String codecName;
    private String codecLongName;
    private String profile;
    private String codecType;
    private String codecTimeBase;
    private String codecTagString;
    private String codecTag;
    private Integer width;
    private Integer height;
    private Integer codedWidth;
    private Integer codedHeight;
    private Integer hasBFrames;
    private String sampleAspectRatio;
    private String displayAspectRatio;
    private String pixFmt;
    private Integer level;
    private String chromaLocation;
    private Integer refs;
    private String isAvc;
    private String nalLengthSize;
    private String rFrameRate;
    private String avgFrameRate;
    private String timeBase;
    private Integer startPts;
    private String startTime;
    private Integer durationTs;
    private String duration;
    private String bitRate;
    private String bitsPerRawSample;
    private String nbFrames;
    private Disposition disposition;
    private Tags tags;
    private String sampleFmt;
    private String sampleRate;
    private Integer channels;
    private String channelLayout;
    private Integer bitsPerSample;
    private String maxBitRate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getCodecName() {
        return codecName;
    }

    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    public String getCodecLongName() {
        return codecLongName;
    }

    public void setCodecLongName(String codecLongName) {
        this.codecLongName = codecLongName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCodecType() {
        return codecType;
    }

    public void setCodecType(String codecType) {
        this.codecType = codecType;
    }

    public String getCodecTimeBase() {
        return codecTimeBase;
    }

    public void setCodecTimeBase(String codecTimeBase) {
        this.codecTimeBase = codecTimeBase;
    }

    public String getCodecTagString() {
        return codecTagString;
    }

    public void setCodecTagString(String codecTagString) {
        this.codecTagString = codecTagString;
    }

    public String getCodecTag() {
        return codecTag;
    }

    public void setCodecTag(String codecTag) {
        this.codecTag = codecTag;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getCodedWidth() {
        return codedWidth;
    }

    public void setCodedWidth(Integer codedWidth) {
        this.codedWidth = codedWidth;
    }

    public Integer getCodedHeight() {
        return codedHeight;
    }

    public void setCodedHeight(Integer codedHeight) {
        this.codedHeight = codedHeight;
    }

    public Integer getHasBFrames() {
        return hasBFrames;
    }

    public void setHasBFrames(Integer hasBFrames) {
        this.hasBFrames = hasBFrames;
    }

    public String getSampleAspectRatio() {
        return sampleAspectRatio;
    }

    public void setSampleAspectRatio(String sampleAspectRatio) {
        this.sampleAspectRatio = sampleAspectRatio;
    }

    public String getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    public void setDisplayAspectRatio(String displayAspectRatio) {
        this.displayAspectRatio = displayAspectRatio;
    }

    public String getPixFmt() {
        return pixFmt;
    }

    public void setPixFmt(String pixFmt) {
        this.pixFmt = pixFmt;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getChromaLocation() {
        return chromaLocation;
    }

    public void setChromaLocation(String chromaLocation) {
        this.chromaLocation = chromaLocation;
    }

    public Integer getRefs() {
        return refs;
    }

    public void setRefs(Integer refs) {
        this.refs = refs;
    }

    public String getIsAvc() {
        return isAvc;
    }

    public void setIsAvc(String isAvc) {
        this.isAvc = isAvc;
    }

    public String getNalLengthSize() {
        return nalLengthSize;
    }

    public void setNalLengthSize(String nalLengthSize) {
        this.nalLengthSize = nalLengthSize;
    }

    public String getRFrameRate() {
        return rFrameRate;
    }

    public void setRFrameRate(String rFrameRate) {
        this.rFrameRate = rFrameRate;
    }

    public String getAvgFrameRate() {
        return avgFrameRate;
    }

    public void setAvgFrameRate(String avgFrameRate) {
        this.avgFrameRate = avgFrameRate;
    }

    public String getTimeBase() {
        return timeBase;
    }

    public void setTimeBase(String timeBase) {
        this.timeBase = timeBase;
    }

    public Integer getStartPts() {
        return startPts;
    }

    public void setStartPts(Integer startPts) {
        this.startPts = startPts;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getDurationTs() {
        return durationTs;
    }

    public void setDurationTs(Integer durationTs) {
        this.durationTs = durationTs;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBitRate() {
        return bitRate;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public String getBitsPerRawSample() {
        return bitsPerRawSample;
    }

    public void setBitsPerRawSample(String bitsPerRawSample) {
        this.bitsPerRawSample = bitsPerRawSample;
    }

    public String getNbFrames() {
        return nbFrames;
    }

    public void setNbFrames(String nbFrames) {
        this.nbFrames = nbFrames;
    }

    public Disposition getDisposition() {
        return disposition;
    }

    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public String getSampleFmt() {
        return sampleFmt;
    }

    public void setSampleFmt(String sampleFmt) {
        this.sampleFmt = sampleFmt;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public String getChannelLayout() {
        return channelLayout;
    }

    public void setChannelLayout(String channelLayout) {
        this.channelLayout = channelLayout;
    }

    public Integer getBitsPerSample() {
        return bitsPerSample;
    }

    public void setBitsPerSample(Integer bitsPerSample) {
        this.bitsPerSample = bitsPerSample;
    }

    public String getMaxBitRate() {
        return maxBitRate;
    }

    public void setMaxBitRate(String maxBitRate) {
        this.maxBitRate = maxBitRate;
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
        return new HashCodeBuilder().append(index).append(codecName).append(codecLongName).append(profile).append(codecType).append(codecTimeBase).append(codecTagString).append(codecTag).append(width).append(height).append(codedWidth).append(codedHeight).append(hasBFrames).append(sampleAspectRatio).append(displayAspectRatio).append(pixFmt).append(level).append(chromaLocation).append(refs).append(isAvc).append(nalLengthSize).append(rFrameRate).append(avgFrameRate).append(timeBase).append(startPts).append(startTime).append(durationTs).append(duration).append(bitRate).append(bitsPerRawSample).append(nbFrames).append(disposition).append(tags).append(sampleFmt).append(sampleRate).append(channels).append(channelLayout).append(bitsPerSample).append(maxBitRate).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Stream) == false) {
            return false;
        }
        Stream rhs = ((Stream) other);
        return new EqualsBuilder().append(index, rhs.index).append(codecName, rhs.codecName).append(codecLongName, rhs.codecLongName).append(profile, rhs.profile).append(codecType, rhs.codecType).append(codecTimeBase, rhs.codecTimeBase).append(codecTagString, rhs.codecTagString).append(codecTag, rhs.codecTag).append(width, rhs.width).append(height, rhs.height).append(codedWidth, rhs.codedWidth).append(codedHeight, rhs.codedHeight).append(hasBFrames, rhs.hasBFrames).append(sampleAspectRatio, rhs.sampleAspectRatio).append(displayAspectRatio, rhs.displayAspectRatio).append(pixFmt, rhs.pixFmt).append(level, rhs.level).append(chromaLocation, rhs.chromaLocation).append(refs, rhs.refs).append(isAvc, rhs.isAvc).append(nalLengthSize, rhs.nalLengthSize).append(rFrameRate, rhs.rFrameRate).append(avgFrameRate, rhs.avgFrameRate).append(timeBase, rhs.timeBase).append(startPts, rhs.startPts).append(startTime, rhs.startTime).append(durationTs, rhs.durationTs).append(duration, rhs.duration).append(bitRate, rhs.bitRate).append(bitsPerRawSample, rhs.bitsPerRawSample).append(nbFrames, rhs.nbFrames).append(disposition, rhs.disposition).append(tags, rhs.tags).append(sampleFmt, rhs.sampleFmt).append(sampleRate, rhs.sampleRate).append(channels, rhs.channels).append(channelLayout, rhs.channelLayout).append(bitsPerSample, rhs.bitsPerSample).append(maxBitRate, rhs.maxBitRate).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
