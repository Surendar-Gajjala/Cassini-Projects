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
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_SPAREPART_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROSparePartType extends MROObjectType {

    public MROSparePartType() {
        super.setObjectType(MROEnumObject.SPAREPARTTYPE);
    }

    @Transient
    private List<MROSparePartType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public MROSparePartType getChildTypeByPath(String path) {
        MROSparePartType sparePartType = null;

        Map<String, MROSparePartType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MROSparePartType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MROSparePartType childType = childrenMap.get(name);
            if(childType != null) {
                sparePartType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            sparePartType = childrenMap.get(name);
        }
        return sparePartType;
    }
}
