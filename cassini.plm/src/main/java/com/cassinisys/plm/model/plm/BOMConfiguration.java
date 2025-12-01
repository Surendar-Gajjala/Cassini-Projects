package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.dto.BomModalDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "BOM_CONFIGURATION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BOMConfiguration extends CassiniObject {

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "RULES")
    private String rules;

    @Transient
    private Boolean hasInstance = Boolean.FALSE;

    @Transient
    private BomModalDto bomModalDto;

    public BOMConfiguration() {
        super(PLMObjectType.BOMCONFIGURATION);
    }


}