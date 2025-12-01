package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Home on 4/25/2016.
 */
@Entity
@Table(name = "PLM_MANUFACTURER")
@PrimaryKeyJoinColumn(name = "MFR_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PLMManufacturer extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MFR_TYPE")
    private PLMManufacturerType mfrType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PHONE_NUMBER", nullable = true)
    private String phoneNumber;

    @Column(name = "CONTACT_PERSON", nullable = true)
    private String contactPerson;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer workflowDefId;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private String createPerson;

    @Transient
    private String workflowStatus="None";
    
    @Transient
    private String typeName;
    @Transient
    private String phaseName;

    @Transient
    private String modifiedPerson;

    public PLMManufacturer() {
        super(PLMObjectType.MANUFACTURER);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}

