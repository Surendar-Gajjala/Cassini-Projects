package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
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
 * Created by reddy on 6/13/17.
 */
@Entity
@Table(name = "PLM_MANUFACTURERTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PLMManufacturerType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @Transient
    private List<PLMManufacturerType> children = new ArrayList<>();

    @Transient
    private List<PLMManufacturerTypeAttribute> attributes = new ArrayList<>();

    @Transient
    private PLMManufacturerType parentTypeReference;

    public PLMManufacturerType() {
        super(PLMObjectType.MANUFACTURERTYPE);
    }
    @Transient
    @JsonIgnore
    public PLMManufacturerType getChildTypeByPath(String path) {
        PLMManufacturerType mfrType = null;

        Map<String, PLMManufacturerType> childrenMap = children.stream()
                .collect(Collectors.toMap(PLMManufacturerType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PLMManufacturerType childType = childrenMap.get(name);
            if(childType != null) {
                mfrType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            mfrType = childrenMap.get(name);
        }
        return mfrType;
    }


}
