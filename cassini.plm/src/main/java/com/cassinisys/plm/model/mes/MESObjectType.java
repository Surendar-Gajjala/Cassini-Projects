package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Entity
@Table(name = "MES_OBJECT_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class MESObjectType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "AUTONUMBER_SOURCE")
    private AutoNumber autoNumberSource;

    public MESObjectType() {
        super(PLMObjectType.MESOBJECTTYPE);
    }

    @Transient
    private List<MESObjectType> children = new ArrayList<>();

    @Transient
    private List<MESObjectTypeAttribute> attributes = new ArrayList<>();

}
