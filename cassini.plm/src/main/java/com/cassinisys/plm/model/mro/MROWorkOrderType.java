package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by GSR on 17-11-2020.
 */
@Entity
@Table(name = "MRO_WORKORDER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROWorkOrderType extends MROObjectType {

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.WorkOrderType")})
    @Column(name = "TYPE")
    private WorkOrderType type;
    @Transient
    private List<MROWorkOrderType> childrens = new ArrayList<>();

    public MROWorkOrderType() {
        super.setObjectType(MROEnumObject.WORKORDERTYPE);
    }

    @Transient
    @JsonIgnore
    public MROWorkOrderType getChildTypeByPath(String path) {
        MROWorkOrderType itemType = null;

        Map<String, MROWorkOrderType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MROWorkOrderType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MROWorkOrderType childType = childrenMap.get(name);
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
