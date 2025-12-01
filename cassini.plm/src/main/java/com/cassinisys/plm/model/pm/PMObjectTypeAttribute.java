package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_PMOBJECTTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PMObjectTypeAttribute extends ObjectTypeAttribute {
    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "SEQUENCE")
    private Integer sequence;

    public PMObjectTypeAttribute() {
    }
}
