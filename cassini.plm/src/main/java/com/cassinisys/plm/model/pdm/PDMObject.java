package com.cassinisys.plm.model.pdm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "@class")
public class PDMObject extends CassiniObject {
    @Column(name = "NAME")
    private String name = "";

    @Column(name = "DESCRIPTION")
    private String description = "";
    @Transient
    private Integer assemblasCount;
    @Transient
    private Integer partsCount;
    @Transient
    private Integer drawingsCount;
    @Transient
    private Integer commitsCount;

    public PDMObject(Enum type) {
        super.setObjectType(type);
    }
}
