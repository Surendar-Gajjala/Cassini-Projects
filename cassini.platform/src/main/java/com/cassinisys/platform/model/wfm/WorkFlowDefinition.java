package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakshmi on 1/31/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "WORKFLOWDEFINITION")
@PrimaryKeyJoinColumn(name = "ID")
public class WorkFlowDefinition extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DIAGRAM")
    private String diagram;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Column(name = "ASSIGNABLE_TO", nullable = true)
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] assignableTo;

    @Transient
    private List<ActivityDefinition> activities = new ArrayList<>();

    @Transient
    private Integer instanceId;


    public WorkFlowDefinition() {
        super(ObjectType.WORKFLOWDEFINITION);
    }


    public WorkFlow createInstance() {
        WorkFlow workFlow = new WorkFlow();
        workFlow.setId(null);
        workFlow.setName(this.getName());
        workFlow.setDescription(this.getDescription());
        workFlow.setDefinition(this.getId());
        workFlow.setDiagramId(this.getDiagramId());
        workFlow.setDiagram(this.getDiagram());
        return workFlow;
    }
}
