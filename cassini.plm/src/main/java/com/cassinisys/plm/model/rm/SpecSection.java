package com.cassinisys.plm.model.rm;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_SPECSECTION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecSection extends SpecElement {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CANADDSECTION")
    private Boolean canAddSection = true;

    @Column(name = "CANADDREQUIREMENT")
    private Boolean canAddRequirement = true;
    @Transient
    private List<Integer> requirementVersions;

    @Transient
    private Integer requirementsEdit = 0;

    public SpecSection() {
        super(PLMObjectType.SPECSECTION);
        setType(SpecElementType.SECTION);
    }


}
