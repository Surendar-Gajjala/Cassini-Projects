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
@Table(name = "PLM_ECO_ECR")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMECOECR implements Serializable {

    @Id
    @SequenceGenerator(name = "ECO_ECR_ID_GEN", sequenceName = "ECO_ECR_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECO_ECR_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ECO")
    private Integer eco;

    @Column(name = "ECR")
    private Integer ecr;

}
