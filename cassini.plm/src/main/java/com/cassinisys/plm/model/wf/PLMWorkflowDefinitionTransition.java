package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by GSR on 19-05-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWDEFINITIONTRANSITION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowDefinitionTransition extends CassiniObject {

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FROM_STATUS")
    private Integer fromStatus;

    @Column(name = "TO_STATUS")
    private Integer toStatus;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Transient
    private String fromStatusDiagramId;

    @Transient
    private String toStatusDiagramId;

    public PLMWorkflowDefinitionTransition() {
        super(PLMObjectType.PLMWORKFLOWDEFINITIONTRANSITION);
    }


}
