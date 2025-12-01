package com.cassinisys.is.model.pm;

import java.io.Serializable;

/**
 * Created by swapna on 09/05/19.
 */
public class MediaCountDTO implements Serializable {

    private Integer images = 0;

    private Integer videos = 0;

    private Integer files = 0;

    public Integer getImages() {
        return images;
    }

    public void setImages(Integer images) {
        this.images = images;
    }

    public Integer getVideos() {
        return videos;
    }

    public void setVideos(Integer videos) {
        this.videos = videos;
    }

    public Integer getFiles() {
        return files;
    }

    public void setFiles(Integer files) {
        this.files = files;
    }
}
