package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION_CHECKLIST")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionChecklist extends CassiniObject {

    @Column(name = "INSPECTION")
    private Integer inspection;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLAN_CHECKLIST")
    private PQMInspectionPlanChecklist planChecklist;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.ChecklistStatus")})
    @Column(name = "STATUS", nullable = true)
    private ChecklistStatus status = ChecklistStatus.PENDING;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.ChecklistResult")})
    @Column(name = "RESULT", nullable = true)
    private ChecklistResult result = ChecklistResult.NONE;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private Integer paramsCount = 0;
    @Transient
    private Integer attachmentCount = 0;

    @Transient
    private List<PQMInspectionChecklist> children = new ArrayList<>();

    public PQMInspectionChecklist() {
        super.setObjectType(PLMObjectType.INSPECTIONCHECKLIST);
    }


}
