package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@Table(name = "PQM_INSPECTION_PLAN_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionPlanHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "PLAN_REVISION_HISTORY_ID_GEN", sequenceName = "PLAN_REVISION_HISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLAN_REVISION_HISTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "INSPECTION_PLAN")
    private Integer inspectionPlan;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "OLD_STATUS")
    private PLMLifeCyclePhase oldStatus;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NEW_STATUS")
    private PLMLifeCyclePhase newStatus;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp = new Date();

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;


}
