package com.cassinisys.plm.model.wf;

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
 * Created by reddy on 5/30/17.
 */
@Entity
@Data
@Table(name = "PLM_WORKFLOWSTATUSACTIONHISTORY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowStatusActionHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "WORKFLOWSTATUSASSIGNMENT_ID_GEN", sequenceName = "WORKFLOWSTATUSASSIGNMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWSTATUSASSIGNMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "ASSIGNMENT")
    private Integer assignment;

    @Column(name = "ACTION")
    private String action;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;


}
