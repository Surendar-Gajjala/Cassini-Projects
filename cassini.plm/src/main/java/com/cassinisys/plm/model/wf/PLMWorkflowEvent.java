package com.cassinisys.plm.model.wf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 24-06-2021.
 */
@Entity
@Data
@Table(name = "PLM_WORKFLOW_EVENT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowEvent implements Serializable {

    @Id
    @SequenceGenerator(name = "WORKFLOWEVENT_ID_GEN", sequenceName = "WORKFLOWEVENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWEVENT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "EVENT_TYPE")
    private String eventType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACTIVITY")
    private PLMWorkflowStatus activity;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "ACTION_DATA")
    private String actionData;

}
