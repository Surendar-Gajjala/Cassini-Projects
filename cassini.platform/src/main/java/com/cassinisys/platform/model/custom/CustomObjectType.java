package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectType;
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

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CUSTOM_OBJECT_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ABSTRACT")
    private Boolean isAbstract = Boolean.FALSE;

    @Column(name = "PARENT")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NUMBER_SOURCE", nullable = true)
    private AutoNumber numberSource;

    @Column(name = "IS_REVISIONED")
    private Boolean isRevisioned = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @Column(name = "HAS_LIFECYCLE")
    private Boolean hasLifecycle = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private Lov lifecycle;

    @Column(name = "TABS")
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] tabs;

    @Column(name = "SHOW_IN_NAVIGATION")
    private Boolean showInNavigation = Boolean.TRUE;

    @Transient
    private List<CustomObjectType> children = new ArrayList<>();

    public CustomObjectType() {
        super.setObjectType(ObjectType.CUSTOMOBJECTTYPE);
    }

    @Transient
    @JsonIgnore
    public CustomObjectType getChildTypeByPath(String path) {
        CustomObjectType itemType = null;

        Map<String, CustomObjectType> childrenMap = children.stream()
                .collect(Collectors.toMap(CustomObjectType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            CustomObjectType childType = childrenMap.get(name);
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
