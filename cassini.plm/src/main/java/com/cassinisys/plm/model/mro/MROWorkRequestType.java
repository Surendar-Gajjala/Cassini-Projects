package com.cassinisys.plm.model.mro;

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
 * Created by GSR on 17-11-2020.
 */
@Entity
@Table(name = "MRO_WORKREQUEST_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROWorkRequestType extends MROObjectType {

    public MROWorkRequestType() {
        super.setObjectType(MROEnumObject.WORKREQUESTTYPE);
    }

    @Transient
    private List<MROWorkRequestType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public MROWorkRequestType getChildTypeByPath(String path) {
        MROWorkRequestType itemType = null;

        Map<String, MROWorkRequestType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MROWorkRequestType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MROWorkRequestType childType = childrenMap.get(name);
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