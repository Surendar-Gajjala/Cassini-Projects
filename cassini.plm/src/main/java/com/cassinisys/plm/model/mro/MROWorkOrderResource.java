package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Suresh Cassini on 10-12-2020.
 */
@Entity
@Data
@Table(name = "MRO_WORK_ORDER_RESOURCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class MROWorkOrderResource implements Serializable {

    @Id
    @SequenceGenerator(name = "WORKORDER_RESOURCE_ID_GEN", sequenceName = "WORKORDER_RESOURCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKORDER_RESOURCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORK_ORDER")
    private Integer workOrder;
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType")})
    @JsonDeserialize(
            using = ObjectTypeDeserializer.class
    )
    @Column(name = "RESOURCE_TYPE")
    private Enum resourceType;

    @Column(name = "RESOURCE_ID")
    private Integer resourceId;
    @Transient
    private Object resourceObject;
    @Transient
    private MROWorkOrder workOrderObject;
    @Transient
    private String resourceName;

}
