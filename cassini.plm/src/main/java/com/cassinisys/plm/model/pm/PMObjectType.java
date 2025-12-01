package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
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
@Table(name = "PLM_PMOBJECTTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PMObjectType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT")
    private Integer parent;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NUMBER_SOURCE")
    private AutoNumber autoNumberSource;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.PMType")})
    @Column(name = "TYPE")
    private PMType type;
    @Column(name = "TABS")
    @Type(
            type = "com.cassinisys.platform.util.converter.StringArrayType"
    )
    private String[] tabs;

    @Transient
    private Boolean typeObjectHasFiles = false;

    @Transient
    private List<PMObjectType> children = new ArrayList<>();

    public PMObjectType() {
        super(PLMObjectType.PMOBJECTTYPE);
    }

    @Transient
    @JsonIgnore
    public PMObjectType getChildTypeByPath(String path) {
        PMObjectType pmObjectType = null;

        Map<String, PMObjectType> childrenMap = children.stream()
                .collect(Collectors.toMap(PMObjectType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PMObjectType childType = childrenMap.get(name);
            if (childType != null) {
                pmObjectType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        } else {
            name = path;
            pmObjectType = childrenMap.get(name);
        }
        return pmObjectType;
    }
}

