package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.pqm.dto.NCRsDto;
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
@Table(name = "PQM_QCR_AGGREGATE_NCR")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMQCRAggregateNCR implements Serializable {

    @Id
    @SequenceGenerator(name = "PLAN_REVISION_HISTORY_ID_GEN", sequenceName = "PLAN_REVISION_HISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLAN_REVISION_HISTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "QCR")
    private Integer qcr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NCR")
    private PQMNCR ncr;

    @Transient
    private NCRsDto ncrDto;




}
