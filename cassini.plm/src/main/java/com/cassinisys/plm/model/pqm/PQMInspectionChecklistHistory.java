package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION_CHECKLIST_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionChecklistHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "PLAN_REVISION_HISTORY_ID_GEN", sequenceName = "PLAN_REVISION_HISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLAN_REVISION_HISTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CHECKLIST")
    private Integer checklist;

    @Column(name = "OLD_STATUS")
    private String oldStatus;

    @Column(name = "NEW_STATUS")
    private String newStatus;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp = new Date();

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "QUALITY_ANALYST")
    private Integer qualityAnalyst;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "FAILURE_TYPE")
    private String failureType;

    @Column(name = "SEVERITY")
    private String severity;

    @Column(name = "DISPOSITION")
    private String disposition;

    @Column(name = "NOTES")
    private String notes;


}
