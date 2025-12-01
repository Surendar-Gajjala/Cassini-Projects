package com.cassinisys.plm.model.req;

import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTOBJECTRELATEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementObjectRelatedItem implements Serializable {

    @Id
    @SequenceGenerator(name = "REQUIREMENTOBJECTRELATEDITEM_ID_GEN", sequenceName = "REQUIREMENTOBJECTRELATEDITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTOBJECTRELATEDITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MES_OBJECT_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    private ObjectType objectType;

    @Column(name = "OBJECT_ID")
    private Integer objectId;
}
