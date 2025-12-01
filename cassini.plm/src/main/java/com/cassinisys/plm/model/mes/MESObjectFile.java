package com.cassinisys.plm.model.mes;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_OBJECT_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESObjectFile extends PLMFile{

    @Column(name = "OBJECT", nullable = false)
    private Integer object;


    public Integer getObject() {
        return object;
    }

    public void setObject(Integer object) {
        this.object = object;
    }
}
