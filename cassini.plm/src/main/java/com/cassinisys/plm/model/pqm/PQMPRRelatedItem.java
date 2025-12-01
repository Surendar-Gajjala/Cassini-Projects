package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_PR_RELATED_ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMPRRelatedItem extends CassiniObject {

    @Column(name = "PROBLEM_REPORT")
    private Integer problemReport;

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "NOTES")
    private String notes;

    public PQMPRRelatedItem() {
        super.setObjectType(PLMObjectType.PRPROBLEMITEM);
    }


}
