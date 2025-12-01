package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.AutoNumber;
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
 * Created by Suresh Cassini on 25-11-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_SUPPLIER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSupplierType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "AUTONUMBER_SOURCE", nullable = true)
    private AutoNumber autoNumberSource;

    @Transient
    private List<PLMSupplierType> children = new ArrayList<>();

    @Transient
    private List<PLMSupplierTypeAttribute> attributes = new ArrayList<>();

    @Transient
    private PLMSupplierType parentTypeReference;

    public PLMSupplierType() {
        super(PLMObjectType.SUPPLIERTYPE);
    }
    @Transient
    @JsonIgnore
    public PLMSupplierType getChildTypeByPath(String path) {
        PLMSupplierType supplierType = null;

        Map<String, PLMSupplierType> childrenMap = children.stream()
                .collect(Collectors.toMap(PLMSupplierType::getName, Function.identity()));

        int index = path.indexOf('/');
        String name;
        if(index != -1) {
            name = path.substring(0, index);
            PLMSupplierType childType = childrenMap.get(name);
            if(childType != null) {
                supplierType = childType.getChildTypeByPath(path.substring(index + 1));
            }
        }
        else {
            name = path;
            supplierType = childrenMap.get(name);
        }
        return supplierType;
    }

}
