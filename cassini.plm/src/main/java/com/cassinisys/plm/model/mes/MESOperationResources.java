package com.cassinisys.plm.model.mes;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MES_OPERATION_RESOURCES")
@Data
public class MESOperationResources implements Serializable {

    @Id
    @SequenceGenerator(name = "OPERATIONRESOURCES_ID_GEN", sequenceName = "OPERATIONRESOURCES_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATIONRESOURCES_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "OPERATION")
    private Integer operation;

    @Column(name = "RESOURCE")
    private String resource;

    @Column(name = "RESOURCE_TYPE")
    private Integer resourceType;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private String resourceTypeName;
    @Transient
    private Integer consumedQty = 0;
}
