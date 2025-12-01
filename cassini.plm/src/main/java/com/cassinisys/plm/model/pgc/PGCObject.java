package com.cassinisys.plm.model.pgc;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 17-11-2020.
 */
@Entity
@Table(name = "PGC_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCObject extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NUMBER")
    private String number;
    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<PGCObjectAttribute> pgcObjectAttributes = new ArrayList<>();

    @Transient
    private List<ObjectAttribute> objectAttributes = new ArrayList<>();

    public PGCObject() {
        super.setObjectType(PLMObjectType.PGCOBJECT);
    }

}
