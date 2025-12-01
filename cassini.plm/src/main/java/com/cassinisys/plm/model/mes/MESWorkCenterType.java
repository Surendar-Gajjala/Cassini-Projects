package com.cassinisys.plm.model.mes;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Hello on 9/18/2020.
 */
@Entity
@Table(name = "MES_WORKCENTER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class MESWorkCenterType extends MESObjectType {

    public MESWorkCenterType() {
        super.setObjectType(MESType.WORKCENTERTYPE);
    }

    @Transient
    private List<MESWorkCenterType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public MESWorkCenterType getChildTypeByPath(String path) {
        MESWorkCenterType wcType = null;

        Map<String, MESWorkCenterType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MESWorkCenterType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MESWorkCenterType childType = childrenMap.get(name);
            if(childType != null) {
                wcType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            wcType = childrenMap.get(name);
        }
        return wcType;
    }
}
