package com.cassinisys.plm.model.pgc;

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
 * Created by GSR on 21-11-2020.
 */
@Entity
@Table(name = "PGC_SPECIFICATION_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCSpecificationType extends PGCObjectType {

    @Transient
    private List<PGCSpecificationType> childrens = new ArrayList<>();

    public PGCSpecificationType() {
        super.setObjectType(PGCEnumObject.PGCSPECIFICATIONTYPE);
    }

    @Transient
    @JsonIgnore
    public PGCSpecificationType getChildTypeByPath(String path) {
        PGCSpecificationType specificationType = null;

        Map<String, PGCSpecificationType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PGCSpecificationType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PGCSpecificationType childType = childrenMap.get(name);
            if(childType != null) {
                specificationType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            specificationType = childrenMap.get(name);
        }
        return specificationType;
    }
}
