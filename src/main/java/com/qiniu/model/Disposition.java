
package com.qiniu.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Disposition {

    private Integer _default;
    private Integer dub;
    private Integer original;
    private Integer comment;
    private Integer lyrics;
    private Integer karaoke;
    private Integer forced;
    private Integer hearingImpaired;
    private Integer visualImpaired;
    private Integer cleanEffects;
    private Integer attachedPic;
    private Integer timedThumbnails;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getDefault() {
        return _default;
    }

    public void setDefault(Integer _default) {
        this._default = _default;
    }

    public Integer getDub() {
        return dub;
    }

    public void setDub(Integer dub) {
        this.dub = dub;
    }

    public Integer getOriginal() {
        return original;
    }

    public void setOriginal(Integer original) {
        this.original = original;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getLyrics() {
        return lyrics;
    }

    public void setLyrics(Integer lyrics) {
        this.lyrics = lyrics;
    }

    public Integer getKaraoke() {
        return karaoke;
    }

    public void setKaraoke(Integer karaoke) {
        this.karaoke = karaoke;
    }

    public Integer getForced() {
        return forced;
    }

    public void setForced(Integer forced) {
        this.forced = forced;
    }

    public Integer getHearingImpaired() {
        return hearingImpaired;
    }

    public void setHearingImpaired(Integer hearingImpaired) {
        this.hearingImpaired = hearingImpaired;
    }

    public Integer getVisualImpaired() {
        return visualImpaired;
    }

    public void setVisualImpaired(Integer visualImpaired) {
        this.visualImpaired = visualImpaired;
    }

    public Integer getCleanEffects() {
        return cleanEffects;
    }

    public void setCleanEffects(Integer cleanEffects) {
        this.cleanEffects = cleanEffects;
    }

    public Integer getAttachedPic() {
        return attachedPic;
    }

    public void setAttachedPic(Integer attachedPic) {
        this.attachedPic = attachedPic;
    }

    public Integer getTimedThumbnails() {
        return timedThumbnails;
    }

    public void setTimedThumbnails(Integer timedThumbnails) {
        this.timedThumbnails = timedThumbnails;
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
        return new HashCodeBuilder().append(_default).append(dub).append(original).append(comment).append(lyrics).append(karaoke).append(forced).append(hearingImpaired).append(visualImpaired).append(cleanEffects).append(attachedPic).append(timedThumbnails).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Disposition) == false) {
            return false;
        }
        Disposition rhs = ((Disposition) other);
        return new EqualsBuilder().append(_default, rhs._default).append(dub, rhs.dub).append(original, rhs.original).append(comment, rhs.comment).append(lyrics, rhs.lyrics).append(karaoke, rhs.karaoke).append(forced, rhs.forced).append(hearingImpaired, rhs.hearingImpaired).append(visualImpaired, rhs.visualImpaired).append(cleanEffects, rhs.cleanEffects).append(attachedPic, rhs.attachedPic).append(timedThumbnails, rhs.timedThumbnails).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
