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
 * Created by Lenovo on 23-09-2020.
 */
@Entity
@Table(name = "MES_OPERATION_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class MESOperationType extends MESObjectType {

    public MESOperationType() {
        super.setObjectType(MESType.OPERATIONTYPE);
    }


    @Transient
    private List<MESOperationType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public MESOperationType getChildTypeByPath(String path) {
        MESOperationType itemType = null;

        Map<String, MESOperationType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MESOperationType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MESOperationType childType = childrenMap.get(name);
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
