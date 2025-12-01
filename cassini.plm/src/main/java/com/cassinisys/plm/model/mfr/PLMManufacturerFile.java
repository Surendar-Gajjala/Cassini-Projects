package com.cassinisys.plm.model.mfr;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Home on 4/25/2016.
 */
@Entity
@Table(name = "PLM_MANUFACTURERFILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMManufacturerFile extends PLMFile {

    @Column(name = "MANUFACTURER")
    private Integer manufacturer;

    public Integer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Integer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
