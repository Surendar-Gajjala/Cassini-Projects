package com.cassinisys.plm.model.cm;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_DCR_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMDCRType")
public class PLMDCRType extends PLMChangeType {
    @Transient
    @JsonProperty("@type")
    private final String type = "PLMDCRType";

    public PLMDCRType() { setObjectType(PLMObjectType.DCRTYPE);}

    @Transient
    public List<PLMDCRType> childrens = new ArrayList<>();
    @Transient
    @JsonIgnore
    public PLMDCRType getChildTypeByPath(String path) {
        PLMDCRType ecoType = null;

        Map<String, PLMDCRType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PLMDCRType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMDCRType childType = childrenMap.get(name);
            if (childType != null) {
                ecoType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        } else {
            name = path;
            ecoType = childrenMap.get(name);
        }
        return ecoType;
    }


}
