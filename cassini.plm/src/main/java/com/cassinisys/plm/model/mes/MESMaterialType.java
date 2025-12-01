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
@Table(name = "MES_MATERIAL_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class MESMaterialType extends MESObjectType {

    public MESMaterialType() {
        super.setObjectType(MESType.MATERIALTYPE);
    }
    @Transient
    private List<MESMaterialType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public MESMaterialType getChildTypeByPath(String path) {
        MESMaterialType itemType = null;

        Map<String, MESMaterialType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MESMaterialType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MESMaterialType childType = childrenMap.get(name);
            if(childType != null) {
                itemType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            itemType = childrenMap.get(name);
        }
        return itemType;
    }


}
