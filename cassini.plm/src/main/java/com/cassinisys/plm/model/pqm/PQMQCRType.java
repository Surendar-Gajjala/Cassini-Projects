package com.cassinisys.plm.model.pqm;

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
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_QCR_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMQCRType extends PQMQualityType {

    @Transient
    private List<PQMQCRType> childrens = new ArrayList<>();

    public PQMQCRType() {
        super.setQualityType(QualityType.QCRTYPE);
    }

    @Transient
    @JsonIgnore
    public PQMQCRType getChildTypeByPath(String path) {
        PQMQCRType qualityType = null;

        Map<String, PQMQCRType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PQMQCRType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMQCRType childType = childrenMap.get(name);
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
