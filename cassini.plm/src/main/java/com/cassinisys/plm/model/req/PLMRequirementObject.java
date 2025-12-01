package com.cassinisys.plm.model.req;

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
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTOBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementObject extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<PLMRequirementObjectAttribute> requirementObjectAttributes = new ArrayList<>();

    @Transient
    private List<ObjectAttribute> objectAttributes = new ArrayList<>();

    public PLMRequirementObject() {
        super(PLMObjectType.REQUIREMENT);
    }
}

