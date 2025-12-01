package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.plm.PLMItemType;
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
@Table(name = "PQM_PRODUCT_INSPECTION_PLAN_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMProductInspectionPlanType extends PQMInspectionPlanType {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_TYPE")
    private PLMItemType productType;

    @Transient
    private List<PQMProductInspectionPlanType> childrens = new ArrayList<>();

    public PQMProductInspectionPlanType() {
        super.setQualityType(QualityType.PRODUCTINSPECTIONPLANTYPE);
    }

    @Transient
    @JsonIgnore
    public PQMProductInspectionPlanType getChildTypeByPath(String path) {
        PQMProductInspectionPlanType qualityType = null;

        Map<String, PQMProductInspectionPlanType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PQMProductInspectionPlanType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMProductInspectionPlanType childType = childrenMap.get(name);
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
