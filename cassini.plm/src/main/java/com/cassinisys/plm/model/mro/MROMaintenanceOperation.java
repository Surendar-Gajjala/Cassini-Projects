package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_MAINTENANCE_OPERATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class MROMaintenanceOperation implements Serializable {

    @Id
    @SequenceGenerator(name = "MAINTENANCEOPERATION_ID_GEN", sequenceName = "MAINTENANCECHECKLISTITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAINTENANCEOPERATION_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MAINTENANCE_PLAN")
    private Integer maintenancePlan;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARAM_NAME")
    private String paramName;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.OperationParamValueType")})
    @Column(name = "PARAM_TYPE", nullable = true)
    private OperationParamValueType paramType = OperationParamValueType.NONE;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "LOV", nullable = true)
    private Lov lov;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate = new Date();
}
