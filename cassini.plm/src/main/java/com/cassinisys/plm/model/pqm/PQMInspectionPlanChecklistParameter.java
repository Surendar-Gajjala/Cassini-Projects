package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.DataType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 10-06-2020.
 */
@Entity
@Data
@Table(name = "PQM_INSPECTION_PLAN_CHECKLIST_PARAMETER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionPlanChecklistParameter implements Serializable {

    @Id
    @SequenceGenerator(name = "PQM_OBJECT_ID_GEN", sequenceName = "PQM_OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PQM_OBJECT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "INSPECTION_PLAN")
    private Integer inspectionPlan;

    @Column(name = "CHECKLIST")
    private Integer checklist;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.DataType")})
    @Column(name = "EXPECTED_VALUE_TYPE")
    private DataType expectedValueType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXPECTED_VALUE")
    private PQMParamValue expectedValue;

    @Column(name = "PASS_CRITERIA")
    private String passCriteria;

    @Column(name = "UNITS")
    private String units;


}
