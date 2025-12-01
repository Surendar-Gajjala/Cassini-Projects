package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "PLM_WORKFLOWACTIVITY_FORMDATA")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "WORKFLOWSTATUS",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowActivityFormData extends ObjectAttribute {
}
