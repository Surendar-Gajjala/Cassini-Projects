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
 * Created by GSR Cassini on 21-11-2020.
 */
@Entity
@Table(name = "MRO_METER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROMeterType extends MROObjectType {

    @Transient
    private List<MROMeterType> childrens = new ArrayList<>();

    public MROMeterType() {
        super.setObjectType(MROEnumObject.METERTYPE);
    }

    @Transient
    @JsonIgnore
    public MROMeterType getChildTypeByPath(String path) {
        MROMeterType meterTypeType = null;

        Map<String, MROMeterType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MROMeterType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MROMeterType childType = childrenMap.get(name);
            if(childType != null) {
                meterTypeType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            meterTypeType = childrenMap.get(name);
        }
        return meterTypeType;
    }
}
