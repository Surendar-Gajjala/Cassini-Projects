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
@Table(name = "PLM_DEVIATION_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMDeviationType")
public class PLMDeviationType extends PLMChangeType {
    @Transient
    @JsonProperty("@type")
    private final String type = "PLMDeviationType";

    @Transient
    private List<PLMDeviationType> childrens = new ArrayList<>();

    public PLMDeviationType() {
        setObjectType(PLMObjectType.DEVIATIONTYPE);
    }

    @Transient
    @JsonIgnore
    public PLMDeviationType getChildTypeByPath(String path) {
        PLMDeviationType ecoType = null;

        Map<String, PLMDeviationType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PLMDeviationType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMDeviationType childType = childrenMap.get(name);
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
