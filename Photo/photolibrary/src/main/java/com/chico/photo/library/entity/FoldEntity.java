package com.chico.photo.library.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2016/12/15.
 * Author Chico Chen
 */
public class FoldEntity implements Serializable{

    private String name;
    private String path;
    private String cover;
    private int number;
    private List<MediaEntity> medias;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<MediaEntity> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaEntity> medias) {
        this.medias = medias;
    }
}
