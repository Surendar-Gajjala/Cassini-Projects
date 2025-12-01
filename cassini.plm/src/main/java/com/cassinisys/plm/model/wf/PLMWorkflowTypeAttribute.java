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
 * Created by Nageshreddy on 04-09-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "WRFL_TYPE")
    private Integer workflowType;

    @Column(name = "SEQ")
    private Integer seq;

    public PLMWorkflowTypeAttribute() {
    }


}