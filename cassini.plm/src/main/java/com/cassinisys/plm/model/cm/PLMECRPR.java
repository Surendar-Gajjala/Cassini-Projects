package com.cassinisys.plm.model.cm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 30-12-2020.
 */
@Entity
@Data
@Table(name = "PLM_ECR_PR")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMECRPR implements Serializable {

    @Id
    @SequenceGenerator(name = "ECR_PR_ID_GEN", sequenceName = "ECR_PR_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECR_PR_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ECR")
    private Integer ecr;

    @Column(name = "PROBLEM_REPORT")
    private Integer problemReport;
}
