package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "MRO_WORKORDER_INSTRUCTIONS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MROWorkOrderInstructions implements Serializable {
    @Id
    @SequenceGenerator(name = "WORKORDER_ID_GEN", sequenceName = "WORKORDER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKORDER_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "WORKORDER")
    private Integer workOrder;

    @Column(name = "INSTRUCTIONS")
    private String instructions;
}
