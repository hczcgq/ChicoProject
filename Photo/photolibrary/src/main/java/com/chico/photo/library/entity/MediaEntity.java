package com.chico.photo.library.entity;

import java.io.Serializable;

/**
 * Created on 2016/12/15.
 * Author Chico Chen
 */
public class MediaEntity implements Serializable{
    private String path;
    private long duration;
    private long lastUpdateAt;

    public MediaEntity(String path) {
        this.path = path;
    }

    public MediaEntity(String path, long duration, long lastUpdateAt) {
        this.path = path;
        this.duration = duration;
        this.lastUpdateAt = lastUpdateAt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
