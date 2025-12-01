package com.cassinisys.plm.model.mfr;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by smukka on 10-03-2022.
 */
@Entity
@Table(name = "PLM_MFRPART_INSPECTIONREPORT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMMfrPartInspectionReport extends PLMFile {

    @Column(name = "MANUFACTURER_PART")
    private Integer manufacturerPart;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    public PLMMfrPartInspectionReport() {
        super.setObjectType(PLMObjectType.MFRPARTINSPECTIONREPORT);
    }

    public Integer getManufacturerPart() {
        return manufacturerPart;
    }

    public void setManufacturerPart(Integer manufacturerPart) {
        this.manufacturerPart = manufacturerPart;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public PLMLifeCyclePhase getLifeCyclePhase() {
        return lifeCyclePhase;
    }

    public void setLifeCyclePhase(PLMLifeCyclePhase lifeCyclePhase) {
        this.lifeCyclePhase = lifeCyclePhase;
    }
}
