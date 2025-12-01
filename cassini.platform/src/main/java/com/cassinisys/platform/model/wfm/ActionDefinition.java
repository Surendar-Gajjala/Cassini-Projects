package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by lakshmi on 1/31/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ACTIONDEFINITION")
@PrimaryKeyJoinColumn(name = "ID")
public class ActionDefinition extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SOURCE")
    private Integer source;

    @Column(name = "TARGET")
    private Integer target;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Transient
    private ActivityDefinition sourceRef;

    @Transient
    private ActivityDefinition targetRef;

    @Transient
    private Integer instanceId;


    public ActionDefinition() {
        super(ObjectType.ACTIONDEFINITION);
    }



    public Action createInstance() {
        Action action = new Action();
        action.setId(null);
        action.setName(this.getName());
        action.setDiagramId(this.getDiagramId());
        return action;
    }
}
