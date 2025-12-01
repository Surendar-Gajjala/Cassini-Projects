package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by GSR on 26-10-2021.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWACTIVITY_FORM_FIELDS")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowActivityFormFields extends ObjectTypeAttribute {

    @Column(name = "WORKFLOWACTIVITY")
    private Integer workflowActivity;

    public PLMWorkflowActivityFormFields() {
    }


}