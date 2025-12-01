package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smukka on 29-03-2022.
 */
@Data
public class SupplierAuditDetailsDto {
    private Integer id;
    private Integer supplierAudit;
    private String number;
    private String name;
    private String type;
    private String description;
    private AuditPlanStatus status;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedDate;

    private Integer createdBy;
    private String preparedByName;
    private List<PQMSupplierAuditReviewer> approvers = new ArrayList<>();

}
