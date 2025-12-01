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
@Table(name = "PGC_DECLARATION_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCDeclarationType extends PGCObjectType {

    @Transient
    private List<PGCDeclarationType> childrens = new ArrayList<>();

    public PGCDeclarationType() {
        super.setObjectType(PGCEnumObject.PGCDECLARATIONTYPE);
    }


    @Transient
    @JsonIgnore
    public PGCDeclarationType getChildTypeByPath(String path) {
        PGCDeclarationType substanceType = null;

        Map<String, PGCDeclarationType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PGCDeclarationType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PGCDeclarationType childType = childrenMap.get(name);
            if(childType != null) {
                substanceType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            substanceType = childrenMap.get(name);
        }
        return substanceType;
    }
}
