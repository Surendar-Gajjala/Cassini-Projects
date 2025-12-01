package com.cassinisys.pdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_FILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMFile extends PDMVersionedObject{

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "FOLDER")
    private Integer folder;

    @Column(name = "VAULT")
    private Integer vault;

    @Column(name = "THUMBNAIL")
    private byte[] thumbnail;

    public PDMFile(){super(PDMObjectType.FILE);}

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getFolder() {
        return folder;
    }

    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    public Integer getVault() {
        return vault;
    }

    public void setVault(Integer vault) {
        this.vault = vault;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
