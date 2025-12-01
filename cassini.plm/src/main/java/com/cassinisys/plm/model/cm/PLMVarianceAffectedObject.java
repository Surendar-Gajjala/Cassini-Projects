package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.model.core.CassiniObject;
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
@Table(name = "PLM_VARIANCE_AFFECTED_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class PLMVarianceAffectedObject extends CassiniObject {

    @Column(name = "VARIANCE")
    private Integer variance;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "SERIALS_OR_LOTS")
    private String serialsOrLots;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_RECURRING")
    private Boolean isRecurring = Boolean.FALSE;
}
