package com.cassinisys.plm.model.cm;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
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
@Table(name = "PLM_ECR_AFFECTEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMECRAffectedItem extends PLMChangeRequestAffectedItem {

    @Column(name = "ECR")
    private Integer ecr;

    @Column(name = "PROBLEM_REPORTS")
    @Type(type = "com.cassinisys.platform.util.converter.IntArrayUserType")
    private Integer[] problemReports = new Integer[0];

    @Transient
    private List<PQMProblemReport> problemReportList = new ArrayList<>();

    public PLMECRAffectedItem() {
        super.setObjectType(PLMObjectType.ECRAFFECTEDITEM);
    }

}
