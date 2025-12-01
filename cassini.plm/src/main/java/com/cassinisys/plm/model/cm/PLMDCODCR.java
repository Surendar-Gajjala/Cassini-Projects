package com.cassinisys.plm.model.cm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_DCO_DCR")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMDCODCR implements Serializable {

    @Id
    @SequenceGenerator(name = "ECO_ECR_ID_GEN", sequenceName = "ECO_ECR_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECO_ECR_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DCO")
    private Integer dco;

    @Column(name = "DCR")
    private Integer dcr;


}
