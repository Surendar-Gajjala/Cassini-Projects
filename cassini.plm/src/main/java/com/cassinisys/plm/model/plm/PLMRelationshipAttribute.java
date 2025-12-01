package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 022 22-Jan -18.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_RELATIONSHIPATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMRelationshipAttribute extends ObjectTypeAttribute {

    @Column(name = "RELATIONSHIP")
    private Integer relationship;

}
