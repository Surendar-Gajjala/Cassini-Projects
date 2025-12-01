package com.cassinisys.plm.integration.reqif.model;

import java.util.UUID;

public class ImageObject {
    private String identifier = UUID.randomUUID().toString().toLowerCase();
    private String oleFileName;
    private String imageFileName;
    private String dataFileName;
    private String imageData;
    private String width;
    private String height;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOleFileName() {
        return oleFileName;
    }

    public void setOleFileName(String oleFileName) {
        this.oleFileName = oleFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
