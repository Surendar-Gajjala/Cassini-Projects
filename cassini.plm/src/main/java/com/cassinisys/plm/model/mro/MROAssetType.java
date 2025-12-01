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
 * Created by Suresh Cassini on 21-11-2020.
 */
@Entity
@Table(name = "MRO_ASSET_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROAssetType extends MROObjectType {

    @Transient
    private List<MROAssetType> childrens = new ArrayList<>();

    public MROAssetType() {
        super.setObjectType(MROEnumObject.ASSETTYPE);
    }

    @Transient
    @JsonIgnore
    public MROAssetType getChildTypeByPath(String path) {
        MROAssetType assetTypeType = null;

        Map<String, MROAssetType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MROAssetType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MROAssetType childType = childrenMap.get(name);
            if(childType != null) {
                assetTypeType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            assetTypeType = childrenMap.get(name);
        }
        return assetTypeType;
    }
}
