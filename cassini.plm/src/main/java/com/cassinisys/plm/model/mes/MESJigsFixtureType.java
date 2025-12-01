package com.cassinisys.plm.model.mes;

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
 * Created by CassiniSystems on 17-09-2020.
 */
@Entity
@Table(name = "MES_JIGS_FIXTURES_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESJigsFixtureType extends MESObjectType {
    public MESJigsFixtureType() {
        super.setObjectType(MESType.JIGFIXTURETYPE);
    }

    @Transient
    private List<MESJigsFixtureType> childrens = new ArrayList<>();


    @Transient
    @JsonIgnore
    public MESJigsFixtureType getChildTypeByPath(String path) {
        MESJigsFixtureType itemType = null;

        Map<String, MESJigsFixtureType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(MESJigsFixtureType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            MESJigsFixtureType childType = childrenMap.get(name);
            if(childType != null) {
                itemType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            itemType = childrenMap.get(name);
        }
        return itemType;
    }


}
