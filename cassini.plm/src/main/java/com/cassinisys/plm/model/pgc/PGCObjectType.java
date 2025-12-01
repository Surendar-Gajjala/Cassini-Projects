package com.cassinisys.plm.model.pgc;

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
 * Created by CassiniSystems on 24-11-2020.
 */
@Entity
@Table(name = "PGC_OBJECT_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCObjectType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "AUTONUMBER_SOURCE")
    private AutoNumber autoNumberSource;

    @Transient
    private List<PGCObjectType> children = new ArrayList<>();

    public PGCObjectType() {
        super(PLMObjectType.PGCOBJECTTYPE);
    }

    @Transient
    private List<PGCObjectTypeAttribute> attributes = new ArrayList<>();
}
