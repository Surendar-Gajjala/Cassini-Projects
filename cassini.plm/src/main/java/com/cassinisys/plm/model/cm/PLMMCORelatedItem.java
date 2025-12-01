package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_MCO_RELATEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMMCORelatedItem extends CassiniObject {

    @Column(name = "MCO")
    private Integer mco;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART")
    private PLMManufacturerPart part;

    @Column(name = "NOTES")
    private String notes;
    @Transient
    private String partNumberPrint;
    @Transient
    private String partNamePrint;
    @Transient
    private String partTypePrint;


    public PLMMCORelatedItem() {
        super.setObjectType(PLMObjectType.MCOAFFECTEDITEM);
    }


}
