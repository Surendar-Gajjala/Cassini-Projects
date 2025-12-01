package com.cassinisys.plm.model.req;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDELIVERABLE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementDeliverable implements Serializable {

    @Id
    @SequenceGenerator(name = "REQUIREMENT_DELIVERABLE_ID_GEN", sequenceName = "REQUIREMENT_DELIVERABLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENT_DELIVERABLE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUIREMENT")
    private PLMRequirement requirement;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private PLMItemRevision itemRevision;


}

