package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
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
@Table(name = "PQM_MATERIAL_INSPECTION_PLAN_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMMaterialInspectionPlanType extends PQMInspectionPlanType {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART_TYPE")
    private PLMManufacturerPartType partType;

    @Transient
    private List<PQMMaterialInspectionPlanType> childrens = new ArrayList<>();

    public PQMMaterialInspectionPlanType() {
        super.setQualityType(QualityType.MATERIALINSPECTIONPLANTYPE);
    }

    @Transient
    @JsonIgnore
    public PQMMaterialInspectionPlanType getChildTypeByPath(String path) {
        PQMMaterialInspectionPlanType qualityType = null;

        Map<String, PQMMaterialInspectionPlanType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PQMMaterialInspectionPlanType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMMaterialInspectionPlanType childType = childrenMap.get(name);
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
