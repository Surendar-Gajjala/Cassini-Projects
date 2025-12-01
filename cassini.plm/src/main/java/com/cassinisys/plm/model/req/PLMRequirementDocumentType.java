package com.cassinisys.plm.model.req;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENTTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementDocumentType extends PLMRequirementObjectType {

    @Column(name = "PARENT")
    private Integer parent;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @Column(name = "TABS")
    @Type(
            type = "com.cassinisys.platform.util.converter.StringArrayType"
    )
    private String[] tabs;

    public PLMRequirementDocumentType() {
        super.setObjectType(PLMObjectType.REQUIREMENTDOCUMENTTYPE);
    }

    @Transient
    private List<PLMRequirementDocumentType> childrens = new ArrayList<>();

    @Transient
    @JsonIgnore
    public PLMRequirementDocumentType getChildTypeByPath(String path) {
        PLMRequirementDocumentType reqDocType = null;

        Map<String, PLMRequirementDocumentType> childrenMap = childrens.stream()
                .collect(Collectors.toMap(PLMRequirementDocumentType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PLMRequirementDocumentType childType = childrenMap.get(name);
            if(childType != null) {
                reqDocType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            reqDocType = childrenMap.get(name);
        }
        return reqDocType;
    }

}
