package com.cassinisys.plm.model.req;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
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
@Table(name = "PLM_REQUIREMENTOBJECTTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementObjectType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NUMBER_SOURCE")
    private AutoNumber autoNumberSource;

    public PLMRequirementObjectType() {
        super(PLMObjectType.REQUIREMENTTYPE);
    }

    @Transient
    private List<PLMRequirementObjectType> children = new ArrayList<>();

    @Transient
    private List<PLMRequirementObjectTypeAttribute> attributes = new ArrayList<>();


}
