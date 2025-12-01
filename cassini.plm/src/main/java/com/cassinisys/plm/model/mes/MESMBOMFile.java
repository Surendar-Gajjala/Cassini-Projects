package com.cassinisys.plm.model.mes;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by smukka on 17-05-2022.
 */
@Entity
@Table(name = "MES_MBOMFILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESMBOMFile extends PLMFile {

    @Column(name = "MBOM_REVISION", nullable = false)
    private Integer mbomRevision;

    // public MESMBOMFile() {
    //     super.setObjectType(PLMObjectType.MBOMFILE);
    // }

    public Integer getMbomRevision() {
        return mbomRevision;
    }

    public void setMbomRevision(Integer mbomRevision) {
        this.mbomRevision = mbomRevision;
    }
}
