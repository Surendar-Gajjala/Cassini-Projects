package com.cassinisys.plm.model.rm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 24-10-2018.
 */

@Entity
@Data
@Table(name = "RM_SPECIFICATIONDELIVERABLE")
public class SpecificationDeliverable implements Serializable {

    @Id
    @SequenceGenerator(name = "SPECIFICATIONDELIVERABLE_ID_GEN", sequenceName = "SPECIFICATIONDELIVERABLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPECIFICATIONDELIVERABLE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECIFICATION")
    private RmObject specification;

   }
