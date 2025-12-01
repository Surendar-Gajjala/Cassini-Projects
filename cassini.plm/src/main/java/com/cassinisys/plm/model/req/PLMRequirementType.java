package com.cassinisys.plm.model.req;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementType extends PLMRequirementObjectType {

    @Column(name = "PARENT")
    private Integer parent;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PRIORITY_LIST", nullable = true)
    private Lov priorityList;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @Column(name = "TABS")
    @Type(
            type = "com.cassinisys.platform.util.converter.StringArrayType"
    )
    private String[] tabs;

    public PLMRequirementType() {
        super.setObjectType(PLMObjectType.REQUIREMENTTYPE);
    }


    @Transient
    private List<PLMRequirementType> childrens = new ArrayList<>();


    @Transient
    @JsonIgnore
    public PLMRequirementType getChildTypeByPath(String path) {
        PLMRequirementType requirementType = null;

        Map<String, PLMRequirementType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PLMRequirementType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PLMRequirementType childType = childrenMap.get(name);
            if(childType != null) {
                requirementType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            requirementType = childrenMap.get(name);
        }
        return requirementType;
    }
}
