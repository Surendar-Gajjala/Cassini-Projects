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
 * Created by Lenovo on 19-09-2020.
 */
@Entity
@Table(name = "MES_TOOL_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class MESToolType extends MESObjectType {

    public MESToolType() {
        super.setObjectType(MESType.TOOLTYPE);
    }

    @Transient
    private List<MESToolType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public MESToolType getChildTypeByPath(String path) {
        MESToolType itemType = null;

        Map<String, MESToolType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MESToolType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MESToolType childType = childrenMap.get(name);
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
