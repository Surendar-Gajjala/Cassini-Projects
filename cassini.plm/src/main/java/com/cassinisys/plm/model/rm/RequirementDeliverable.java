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
@Table(name = "RM_REQUIREMENTDELIVERABLE")
public class RequirementDeliverable implements Serializable {
    @Id
    @SequenceGenerator(name = "REQUIREMENTDELIVERABLE_ID_GEN", sequenceName = "REQUIREMENTDELIVERABLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTDELIVERABLE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUIREMENT")
    private RmObject requirement;


}
