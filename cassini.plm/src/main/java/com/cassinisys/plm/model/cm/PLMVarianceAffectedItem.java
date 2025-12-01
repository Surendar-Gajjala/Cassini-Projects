package com.cassinisys.plm.model.cm;

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
@Table(name = "PLM_VARIANCE_AFFECTED_ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PLMVarianceAffectedItem extends PLMVarianceAffectedObject {

    @Column(name = "ITEM")
    private Integer item;

    public PLMVarianceAffectedItem() {
        super.setObjectType(PLMObjectType.VARIANCEAFFECTEDITEM);
    }

}
