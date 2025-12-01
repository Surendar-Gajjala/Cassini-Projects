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
@Table(name = "PQM_ITEM_INSPECTION_RELATED_ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMItemInspectionRelatedItem extends CassiniObject {

    @Column(name = "INSPECTION")
    private Integer inspection;

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "NOTES")
    private String notes;

    public PQMItemInspectionRelatedItem() {
        super.setObjectType(PLMObjectType.INSPECTIONRELEATEDITEM);
    }


}
