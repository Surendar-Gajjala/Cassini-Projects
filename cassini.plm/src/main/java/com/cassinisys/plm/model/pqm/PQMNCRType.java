package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.Lov;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Table(name = "PQM_NCR_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMNCRType extends PQMQualityType {

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "FAILURE_TYPES", nullable = true)
    private Lov failureTypes;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "SEVERITIES", nullable = true)
    private Lov severities;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DISPOSITIONS", nullable = true)
    private Lov dispositions;

    @Transient
    private List<PQMNCRType> childrens = new ArrayList<>();

    public PQMNCRType() {
        super.setQualityType(QualityType.NCRTYPE);
    }

    @Transient
    @JsonIgnore
    public PQMNCRType getChildTypeByPath(String path) {
        PQMNCRType qualityType = null;

        Map<String, PQMNCRType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PQMNCRType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMNCRType childType = childrenMap.get(name);
            if (childType != null) {
                qualityType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        } else {
            name = path;
            qualityType = childrenMap.get(name);
        }
        return qualityType;
    }


}
