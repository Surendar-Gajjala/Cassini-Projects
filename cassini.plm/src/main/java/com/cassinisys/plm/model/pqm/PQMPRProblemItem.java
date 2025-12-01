package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_PR_PROBLEM_ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMPRProblemItem extends CassiniObject {

    @Column(name = "PROBLEM_REPORT")
    private Integer problemReport;

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_IMPLEMENTED")
    private Boolean isImplemented = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMPLEMENTED_DATE")
    private Date implementedDate;

    public PQMPRProblemItem() {
        super.setObjectType(PLMObjectType.PRPROBLEMITEM);
    }


}
