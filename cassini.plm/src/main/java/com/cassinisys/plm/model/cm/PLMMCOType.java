package com.cassinisys.plm.model.cm;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_MCO_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMMCOType")
public class PLMMCOType extends PLMChangeType {

    @Transient
    @JsonProperty("@type")
    private final String type = "PLMMCOType";

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.MCOType")})
    @Column(name = "MCO_TYPE", nullable = true)
    private MCOType mcoType;

    @Transient
    public List<PLMMCOType> childrens = new ArrayList<>();

    public PLMMCOType() {
        setObjectType(PLMObjectType.MCOTYPE);
    }

    @Transient
    @JsonIgnore
    public PLMMCOType getChildTypeByPath(String path) {
        PLMMCOType ecoType = null;

        Map<String, PLMMCOType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PLMMCOType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMMCOType childType = childrenMap.get(name);
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
