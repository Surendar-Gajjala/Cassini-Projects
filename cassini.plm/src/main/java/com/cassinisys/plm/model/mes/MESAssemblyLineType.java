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
 * Created by CassiniSystems on 17-09-2020.
 */
@Entity
@Table(name = "MES_ASSEMBLYLINE_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESAssemblyLineType extends MESObjectType {

    @Transient
    private List<MESAssemblyLineType> childrens = new ArrayList<>();

    public MESAssemblyLineType() {
        super.setObjectType(MESType.ASSEMBLYLINETYPE);
    }

    @Transient
    @JsonIgnore
    public MESAssemblyLineType getChildTypeByPath(String path) {
        MESAssemblyLineType assemblyLine = null;

        Map<String, MESAssemblyLineType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MESAssemblyLineType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MESAssemblyLineType childType = childrenMap.get(name);
            if(childType != null) {
                assemblyLine = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            assemblyLine = childrenMap.get(name);
        }
        return assemblyLine;
    }

}
