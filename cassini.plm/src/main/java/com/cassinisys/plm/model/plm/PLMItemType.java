package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
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

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_ITEMTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ITEMNUMBER_SOURCE", nullable = true)
    private AutoNumber itemNumberSource;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @Column(name = "EXCLRULES")
    private String excluRules;

    @Column(name = "TABS")
    @Type(
            type = "com.cassinisys.platform.util.converter.StringArrayType"
    )
    private String[] tabs;

    @Column(name = "REQUIRED_ECO")
    private Boolean requiredEco = Boolean.TRUE;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.ItemClass")})
    @Column(name = "ITEM_CLASS", nullable = true)
    private ItemClass itemClass;

    @Column(name = "IS_SOFTWARE_TYPE")
    private Boolean softwareType = Boolean.FALSE;

    @Transient
    private Integer level;

    @Transient
    private List<PLMItemType> children = new ArrayList<>();

    @Transient
    private List<PLMItemTypeAttribute> attributes = new ArrayList<>();

    @Transient
    private PLMItemType parentTypeReference;

    public PLMItemType() {
        super(PLMObjectType.ITEMTYPE);
    }


    @Transient
    @JsonIgnore
    public PLMItemType getChildTypeByPath(String path) {
        PLMItemType itemType = null;

        Map<String, PLMItemType> childrenMap = children.stream()
                .collect(Collectors.toMap(PLMItemType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PLMItemType childType = childrenMap.get(name);
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
