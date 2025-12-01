package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWTRANSITION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowTransition extends CassiniObject {

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "FROM_STATUS", nullable = false)
    private Integer fromStatus;

    @Column(name = "TO_STATUS", nullable = false)
    private Integer toStatus;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Transient
    private String fromStatusDiagramId;

    @Transient
    private String toStatusDiagramId;

    public PLMWorkflowTransition() {
        super(PLMObjectType.PLMWORKFLOWTRANSITION);
    }


}
