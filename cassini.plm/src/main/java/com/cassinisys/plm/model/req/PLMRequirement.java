package com.cassinisys.plm.model.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirement extends PLMRequirementObject {

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "PATH")
    private String path;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PLMRequirementType type;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "LATEST_VERSION")
    private Integer latestVersion;

    @Column(name = "IGNORE_FOR_RELEASE")
    private Boolean ignoreForRelease = Boolean.FALSE;

    @Transient
    private Boolean ignoreReqBtn = Boolean.FALSE;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private String workflowSettingStatus = "NONE";
    
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";

}
