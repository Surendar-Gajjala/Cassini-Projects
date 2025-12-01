package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION_PLAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionPlan extends CassiniObject {

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;

    @Transient
    private Integer workflow;
    @Transient
    private String createdPerson;

    @Transient
    private String modifiedPerson;
    @Transient
    private String type;
    @Transient
    private String typeName;
    @Transient
    private String revisionName;
    @Transient
    private String lifeCycleName;
    public PQMInspectionPlan() {
        super.setObjectType(PLMObjectType.INSPECTIONPLAN);
    }
    
    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
