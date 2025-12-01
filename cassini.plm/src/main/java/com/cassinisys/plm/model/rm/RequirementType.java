package com.cassinisys.plm.model.rm;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_REQUIREMENTTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequirementType extends RmObjectType {

    @Column(name = "PARENTTYPE")
    private Integer parentType;

    @Transient
    private List<RequirementType> children = new ArrayList<>();

    @Transient
    private RequirementType parentTypeReference;

    public RequirementType() {
        super(PLMObjectType.REQUIREMENTTYPE);
    }


}
