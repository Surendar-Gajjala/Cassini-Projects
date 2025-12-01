package com.cassinisys.plm.model.cm;

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
@Table(name = "PLM_DCR")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMDCR extends PLMChangeRequest {

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer workflowDfId;
    @Transient
    private String printWorkFlow;
    @Transient
    private String printAnalyst;
    @Transient
    private String type;
    @Transient
    private String createdPerson;
    @Transient
    private String originatorPrint;
    @Transient
    private String requestedByPrint;
    @Transient
    private String modifiedPersonPrint;
    @Transient
    private String qcrPrint;
    @Transient
    private String urgencyPrint;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


    public PLMDCR() {
        super.setChangeType(ChangeType.DCR);
    }


}
