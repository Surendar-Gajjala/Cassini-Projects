package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION_PLAN_CHECKLIST")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionPlanChecklist extends CassiniObject {

    @Column(name = "INSPECTION_PLAN")
    private Integer inspectionPlan;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "PROCEDURE")
    private String procedure;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SEQ")
    private Integer seq;

    @Column(name = "PARENT")
    private Integer parent;

    @Transient
    private List<PQMInspectionPlanChecklist> children = new LinkedList<>();

    @Transient
    private Integer paramsCount = 0;
    @Transient
    private Integer attachmentCount = 0;

    public PQMInspectionPlanChecklist() {
        super.setObjectType(PLMObjectType.INSPECTIONPLANCHECKLIST);
    }

}
