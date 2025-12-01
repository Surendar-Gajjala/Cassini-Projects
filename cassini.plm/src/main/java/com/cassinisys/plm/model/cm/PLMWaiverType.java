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
@Table(name = "PLM_WAIVER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMWaiverType")
public class PLMWaiverType extends PLMChangeType {
    @Transient
    @JsonProperty("@type")
    private final String type = "PLMWaiverType";

    @Transient
    private List<PLMWaiverType> childrens = new ArrayList<>();

    public PLMWaiverType() {
        setObjectType(PLMObjectType.WAIVERTYPE);
    }

    @Transient
    @JsonIgnore
    public PLMWaiverType getChildTypeByPath(String path) {
        PLMWaiverType ecoType = null;

        Map<String, PLMWaiverType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PLMWaiverType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMWaiverType childType = childrenMap.get(name);
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
